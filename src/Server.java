import java.net.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Server extends PasswordProtectedLogin implements Runnable {

    // Initializes the server socket at port 1234
    private Socket clientSocket;

    // Handles IOException during server initialization
    public Server(Socket socket) {
        this.clientSocket = socket;
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server is listening to port 1234...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Server: Client connected");

                Thread clientThread = new Thread(new Server(socket));
                clientThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handles client connections and actions
    public void run() {

        System.out.println(new File("users.txt").getAbsolutePath());

        String username = "";
        String email = "";
        String password = "";
        boolean loginComplete = false;
        boolean registrationComplete = false;

        // Try with resources for socket communication
        try (BufferedReader read = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter write = new PrintWriter(clientSocket.getOutputStream(), true);
             ObjectOutputStream objectWrite = new ObjectOutputStream(clientSocket.getOutputStream());
             BufferedReader readPost = new BufferedReader(new FileReader("newsPosts.txt"))) {

            System.out.println("Server: Client connected");
            UserProfile currentUser = null;
            String loginOrRegister = read.readLine(); // Login or registration choice

            if (loginOrRegister.equals("1")) {

                System.out.println("User selected login");

                // Loop until a successful login
                do {
                    username = read.readLine();
                    System.out.println("Received username: " + username);

                    password = read.readLine();
                    System.out.println("Received password: " + password);

                    loginComplete = authenticate(username, password);

                    objectWrite.writeBoolean(loginComplete);
                    objectWrite.flush();

                } while (!loginComplete);

            } else if (loginOrRegister.equalsIgnoreCase("2")) {

                System.out.println("User selected register");

                // Loop until a successful registration
                do {
                    username = read.readLine();
                    System.out.println("Received username");

                    email = read.readLine();
                    System.out.println("Received email");

                    password = read.readLine();
                    System.out.println("Received password");

                    CreateNewUser tempUser = new CreateNewUser(username, email, password);

                    if (!tempUser.isAlreadyRegistered()) { // Checks if user already exists
                        System.out.println("User: " + username + " created and saved to file");
                        registrationComplete = true;
                        objectWrite.writeBoolean(registrationComplete);
                        objectWrite.flush();
                    } else {
                        System.out.println("User already exists, please login");
                        objectWrite.writeBoolean(registrationComplete);
                        objectWrite.flush();
                    }

                } while (!registrationComplete);
            }

            currentUser = UserSearch.findUserByUsername(username);

            // Handles actions while logged in
            while (loginComplete) {

                String menu = read.readLine();
                String prompt = "";

                switch (menu) {
                    case "1" -> { // Search for a user
                        prompt = read.readLine();
                        System.out.println("Searching for user: " + prompt);

                        UserProfile searchedUser = UserSearch.findUserByUsername(prompt);

                        if (searchedUser == null) {
                            objectWrite.writeObject("User not found");
                            objectWrite.flush();
                        } else {
                            objectWrite.writeObject(searchedUser);
                            objectWrite.flush();

                            String commentQuestion = read.readLine();

                            boolean commentMenu = false;

                            do {
                                if (commentQuestion.equalsIgnoreCase("y")) {

                                    ArrayList<NewsPost> searchedUserPosts = searchedUser.getUserPosts();

                                    synchronized (this) {
                                        objectWrite.writeObject(searchedUserPosts);
                                    }
                                    commentMenu = true;

                                    String postToCommentOn = read.readLine();
                                    String commentAnswer = read.readLine();

                                    for (NewsPost newsPost : searchedUserPosts) {
                                        if (newsPost.getCaption().equals(postToCommentOn)) {
                                            NewsComment newComment = new NewsComment(commentAnswer,
                                                    currentUser.getUsername(), postToCommentOn);
                                            newComment.saveToFile();
                                            newsPost.addComment(newComment);
                                        }
                                    }

                                } else if (commentQuestion.equalsIgnoreCase("n")) {
                                    commentMenu = true;
                                } else {
                                    System.out.println("Invalid input received");
                                }
                            } while (!commentMenu);
                        }
                        objectWrite.flush();
                    }
                    case "2" -> { // Create/delete post
                        prompt = read.readLine();
                        if (prompt.equals("1")) {
                            synchronized (this) {
                                String title = read.readLine();
                                String imagePath = read.readLine();
                                String date = String.valueOf(LocalDate.now());
                                new NewsPost(username, title, imagePath, date);
                            }
                        } else if (prompt.equals("2")) {
                            synchronized (this) {
                                String title = read.readLine();
                                NewsPost.deletePost(title);
                            }
                        } else if (prompt.equals("3")) {

                            ArrayList<NewsComment> usersComments = currentUser.findCommentsForUser();
                            synchronized (this) {
                                objectWrite.writeObject(usersComments);
                                String commentAnswer = read.readLine();
                                NewsComment.deleteComment(commentAnswer);
                            }
                        }
                    }
                    case "3" -> { // Add/block/remove friends
                        prompt = read.readLine();
                        switch (prompt) {
                            case "1" -> {
                                String friendToAdd = read.readLine();
                                currentUser.addFriend(friendToAdd);
                                currentUser.updateFriendsList();
                                write.println("Added friend: " + friendToAdd);
                            }
                            case "2" -> {
                                String userToBlock = read.readLine();
                                currentUser.blockUser(userToBlock);
                                currentUser.updateBlockedList();
                                write.println("Blocked: " + userToBlock);
                            }
                            case "3" -> {
                                String friendToRemove = read.readLine();
                                currentUser.removeFriend(friendToRemove);
                                currentUser.updateFriendsList();
                                write.println("Removed friend: " + friendToRemove);
                            }
                            case "4" -> {
                                String userToUnblock = read.readLine();
                                currentUser.removeBlockedUser(userToUnblock);
                                currentUser.updateBlockedList();
                                write.println("Unblocked: " + userToUnblock);
                            }
                            default -> write.println("A valid input was not selected!");
                        }
                    }
                    case "4" -> { // View posts and info
                        prompt = read.readLine();
                        switch (prompt) {
                            case "1" -> {
                                synchronized (this) {
                                    objectWrite.writeObject(currentUser.getUserPosts());
                                }
                            }
                            case "2" -> write.println(currentUser.getAccountInfo());
                            default -> System.out.println("A valid input was not selected!");
                        }
                    }
                    default -> System.out.println("A valid input was not selected!");
                }
            }

        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                System.out.println("Client disconnected");
            } catch (IOException e) {
                System.out.println("Error closing socket: " + e.getMessage());
            }
        }
    }
}
//push comment