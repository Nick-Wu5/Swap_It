import java.util.*;
import java.io.*;

public class PasswordProtectedLogin implements PasswordProtectedLoginInterface {
    private static final String filename = "users.txt";
    private static ArrayList<String> users = new ArrayList<>();
    private static ArrayList<String> passes = new ArrayList<>();


    public PasswordProtectedLogin() {
        loadUsersFromFile();
    }

    private void loadUsersFromFile() {
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


    public boolean authenticate(String username, String password) { //made this method public, so it is accessible by the test case
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
