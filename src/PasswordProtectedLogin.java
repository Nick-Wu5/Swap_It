import java.util.*;
import java.io.*;

/**
 * Team Project - Social Media App
 * <p>
 * This program provides a social networking system that allows users to create password-protected accounts and log in
 * securely. It includes features for searching and viewing other user profiles, as well as options to add, block, or
 * remove friends. The system also supports account and relationship management for an interactive user experience.
 *
 * @version November 3, 2024
 * @author Nick Wu, Chris Brantley, Ramya Prasanna, and Divya Vemireddy
 */
public class PasswordProtectedLogin implements PasswordProtectedLoginInterface {
    private static final String FILENAME = "users.txt";  //file name of users.txt
    private static ArrayList<String> users = new ArrayList<>();  //list of users
    private static ArrayList<String> passes = new ArrayList<>();  //lis of passes


    public PasswordProtectedLogin() {
        loadUsersFromFile();
    }

    private void loadUsersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;  //temp line of each line in users.txt
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

    public static ArrayList<String> getUsers() {
        return PasswordProtectedLogin.users;
    }

    public static ArrayList<String> getPasses() {
        return PasswordProtectedLogin.passes;
    }

    public boolean login() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter username: ");
        String username = scanner.nextLine();

        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        boolean authenticated = authenticate(username, password);

        if (authenticated) {
            System.out.println("Login successful! Welcome, " + username);
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }

        return authenticated;
    }


    public boolean authenticate(String username, String password) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).equals(username) && passes.get(i).equals(password)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        PasswordProtectedLogin loginSystem = new PasswordProtectedLogin();
        loginSystem.login();
    }
}
