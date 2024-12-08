import java.io.*;
import java.util.ArrayList;

/**
 * CreateNewUser
 * <p>
 * Handles the creation of new user profiles in the social media application. It ensures that
 * new accounts are unique by checking against existing usernames. Successfully created accounts
 * are saved to a file and added to the system's user profile list.
 *
 * @version December 7, 2024
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
            if (userProfiles == null) {
                userProfiles = new ArrayList<>();
            }
            userProfiles.add(this);
            System.out.println("User registered successfully");
        } else {
            System.out.println("User already registered, cannot create profile.");
        }
    }

    public CreateNewUser() {
        this.alreadyRegistered = false;
        userProfiles = null;
    }

    /**
     * Retrieves the list of all user profiles.
     *
     * @return an ArrayList containing all user profiles
     */
    public static ArrayList<UserProfile> getUserProfiles() {
        return userProfiles;
    }

    /**
     * Checks whether the user is already registered.
     *
     * @return true if the user is already registered, false otherwise
     */
    public boolean isAlreadyRegistered() {
        return this.alreadyRegistered;
    }

    /**
     * Retrieves the user profile of the currently logged-in or last registered user.
     *
     * @return the UserProfile object of the user, or null if none exists
     */
    public UserProfile getUser() {
        if (!this.alreadyRegistered) {
            if (userProfiles != null && !userProfiles.isEmpty()) {
                return (UserProfile) userProfiles.get(userProfiles.size() - 1);
            } else {
                System.out.println("User already registered, cannot create profile.");
                return null;
            }
        } else {
            System.out.println("User already registered, cannot create profile.");
            return null;
        }
    }

    /**
     * Checks if a username already exists in the user profiles list.
     *
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
    private boolean checkIfUserExists(String username) {

        CreateNewUser.loadUsersFromFile();

        for (UserProfile user : userProfiles) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Loads user profiles from the file into the user profiles list. Reads the file line
     * by line and creates UserProfile objects for each valid entry.
     */
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