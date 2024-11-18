import java.io.*;
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

            // Authentication loop
            do {
                System.out.println("\nPlease Specify An Authentication Method");
                System.out.println("1 : Login\n2 : Register");
                String choice = scan.nextLine().trim();
                writer.println(choice);

                if (choice.equalsIgnoreCase("1")) { // Login process
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
                } else if (choice.equalsIgnoreCase("2")) { // Registration process
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
            } while (!registrationComplete && !loginComplete);

            System.out.println("\n--------- Welcome To Swap_It ---------");

            // Main menu
            while (true) {
                System.out.println("\n>>> Main Menu : Please Enter A Number <<<");
                System.out.println("1 : Search\n2 : Content\n3 : Friends\n4 : Account\n5 : Exit");

                String menuAction = scan.nextLine();
                writer.println(menuAction);

                switch (menuAction) {
                    case "1": // Search functionality
                        System.out.println("\nEnter a username to search:");
                        String searchUsername = scan.nextLine();
                        writer.println(searchUsername);

                        Object userProfile = objectReader.readObject();

                        if (userProfile instanceof String) {
                            System.out.println(userProfile);
                        } else if (userProfile instanceof User) {
                            User user = (User) userProfile;
                            System.out.println("\nUser Exists!");
                            System.out.println("Username: " + user.getUsername());
                            System.out.println("Friends: " + user.getFriends().size());
                            System.out.println("Posts: " + user.getUserPosts().size());

                            if (!user.getUserPosts().isEmpty()) {
                                System.out.println("Comment on " + user.getUsername() + "'s posts? : 'y' or 'n'");
                                String commentQuestion = scan.nextLine();
                                writer.println(commentQuestion);

                                if (commentQuestion.equalsIgnoreCase("y")) {
                                    Object posts = objectReader.readObject();
                                    if (posts instanceof ArrayList<?>) {
                                        ArrayList<NewsPost> userPosts = new ArrayList<>();
                                        for (Object obj : (ArrayList<?>) posts) {
                                            if (obj instanceof NewsPost) {
                                                userPosts.add((NewsPost) obj);
                                            } else {
                                                System.out.println("Found non-NewsPost object");
                                                return;
                                            }
                                        }

                                        System.out.println();
                                        for (NewsPost post : userPosts) {
                                            System.out.println(post.toString());
                                        }

                                        System.out.println("\nSelect The Post You Want To Comment On");
                                        for (int i = 0; i < userPosts.size(); i++) {
                                            System.out.println(i + 1 + " : " + userPosts.get(i).getCaption());
                                        }

                                        int commentMenu = scan.nextInt();
                                        writer.println(userPosts.get(commentMenu - 1).getCaption());

                                        System.out.println("Enter Comment: ");
                                        scan.nextLine(); // Consume newline
                                        String commentAnswer = scan.nextLine();
                                        writer.println(commentAnswer);
                                        System.out.println("Comment created!");

                                    }
                                } else {
                                    break;
                                }
                            }
                        } else {
                            System.out.println("User not found: " + userProfile);
                        }
                        break;

                    case "2": // Content management
                        System.out.println("Content Menu\n1 : Create Post\n2 : Delete Post\n3 : Delete Comment");
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
                            String captionToDelete = scan.nextLine();
                            writer.println(captionToDelete);

                            System.out.println("Post deleted.");

                        } else if (postAction.equalsIgnoreCase("3")) {

                            Object comments = objectReader.readObject();

                            if (comments instanceof ArrayList<?>) {
                                ArrayList<NewsComment> userComments = new ArrayList<>();
                                for (Object obj : (ArrayList<?>) comments) {
                                    if (obj instanceof NewsComment) {
                                        userComments.add((NewsComment) obj);
                                    } else {
                                        System.out.println("Found non-NewsComment object");
                                        return;
                                    }
                                }

                                if (userComments.isEmpty()) {
                                    System.out.println("No comments available.");
                                    break;
                                } else {
                                    System.out.println("Choose which comment to delete");
                                    for (int i = 0; i < userComments.size(); i++) {
                                        System.out.println(i + 1 + " : " + userComments.get(i).getContent());
                                    }
                                }

                                int commentMenu = scan.nextInt();
                                scan.nextLine();
                                writer.println(userComments.get(commentMenu - 1).getContent());
                                System.out.println("Comment Deleted");
                                break;
                            } else {
                                System.out.println("No posts available.");
                            }
                        }
                        break;

                    case "3": // Friend actions
                        System.out.println("\nFriend Menu\n1 : Add\n2 : Block\n3 : Remove\n4 : Unblock");
                        String friendAction = scan.nextLine();
                        writer.println(friendAction);

                        System.out.println("Enter username:");
                        String friendUsername = scan.nextLine();
                        writer.println(friendUsername);

                        String serverNotification = reader.readLine();
                        System.out.println(serverNotification);
                        break;

                    case "4": // Account info
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
                                        System.out.println("Found non-NewsPost object");
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
                                    + "Friends: " + info[3] + "\n"
                                    + "Blocked: " + info[4]);
                        }
                        break;

                    case "5": // Exit
                        System.out.println("Goodbye!");
                        return;

                    default:
                        System.out.println("Invalid option. Try again.");
                }
            }
        }
    }
}