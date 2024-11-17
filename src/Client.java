import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Client {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        boolean registrationComplete = false;
        boolean loginComplete = false;

        try (Socket socket = new Socket("localhost", 1234);
             Scanner scan = new Scanner(System.in);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             ObjectInputStream objectReader = new ObjectInputStream(socket.getInputStream())) {

            do {
                // authentication method choice
                System.out.println("\nPlease Specify An Authentication Method");
                System.out.println("1 : Login\n2 : Register");
                String choice = scan.nextLine().trim();
                writer.println(choice); // sends choice to server

                if (choice.equalsIgnoreCase("1")) { // handles login process

                    System.out.println("Enter your username:");
                    String username = scan.nextLine();
                    writer.println(username);

                    System.out.println("Enter your password:");
                    String password = scan.nextLine();
                    writer.println(password);

                    loginComplete = objectReader.readBoolean();

                    if (loginComplete) {
                        System.out.println("Login Successful");
                    } else {
                        System.out.println("Login Failed. Please try again");
                    }

                } else if (choice.equalsIgnoreCase("2")) { // handle registration process

                    System.out.println("Enter a username:");
                    String username = scan.nextLine();
                    writer.println(username);

                    System.out.println("Enter an email:");
                    String email = scan.nextLine();
                    writer.println(email);

                    System.out.println("Enter a password:");
                    String password = scan.nextLine();
                    writer.println(password);

                    registrationComplete = objectReader.readBoolean();

                    if (registrationComplete) {
                        System.out.println("Registration complete.");
                    } else {
                        System.out.println("User already exists. Please login");
                    }

                }

            } while (!registrationComplete && !loginComplete); // loops until successful login/registration

            System.out.println("\n--------- Welcome To Swap_It ---------"); // welcome message on authentication

            // Main menu
            while (true) {
                System.out.println("\n>>> Main Menu : Please Enter A Number <<<");
                System.out.println("\n1 : Search" + "\n2 : Post" + "\n3 : Friends" + "\n4 : Account" + "\n5 : Exit\n");

                String menuAction = scan.nextLine();
                writer.println(menuAction);

                switch (menuAction) {
                    case "1": // handles user search
                        System.out.println("\nEnter a username to search:");
                        String searchUsername = scan.nextLine();
                        writer.println(searchUsername);

                        Object userProfile = objectReader.readObject();

                        if (userProfile instanceof String) {
                            System.out.println(userProfile); // "User not found" message
                        } else if (userProfile instanceof User) {
                            User user = (User) userProfile;
                            System.out.println("\nUser Exists!");
                            System.out.println("Username: " + user.getUsername());
                            System.out.println("Friends: " + user.getFriends().size());
                            System.out.println("Posts: " + user.getUserPosts().size());
                        } else {
                            System.out.println("User not found: " + userProfile);
                        }
                        break;

                    case "2": //handles post creation/deletion
                        System.out.println("Post Menu\n1 : Create\n2 : Delete");
                        String postAction = scan.nextLine();
                        writer.println(postAction);

                        if (postAction.equalsIgnoreCase("1")) {
                            System.out.println("Add a caption:");
                            String title = scan.nextLine();
                            writer.println(title);

                            System.out.println("Add an image:");
                            String imagePath = scan.nextLine();
                            writer.println(imagePath);

                            System.out.println("Post created!");
                        } else if (postAction.equalsIgnoreCase("2")) {
                            System.out.println("Enter caption of the post to delete:");
                            String titleToDelete = scan.nextLine();
                            writer.println(titleToDelete);

                            System.out.println("Post deleted.");
                        }
                        break;

                    case "3": //handles friend actions
                        System.out.println("\nFriend Menu\n1 : Add\n2 : Block\n3 : Remove\n4 : Unblock");
                        String friendAction = scan.nextLine();
                        writer.println(friendAction);

                        System.out.println("Enter username:");
                        String friendUsername = scan.nextLine();
                        writer.println(friendUsername);

                        String serverNotification = reader.readLine();
                        System.out.println(serverNotification);
                        break;

                    case "4": //handles viewing posts and account info
                        System.out.println("\nAccount Menu\n1 : View Your Posts\n2 : View Your Personal Info");
                        String viewAction = scan.nextLine();
                        writer.println(viewAction);

                        if (viewAction.equalsIgnoreCase("1")) {
                            Object posts = objectReader.readObject();

                            if (posts instanceof ArrayList<?>) {

                                ArrayList<NewsPost> userPosts = new ArrayList<>();

                                for (Object obj : (ArrayList<?>) posts) {

                                    if (obj instanceof NewsPost) {
                                        userPosts.add((NewsPost) obj);
                                    } else {
                                        System.out.println("found non-NewsPost obj");
                                        return;
                                    }
                                }

                                System.out.println();

                                for (NewsPost post : userPosts) {
                                    System.out.println(post.toString());
                                }

                            } else {
                                System.out.println("No posts available.");
                            }
                        } else if (viewAction.equals("2")) {
                            String[] info = reader.readLine().split(";");
                            System.out.println("\nUsername: " + info[0] + "\n"
                                    + "Email: " + info[1] + "\n"
                                    + "Password: " + info[2] + "\n"
                                    + "Friends: "+ info[3] + "\n"
                                    + "Blocked:  " + info[4]);
                        }
                        break;

                    case "5":
                        System.out.println("Goodbye!");
                        return;

                    default:
                        System.out.println("Invalid option. Try again.");
                }
            }
        }
    }
}