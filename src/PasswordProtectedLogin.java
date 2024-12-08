import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * PasswordProtectedLogin
 * <p>
 * Provides a secure login system for the social media application. This class manages user authentication
 * by loading usernames and passwords from a file and verifying user credentials.
 *
 * @version December 7, 2024
 * @author
 * Nick Wu, Chris Brantley, Ramya Prasanna, and Divya Vemireddy
 */
public class PasswordProtectedLogin implements PasswordProtectedLoginInterface {
    private static final String FILENAME = "users.txt";  //file name of users.txt
    private static ArrayList<String> users = new ArrayList<>();  //list of users
    private static ArrayList<String> passes = new ArrayList<>();  //lis of passes

    public PasswordProtectedLogin() {
        loadUsersFromFile();
    }

    /**
     * Loads usernames and passwords from the "users.txt" file into the `users` and `passes` lists.
     * Each line in the file is split to extract the username and password.
     * Handles file I/O errors gracefully.
     */
    private void loadUsersFromFile() {

        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line;  //temp line of each line in users.txt
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                users.add(parts[0]);
                passes.add(parts[2]);
            }
        } catch (IOException e) {
            System.out.println("Error reading users from file: " + e.getMessage());
        }
    }

    /**
     * Retrieves the list of usernames.
     *
     * @return an ArrayList containing all usernames
     */
    public static ArrayList<String> getUsers() {
        return PasswordProtectedLogin.users;
    }

    /**
     * Retrieves the list of passwords.
     *
     * @return an ArrayList containing all passwords
     */
    public static ArrayList<String> getPasses() {
        return PasswordProtectedLogin.passes;
    }

    /**
     * Authenticates a user by checking the provided username and password against
     * the loaded user data.
     *
     * @param username the username to authenticate
     * @param password the password to authenticate
     * @return true if the username and password match an entry in the user data, false otherwise
     */
    public boolean authenticate(String username, String password) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).equals(username) && passes.get(i).equals(password)) {
                return true;
            }
        }
        return false;
    }
}