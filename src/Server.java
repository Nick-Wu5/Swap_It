import java.lang.reflect.Array;
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

            //Loop until sign-in is successful
            do {
                String loginOrRegister = read.readLine(); // Login or registration choice

                if (loginOrRegister.equals("1")) {

                    System.out.println("User selected login");

                    // Loop until a successful login
                    username = read.readLine();
                    System.out.println("Received username: " + username);

                    password = read.readLine();
                    System.out.println("Received password: " + password);

                    loginComplete = authenticate(username, password);

                    objectWrite.writeBoolean(loginComplete);
                    objectWrite.flush();

                } else if (loginOrRegister.equalsIgnoreCase("2")) {

                    System.out.println("User selected register");

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
                }
            } while (!loginComplete && !registrationComplete);

             currentUser = UserSearch.findUserByUsername(username);
             //objectWrite.writeObject(currentUser);

            // Handles actions while logged in
            while (loginComplete) {

                System.out.println("Looking for input");
                String menu = read.readLine();
                String prompt = "";

                switch (menu) {
                    case "1" -> { // Search for a user
                        System.out.println("user selected search");
                        prompt = read.readLine();
                        System.out.println("Searching for user: " + prompt);

                        UserProfile searchedUser = UserSearch.findUserByUsername(prompt);

                        if (searchedUser == null) {
                            objectWrite.writeObject("User not found");
                            objectWrite.flush();
                        } else {
                            objectWrite.writeObject(searchedUser);
                            objectWrite.flush();

                            String command = read.readLine();

                            switch (command) {
                                case "COMMENT":

                                    String postCaption = read.readLine();
                                    String commentText = read.readLine();

                                    //find post here by caption
                                    NewsPost targetPost = null;
                                    synchronized (searchedUser) {
                                        for (NewsPost post : searchedUser.getUserPosts()) {
                                            if (post.getCaption().equals(postCaption)) {
                                                targetPost = post;
                                                break;
                                            }
                                        }
                                    }

                                    if (targetPost != null) {
                                        // Create and add the new comment
                                        NewsComment newComment = new NewsComment(commentText, currentUser.getUsername(),
                                                postCaption);
                                        newComment.saveToFile(); // Save the comment to file
                                        targetPost.addComment(newComment); // Add the comment to the post
                                        System.out.println("Comment added to post: " + postCaption);
                                    } else {
                                        System.out.println("Post not found for caption: " + postCaption);
                                    }
                                    break;
                                case "VIEW":
                                    System.out.println("user is viewing comments");
                                    break;
                            }
                        }
                        objectWrite.flush();
                    }
                    case "2" -> { // Create/delete post
                        System.out.println("User selected content");
                        prompt = read.readLine();
                        if (prompt.equals("1")) {
                            System.out.println("user trying to create post");
                            synchronized (this) {
                                String title = read.readLine();
                                String imagePath = read.readLine();
                                String date = String.valueOf(LocalDate.now());
                                new NewsPost(username, title, imagePath, date);
                                System.out.println("tried to create new post");
                            }
                        } else if (prompt.equals("2")) {
                            System.out.println("user trying to delete post");
                            synchronized (this) {
                                String title = read.readLine();
                                NewsPost.deletePost(title);
                                System.out.println("tried to delete post: " + title);
                            }
                        } else if (prompt.equals("3")) {

                            System.out.println("user trying to delete comment");
                            synchronized (this) {
                                String commentAnswer = read.readLine();
                                NewsComment.deleteComment(commentAnswer);
                                System.out.println("deleted: " + commentAnswer);
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
                        System.out.println("Looking at user info stuff");
                        prompt = read.readLine();
                        switch (prompt) {
                            case "1" -> {
                                synchronized (this) {
                                    if (!(currentUser.getUserPosts().isEmpty())){
                                        objectWrite.writeObject(currentUser.getUserPosts());
                                    } else {
                                        objectWrite.writeObject("user has no posts");
                                    }
                                }
                            }
                            case "2" -> write.println(currentUser.getAccountInfo());
                            case "exit" -> {}
                            default -> System.out.println("A valid input was not selected!");
                        }
                    }
                    case "5" -> {
                        System.out.println("user selected home");
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