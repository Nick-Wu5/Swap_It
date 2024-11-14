import java.io.*;
import java.util.*;

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
public class UserSearch implements Search {

    /**
     * Search For Profile By Username Method
     *
     * @param username - account username that someone is looking for
     * @return a UserProfile object is that username is in users.txt, null if otherwise
     */
    public static UserProfile findUserByUsername(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userDetails = line.split(",");
                String parsedUsername = userDetails[0];

                // Check if the parsed username matches the search criteria
                if (parsedUsername.equals(username)) {
                    System.out.println("Found user: " + username);
                    return getSearchedUser(userDetails, parsedUsername);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("couldn't find user: " + username);
        return null;  //return null if no user is found
    }

    /**
     * @param userDetails    - a string containing the information of the user from the users.txt file
     * @param parsedUsername - username of searched user
     * @return the UserProfile of the searched user if their username exists in database
     */
    public static UserProfile getSearchedUser(String[] userDetails, String parsedUsername) {

        //Get email, password, and friends from data
        String email = userDetails[1];
        String password = userDetails[2];
        ArrayList<String> friends = new ArrayList<>(Arrays.asList(userDetails[3].split(";")));
        ArrayList<String> blockedFriends = new ArrayList<>(Arrays.asList(userDetails[4].split(";")));

        // Avoid using the constructor that saves to a file by using a different approach
        UserProfile searchedUser = new UserProfile();  // Use a no-arg constructor
        searchedUser.setUsername(parsedUsername);
        searchedUser.setEmail(email);
        searchedUser.setPassword(password);
        searchedUser.setFriends(friends);
        searchedUser.setBlockedFriends(blockedFriends);
        return searchedUser;
    }
}
