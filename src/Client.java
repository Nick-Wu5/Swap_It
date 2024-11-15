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
                System.out.println("\n--> 'Login' or 'Register' <--\n");
                String choice = scan.nextLine().trim();
                writer.println(choice); // sends choice to server

                if (choice.equalsIgnoreCase("login")) { // handles login process

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

                } else if (choice.equalsIgnoreCase("register")) { // handle registration process

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
                System.out.println("\n1 : Search" + "\n2 : Post" + "\n3 : Friends" + "\n4 : View" + "\n5 : Exit\n");

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
                            System.out.println("Found user: " + user.getUsername());

                        } else {
                            System.out.println("Received an unexpected response: " + userProfile);
                        }
                        break;

                    case "2": //handles post creation/deletion
                        System.out.println("Do you want to 'create' or 'delete' a post?");
                        String postAction = scan.nextLine();
                        writer.println(postAction);

                        if (postAction.equalsIgnoreCase("create")) {
                            System.out.println("Add a caption:");
                            String title = scan.nextLine();
                            writer.println(title);

                            System.out.println("Add an image:");
                            String imagePath = scan.nextLine();
                            writer.println(imagePath);

                            System.out.println("Post created!");
                        } else if (postAction.equalsIgnoreCase("delete")) {
                            System.out.println("Enter caption of the post to delete:");
                            String titleToDelete = scan.nextLine();
                            writer.println(titleToDelete);

                            System.out.println("Post deleted.");
                        }
                        break;

                    case "3": //handles friend actions
                        System.out.println("Do you want to 'add', 'block', or 'remove' a friend?");
                        String friendAction = scan.nextLine();
                        writer.println(friendAction);

                        System.out.println("Enter username:");
                        String friendUsername = scan.nextLine();
                        writer.println(friendUsername);

                        System.out.println("Friend action performed.");
                        break;

                    case "4": //handles viewing posts and account info
                        System.out.println("Do you want to view 'post' or 'info'?");
                        String viewAction = scan.nextLine();
                        writer.println(viewAction);

                        if (viewAction.equalsIgnoreCase("post")) {
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

                                for (NewsPost post : userPosts) {
                                    System.out.println(post.toString());
                                }

                            } else {
                                System.out.println("No posts available.");
                            }
                        } else if (viewAction.equals("info")) {
                            String info = reader.readLine();
                            System.out.println("Account Info: " + info);
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