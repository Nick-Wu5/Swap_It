import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

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
public class CreateNewUser extends UserProfile implements CreateNewUserInterface {

    private boolean alreadyRegistered;  //signifies whether account has been registered or not
    private static ArrayList<UserProfile> userProfiles;  //list of userProfiles
    private static final String FILENAME = "users.txt";  //file name for user.txt

    public CreateNewUser(String username, String email, String password) {

        super(username, email, password);

        this.alreadyRegistered = this.checkIfUserExists(username);

        if (!this.alreadyRegistered) {
            super.saveToFile();
            System.out.println("User registered successfully");
        } else {
            System.out.println("User already registered, cannot create profile.");
        }
    }

    public CreateNewUser() {
        this.alreadyRegistered = false;
        userProfiles = null;
    }

    public static ArrayList<UserProfile> getUserProfiles() {
        return userProfiles;
    }

    public boolean isAlreadyRegistered() {
        return this.alreadyRegistered;
    }

    @Override
    public boolean addFriend() {
        return false;
    }

    @Override
    public void unblockUser(String userToUnblock) {

    }

    public void setAlreadyRegistered(boolean alreadyRegistered) {
        this.alreadyRegistered = alreadyRegistered;
    }

    public UserProfile getUser() {
        if (!this.alreadyRegistered) {
            return (UserProfile) userProfiles.get(userProfiles.size() - 1);
        } else {
            System.out.println("User already registered, cannot create profile.");
            return null;
        }
    }

    private boolean checkIfUserExists(String username) {

        CreateNewUser.loadUsersFromFile();

        for (UserProfile user : userProfiles) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static void loadUsersFromFile() {
        userProfiles = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) { // Ensure there are at least three parts
                    String username = parts[0];
                    String email = parts[1];
                    String password = parts[2];
                    UserProfile userProfile = new UserProfile(username, email, password);
                    userProfiles.add(userProfile);
                } else {
                    System.out.println("skipping weird line");
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + FILENAME);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading from file: " + FILENAME);
            e.printStackTrace();
        }
    }
}
