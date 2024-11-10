import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Client {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Scanner scan = new Scanner(System.in);

        // Connect to the server
        Socket socket = new Socket("localhost", 4242);

        // Input/Output streams
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true); // Auto-flush
        ObjectInputStream objectReader = new ObjectInputStream(socket.getInputStream());

        try {
            System.out.println("Do you want to 'login' or 'register'?");
            String choice = scan.nextLine().trim();
            writer.println(choice);

            if (choice.equalsIgnoreCase("login")) {
                System.out.println("Enter your username:");
                String username = scan.nextLine();
                writer.println("username:" + username);

                System.out.println("Enter your password:");
                String password = scan.nextLine();
                writer.println("password:" + password);

                String loginResponse = reader.readLine();
                if (loginResponse.contains("Incorrect")) {
                    System.out.println("Login failed. Try again.");
                    return;
                }
            } else if (choice.equalsIgnoreCase("register")) {
                System.out.println("Enter a username:");
                String username = scan.nextLine();
                writer.println("username:" + username);

                System.out.println("Enter an email:");
                String email = scan.nextLine();
                writer.println("email:" + email);

                System.out.println("Enter a password:");
                String password = scan.nextLine();
                writer.println("password:" + password);

                System.out.println("Registration complete.");
            }

            // Main menu
            while (true) {
                System.out.println("Choose an action: search, post, friend, view, or exit");
                String menuAction = scan.nextLine();
                writer.println(menuAction);

                switch (menuAction.toLowerCase()) {
                    case "search":
                        System.out.println("Enter username to search:");
                        String searchUsername = scan.nextLine();
                        writer.println(searchUsername);

                        Object userProfile = objectReader.readObject();
                        if (userProfile == null) {
                            System.out.println("User not found.");
                        } else {
                            System.out.println("User found: " + userProfile);
                        }
                        break;

                    case "post":
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

                    case "friend":
                        System.out.println("Do you want to 'add', 'block', or 'remove' a friend?");
                        String friendAction = scan.nextLine();
                        writer.println(friendAction);

                        System.out.println("Enter username:");
                        String friendUsername = scan.nextLine();
                        writer.println(friendUsername);

                        System.out.println("Friend action performed.");
                        break;

                    case "view":
                        System.out.println("Do you want to view 'post' or 'info'?");
                        String viewAction = scan.nextLine();
                        writer.println(viewAction);

                        if (viewAction.equalsIgnoreCase("post")) {
                            Object posts = objectReader.readObject();
                            if (posts instanceof ArrayList) {
                                ArrayList<String> postList = (ArrayList<String>) posts;
                                for (String post : postList) {
                                    System.out.println(post);
                                }
                            } else {
                                System.out.println("No posts available.");
                            }
                        } else if (viewAction.equals("info")) {
                            String info = reader.readLine();
                            System.out.println("Account Info: " + info);
                        }
                        break;

                    case "exit":
                        System.out.println("Goodbye!");
                        return;

                    default:
                        System.out.println("Invalid option. Try again.");
                }
            }
        } finally {
            // Close resources
            objectReader.close();
            writer.close();
            reader.close();
            socket.close();
            scan.close();
        }
    }
}