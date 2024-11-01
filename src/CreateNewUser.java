import java.util.*;
import java.io.*;

// gotta add usernames/passwords to a file (buffered reader?)
public class CreateNewUser {
    private String username;
    private String password;
    private boolean alreadyRegistered;
    private static ArrayList<String> users = new ArrayList<>();
    private static ArrayList<String> passes = new ArrayList<>();
    private static final String filename = "users.txt";

    public CreateNewUser(String username, String password) {
        this.username = username;
        this.password = password;
        this.alreadyRegistered = checkIfUserExists(username);

        if (!alreadyRegistered) {
            users.add(username);
            passes.add(password);
            saveUserToFile();
        }
    }
    private boolean checkIfUserExists(String username) {
        return users.contains(username);
    }
    private void saveUserToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(username + ":" + password);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving user to file: " + e.getMessage());
        }
    }
    public static void loadUsersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    users.add(parts[0]);
                    passes.add(parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users from file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        loadUsersFromFile();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a username: ");
        String username = scanner.nextLine();

        System.out.println("Enter a password: ");
        String password = scanner.nextLine();

        CreateNewUser newUser = new CreateNewUser(username, password);

        if (newUser.alreadyRegistered == true) {
            System.out.println("User already registered!");
        } else{
            System.out.println("User registered successfully!");
        }

        scanner.close();
    }

}
