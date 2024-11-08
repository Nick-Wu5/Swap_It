import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
    private static ArrayList<UserProfile> userProfiles = new ArrayList();  //list of userProfiles
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

    private boolean checkIfUserExists(String userName) {
        Iterator var2 = userProfiles.iterator();

        UserProfile profile;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            profile = (UserProfile) var2.next();
        } while (!profile.getUsername().equals(userName));

        return true;
    }

    public static void loadUsersFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("users.txt"));

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        String username = parts[0];
                        String password = parts[1];
                        UserProfile userProfile = new UserProfile(username, username + "@example.com", password);
                        userProfiles.add(userProfile);
                    }
                }
            } catch (Throwable var7) {
                try {
                    reader.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }

                throw var7;
            }

            reader.close();
        } catch (IOException var8) {
            IOException e = var8;
            System.out.println("Error reading users from file: " + e.getMessage());
        }

    }
}
