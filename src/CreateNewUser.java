import java.util.*;
import java.io.*;

public class CreateNewUser extends UserProfile {
    private static String dateJoined;
    private String username;
    private String password;
    private boolean alreadyRegistered;
    private static ArrayList<UserProfile> userProfiles = new ArrayList<>();
    private static final String filename = "users.txt";

    public CreateNewUser(String username, String password) {
        super(username,username + "@example.com", password, dateJoined);
        this.username = username;
        this.password = password;
        this.alreadyRegistered = checkIfUserExists(username);

        if (!alreadyRegistered) { //redo
            UserProfile newUserProfile = new UserProfile(username, username + "@example.com", password, getDateJoined());
            userProfiles.add(newUserProfile);
            saveUserToFile();
        }
    }
    private boolean checkIfUserExists(String username) {
        for (UserProfile profile : userProfiles) {
            if (profile.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
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
                    String username = parts[0];
                    String password = parts[1];
                    UserProfile userProfile = new UserProfile(username, username + "@example.com",password, dateJoined);
                    userProfiles.add(userProfile);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users from file: " + e.getMessage());
        }
    }
    public UserProfile getUserProfile() {
        if (!alreadyRegistered) {
            return userProfiles.get(userProfiles.size() - 1); // Return the newly added user
        } else {
            System.out.println("User already registered, cannot create profile.");
            return null;
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
            UserProfile profile = newUser.getUserProfile();
            if (profile != null) {
                System.out.println("User profile created for " + profile.getUsername());
        }

        scanner.close();
    }

}
}
