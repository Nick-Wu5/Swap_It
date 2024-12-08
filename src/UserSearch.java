import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * UserSearch
 * <p>
 * Provides functionality to search for user profiles in the social media application.
 * This class includes methods to find a user by their username and retrieve detailed
 * information about the searched user from the database.
 *
 * @author Nick Wu, Chris Brantley, Ramya Prasanna, and Divya Vemireddy
 * @version December 7, 2024
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
        ArrayList<String> friends;
        ArrayList<String> blockedFriends;

        if (!userDetails[3].equals(";")) {
            friends = new ArrayList<>(Arrays.asList(userDetails[3].split(";")));
        } else {
            friends = new ArrayList<>();
        }

        if (!userDetails[4].equals(";")) {
            blockedFriends = new ArrayList<>(Arrays.asList(userDetails[4].split(";")));
        } else {
            blockedFriends = new ArrayList<>();
        }


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