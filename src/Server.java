import java.net.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Server extends PasswordProtectedLogin implements Runnable {

    //initializes the server socket at port 1234
    private Socket clientSocket;

    //handles IOException during server initialization
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

    //handles client connections and actions
    public void run() {

        System.out.println(new File("users.txt").getAbsolutePath());

        String username = "";
        String email = "";
        String password = "";
        boolean loginComplete = false;
        boolean registrationComplete = false;

        //try with resources for socket communication
        try (BufferedReader read = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter write = new PrintWriter(clientSocket.getOutputStream(), true);
             ObjectOutputStream objectWrite = new ObjectOutputStream(clientSocket.getOutputStream());
             BufferedReader readPost = new BufferedReader(new FileReader("newsPosts.txt"))) {

            System.out.println("Server: Client connected");
            UserProfile currentUser = null;
            String loginOrRegister = read.readLine(); //login or registration choice

            if (loginOrRegister.equals("login")) {

                System.out.println("User selected login");

                // loop until a successful login
                do {
                    username = read.readLine();
                    System.out.println("received username");

                    password = read.readLine();
                    System.out.println("received password");

                    loginComplete = authenticate(username, password);

                    objectWrite.writeBoolean(loginComplete);
                    objectWrite.flush();

                } while (!loginComplete);

            } else if (loginOrRegister.equalsIgnoreCase("register")) {

                System.out.println("User selected register");

                // loop until a successful registration
                do {

                    username = read.readLine();
                    System.out.println("received username");

                    email = read.readLine();
                    System.out.println("received email");

                    password = read.readLine();
                    System.out.println("received password");

                    CreateNewUser tempUser = new CreateNewUser(username, email, password);

                    if (!tempUser.isAlreadyRegistered()) { // checks if user already exists
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

            //handles actions while logged in
            while (loginComplete) {

                String menu = read.readLine();
                String prompt = "";

                switch (menu) {
                    //switch case for comment stuff
                    //IMPORTANT: make delete comment & post methods (after this is done we can just call this method)
                    case "1" -> { // search for a user

                        prompt = read.readLine();
                        System.out.println("searching for user: " + prompt);

                        UserProfile searchedUser = UserSearch.findUserByUsername(prompt);

                        if (searchedUser == null) {
                            objectWrite.writeObject("User not found");
                        } else {
                            objectWrite.writeObject(searchedUser);
                        }

                        objectWrite.flush();
                        break;
                    }
                    case "2" -> { // create/delete post and delete post
                        prompt = read.readLine();
                        if (prompt.equals("create")) {
                            synchronized (this) {
                                String title = read.readLine();
                                String imagePath = read.readLine();
                                String date = String.valueOf(LocalDate.now());
                                new NewsPost(username, title, imagePath, date);
                            }
                            //make sure this saves to a file somehow
                        } else if (prompt.equals("delete")) {
                            //IMPORTANT: need to delete comments associated with post as well
                            //make an arraylist of all posts made by the user and dropdown of title
                            synchronized (this) {
                                String title = read.readLine();
                                //needs to search through a file for a title and then delete the info of that post
                                NewsPost.deletePost(title);
                            }
                            //make sure each post's info is on one line
                        } else if (prompt.equals("delete comment")) {

                            String content = read.readLine();
                            NewsPost.deleteComment(content);

                        }
                    }
                    case "3" -> { // add/block/remove friends
                        prompt = read.readLine();
                        switch (prompt) {
                            case "add" -> {
                                String friendToAdd = read.readLine();
                                currentUser.addFriend(friendToAdd);
                            }
                            case "block" -> {
                                String userToBlock = read.readLine();
                                currentUser.blockUser(userToBlock);
                            }
                            case "remove" -> {
                                String friendToRemove = read.readLine();
                                currentUser.removeFriend(friendToRemove);
                            }
                            default -> {
                                System.out.println("A valid input was not selected!");
                                throw new Exception();
                            }
                        }
                    }
                    case "4" -> {  // view posts and info
                        prompt = read.readLine();
                        switch(prompt) {
                            case "post" -> {
                                //load posts for this specfic user
                                //IMPORTANT: change file & newsPost to put all post info on one line
                                synchronized (this) {
                                    String line;
                                    ArrayList<NewsPost> userPosts = new ArrayList<>();

                                    while ((line = readPost.readLine()) != null) {
                                        if (line.contains(currentUser.getUsername())) {

                                            String[] postInfo = line.split(",");

                                            NewsPost tempPost = new NewsPost();
                                            tempPost.setAuthor(postInfo[0]);
                                            tempPost.setCaption(postInfo[1]);
                                            tempPost.setImagePath(postInfo[2]);
                                            tempPost.setDate(postInfo[3]);
                                            tempPost.setUpvotes(Integer.parseInt(postInfo[4]));
                                            tempPost.setDownvotes(Integer.parseInt(postInfo[5]));
                                            tempPost.setComments(NewsPost.findComments(postInfo[1]));

                                            userPosts.add(tempPost);
                                        }
                                    }
                                    objectWrite.writeObject(userPosts);
                                }
                                //need to parse through arraylist & make array splitting semicolons and make pretty
                            }
                            case "info" -> {
                                //load info for this specfic user
                                StringBuilder accountInfo = new StringBuilder();
                                accountInfo.append(currentUser.getUsername() + ";");
                                accountInfo.append(currentUser.getEmail() + ";");
                                accountInfo.append(currentUser.getPassword() + ";");
                                accountInfo.append(currentUser.getFriends() + ";");
                                accountInfo.append(currentUser.getBlockedFriends() + ";");
                                //could possibly return string value of number of posts
                                write.println(accountInfo);
                            }
                            default -> {
                                System.out.println("A valid input was not selected!");
                                throw new Exception();
                            }
                        }
                    }
                    default -> {
                        System.out.println("A valid input was not selected!");
                        throw new Exception();
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();;
                System.out.println("Client disconnected");
            } catch (IOException e) {
                System.out.println("Error closing socket: " + e.getMessage());
            }
        }
    }
}
