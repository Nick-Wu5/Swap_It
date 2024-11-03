import java.io.*;
import java.util.*;

public class UserSearch {

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

                //Making sure every line is correctly formatted
                if (userDetails.length >= 6) {
                    String parsedUsername = userDetails[0];

                    //Get email, password, and dateJoined from data
                    String email = userDetails[3];
                    String password = userDetails[4];
                    String dateJoined = userDetails[5];

                    // Check if the parsed username matches the search criteria and return if so
                    if (parsedUsername.equals(username)) {
                        return new UserProfile(parsedUsername, email, password, dateJoined);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;  //return null if no user is found
    }

    /**
     * Search For Profile By Username Method
     *
     * @param userListString - list of users either of friends or blocked friends
     * @return an arraylist of the usernames passed
     */
    public static ArrayList<String> parseUserList(String userListString) {
        ArrayList<String> userList = new ArrayList<>();
        if (userListString != null && !userListString.isEmpty()) {
            String[] usernames = userListString.split(";");
            for (String username : usernames) {
                if (!username.isEmpty()) {
                    userList.add(username);
                }
            }
        }
        return userList;
    }
}
