import java.lang.reflect.Array;
import java.net.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Server extends PasswordProtectedLogin {

    ServerSocket serverSocket = new ServerSocket(1234);

    public Server() throws IOException {
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {

        String username = "";
        String email = "";
        String password = "";
        boolean loggedIn = false;
        boolean alreadyRegistered = false;

        try (Socket socket = serverSocket.accept();
             BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter write = new PrintWriter(socket.getOutputStream());
             ObjectOutputStream objectWrite = new ObjectOutputStream(socket.getOutputStream());
             BufferedReader readPost = new BufferedReader(new FileReader("newsPosts.txt"))) {

            System.out.println("Server: Client connected");
            UserProfile currentUser = null;
            String loginOrRegister = read.readLine();

            if (loginOrRegister.equals("login")) {
                do {
                    while (username.isEmpty() || password.isEmpty()) {
                        String line = read.readLine();
                        if (line.contains("username")) {
                            username = line.substring(read.readLine().indexOf(":") + 1);
                        } else if (line.contains("password")) {
                            password = read.readLine().substring(read.readLine().indexOf(":") + 1);
                        }
                    }

                    loggedIn = authenticate(username, password);

                    if (!loggedIn) {
                        write.println("Incorrect username or password");
                    }

                } while (!loggedIn);

            } else if (loginOrRegister.equals("register")) {

                do {

                    while (username.isEmpty() && email.isEmpty() && password.isEmpty()) {

                        if (read.readLine().contains("username")) {
                            username = read.readLine().substring(read.readLine().indexOf(":") + 1);
                        }

                        if (read.readLine().contains("email")) {
                            email = read.readLine().substring(read.readLine().indexOf(":") + 1);
                        }

                        if (read.readLine().contains("password")) {
                            password = read.readLine().substring(read.readLine().indexOf(":") + 1);
                        }
                    }

                    CreateNewUser tempUser = new CreateNewUser(username, email, password);
                    alreadyRegistered = tempUser.isAlreadyRegistered();

                } while (alreadyRegistered);
            }

            while (loggedIn) {

                currentUser = UserSearch.findUserByUsername(username);
                String menu = read.readLine();
                String prompt = "";

                switch (menu) {
                    //switch case for comment stuff
                    //IMPORTANT: make delete comment & post methods (after this is done we can just call this method)
                    case "search" -> {
                        prompt = read.readLine();
                        UserProfile searchedUser = UserSearch.findUserByUsername(prompt);
                        objectWrite.writeObject(searchedUser);
                        objectWrite.flush();
                        // deal with the null on the client side & resend menu
                    }
                    case "post" -> {
                        prompt = read.readLine();
                        if (prompt.equals("create")) {
                            String title = read.readLine();
                            String imagePath = read.readLine();
                            String date = String.valueOf(LocalDate.now());
                            new NewsPost(username, title, imagePath, date);
                            //make sure this saves to a file somehow
                        } else if (prompt.equals("delete")) {
                            //IMPORTANT: need to delete comments associated with post as well
                            //make an arraylist of all posts made by the user and dropdown of title
                            String title = read.readLine();
                            //needs to search through a file for a title and then delete the info of that post
                            NewsPost.deletePost(title);
                            //make sure each post's info is on one line
                        } else if (prompt.equals("delete comment")) {

                            String content = read.readLine();
                            NewsPost.deleteComment(content);

                        }
                    }
                    case "friend" -> {
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
                    case "view" -> {
                        prompt = read.readLine();
                        switch(prompt) {
                            case "post" -> {
                                //load posts for this specfic user
                                //IMPORTANT: change file & newsPost to put all post info on one line
                                String line;
                                ArrayList<NewsPost> userPosts = new ArrayList<>();

                                while ((line = readPost.readLine()) != null) {
                                    if(line.contains(currentUser.getUsername())) {

                                        String[] postInfo = line.split(",");

                                        NewsPost tempPost = new NewsPost(postInfo[0], postInfo[1], postInfo[2], postInfo[3]);

                                        tempPost.setComments(NewsPost.findComments(postInfo[1]));
                                        userPosts.add(tempPost);
                                    }
                                }
                                objectWrite.writeObject(userPosts);
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
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
