import java.io.*;
import java.util.*;

public class UserSearch implements Search {

    /**
     * Search For Profile By Username Method
     *
     * @param username - account username that someone is looking for
     * @return a UserProfile object is that username is in users.txt, null if otherwise
     */
    public UserProfile findUserByUsername(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userDetails = line.split(",");

                //Making sure every line is correctly formatted
                if (userDetails.length >= 6) {
                    String parsedUsername = userDetails[0];

                    // Check if the parsed username matches the search criteria
                    if (parsedUsername.equals(username)) {
                        return getSearchedUser(userDetails, parsedUsername);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;  //return null if no user is found
    }

    /**
     *
     * @param userDetails - a string containing the information of the user from the users.txt file
     * @param parsedUsername - username of searched user
     * @return the UserProfile of the searched user if their username exists in database
     */
    private static UserProfile getSearchedUser(String[] userDetails, String parsedUsername) {

        //Get email, password, and dateJoined from data
        ArrayList<String> friends = new ArrayList<>(Arrays.asList(userDetails[1].split(";")));
        ArrayList<String> blockedFriends = new ArrayList<>(Arrays.asList(userDetails[2].split(";")));
        String email = userDetails[3];
        String password = userDetails[4];
        String dateJoined = userDetails[5];

        // Avoid using the constructor that saves to a file by using a different approach
        UserProfile searchedUser = new UserProfile();  // Use a no-arg constructor
        searchedUser.setUsername(parsedUsername);
        searchedUser.setEmail(email);
        searchedUser.setPassword(password);
        searchedUser.setDateJoined(dateJoined);
        searchedUser.setFriends(friends);
        searchedUser.setBlockedFriends(blockedFriends);
        return searchedUser;
    }
}
