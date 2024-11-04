import java.util.*;
import java.io.*;

public class CreateNewUser extends UserProfile {
    private String username; //unique name associated with this user
    private String password; //password associated with user
    private boolean alreadyRegistered; //checks if the username is already a registered account
    private static ArrayList<UserProfile> userProfiles = new ArrayList<>(); //a list of all user profiles, everytime one is added the entire array list will be changed
    private static final String filename = "users.txt"; //name of the file that stores user profiles

    public CreateNewUser(String username, String password) {
        super(username,username + "@example.com", password);
        this.username = username;
        this.password = password;
        this.alreadyRegistered = checkIfUserExists(username);

        if (!alreadyRegistered) { //this is messing with the test cases but i don't know how to fix it
            UserProfile newUserProfile = new UserProfile(username, username + "@example.com", password);
            userProfiles.add(newUserProfile);
            saveUserToFile();
            System.out.println("User registered successfully!");
        } else {
            System.out.println("User already registered, cannot create profile.");
        }
    }

    public CreateNewUser() {
        this.username = null;
        this.password = null;
        this.alreadyRegistered = false;
        CreateNewUser.userProfiles = null;
    }

    public static ArrayList<UserProfile> getUserProfiles() {
        return CreateNewUser.userProfiles;
    }

    public String getUsername() {
        return this.username;
    }
    public String getPassword() {
        return this.password;
    }
    public boolean isAlreadyRegistered() {
        return this.alreadyRegistered;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setAlreadyRegistered(boolean alreadyRegistered) {
        this.alreadyRegistered = alreadyRegistered;
    }

    public UserProfile getUser() { //should not be confused with the getter method getUserProfiles
        if (!alreadyRegistered) {
            return userProfiles.get(userProfiles.size() - 1); // Return the newly added user
        } else {
            System.out.println("User already registered, cannot create profile.");
            return null;
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
                    UserProfile userProfile = new UserProfile(username, username + "@example.com",password);
                    userProfiles.add(userProfile);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users from file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        loadUsersFromFile();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter an email address: ");
        String username = scanner.nextLine().split("@")[0];

        System.out.println("Enter a password: ");
        String password = scanner.nextLine();

        CreateNewUser newUser = new CreateNewUser(username, password);

        if (newUser.alreadyRegistered) {
            System.out.println("User already registered!");
        } else{
            System.out.println("User registered successfully!");
            UserProfile profile = newUser.getUser();
            if (profile != null) {
                System.out.println("User profile created for " + profile.getUsername());
        }

        scanner.close();
    }

}
}
