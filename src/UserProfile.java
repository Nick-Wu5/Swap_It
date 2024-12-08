import java.io.*;
import java.util.ArrayList;

/**
 * UserProfile
 * <p>
 * Represents a user's profile in the social media application. A user profile contains
 * information such as the username, email, password, friends list, and blocked list.
 * It provides methods to manage friends, blocked users, and user-related data such as
 * posts and comments.
 *
 * @author Nick Wu, Chris Brantley, Ramya Prasanna, and Divya Vemireddy
 * @version December 7, 2024
 */
public class UserProfile implements User, Serializable {

    private String username;  //unique name associated with user
    private ArrayList<String> friends;  //list of friends of user
    private ArrayList<String> blockedFriends;  //list of blocked friends of user
    private String email;  //email associated with user
    private String password;  //password associated with user

    public UserProfile(String username, String email, String password) {

        this.username = username;
        this.friends = new ArrayList<String>();
        this.blockedFriends = new ArrayList<String>();
        this.email = email;
        this.password = password;

    }

    public UserProfile() {
    }

    /**
     * Retrieves the username of the user.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Updates the username of the user.
     *
     * @param newUsername the new username to set
     */
    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    /**
     * Retrieves the list of friends associated with the user by reading from the file.
     *
     * @return a list of friends for the user
     */
    public ArrayList<String> getFriends() {

        ArrayList<String> friendsList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails[0].equals(this.username)) {
                    if (userDetails[3].equals("EmptyFriendsList")) {
                        break;
                    } else if (!(userDetails[3].equals("EmptyFriendsList")) && !(userDetails[3].contains(";"))) {
                        friendsList.add(userDetails[3]);
                    } else {
                        String[] friendsListArray = userDetails[3].split(";");
                        for (String friend : friendsListArray) {
                            friendsList.add(friend);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (friendsList.isEmpty()) {
            friendsList.add("EmptyFriendsList");
        }
        return friendsList;
    }

    /**
     * Retrieves the list of friends stored in memory for the user.
     *
     * @return the friends list in memory
     */
    public ArrayList<String> getFriendsList() {
        return this.friends;
    }

    /**
     * Sets a new friends list for the user.
     *
     * @param newFriends the new list of friends
     */
    public void setFriends(ArrayList<String> newFriends) {
        this.friends = newFriends;
    }

    /**
     * Retrieves the list of blocked users stored in memory.
     *
     * @return the blocked friends list in memory
     */
    public ArrayList<String> getBlockedFriends() {
        return this.blockedFriends;
    }

    /**
     * Retrieves the list of blocked users associated with the user by reading from the file.
     *
     * @return a list of blocked friends for the user
     */
    public ArrayList<String> getBlockedFriendsFromFile() {

        ArrayList<String> blockedList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails[0].equals(this.username)) {
                    if (userDetails[4].equals("EmptyBlockedList")) {
                        break;
                    } else if (!(userDetails[4].equals("EmptyBlockedList")) && !(userDetails[4].contains(";"))) {
                        blockedList.add(userDetails[4]);
                    } else {
                        String[] blockedListArray = userDetails[4].split(";");
                        for (String blockedProfile : blockedListArray) {
                            blockedList.add(blockedProfile);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (blockedList.isEmpty()) {
            blockedList.add("EmptyBlockedList");
        }
        return blockedList;
    }

    /**
     * Sets a new blocked friends list for the user.
     *
     * @param newBlockedFriends the new list of blocked friends
     */
    public void setBlockedFriends(ArrayList<String> newBlockedFriends) {
        this.blockedFriends = newBlockedFriends;
    }

    /**
     * Retrieves the email of the user.
     *
     * @return the email address of the user
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Updates the email address of the user.
     *
     * @param newEmail the new email address to set
     */
    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    /**
     * Retrieves the password of the user.
     *
     * @return the password of the user
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Updates the password of the user.
     *
     * @param newPassword the new password to set
     */
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    /**
     * Add Friend Method
     *
     * @param userToAdd - the user account to add to the friend list.
     * @return True if the user passed has been successfully added, false if otherwise.
     */
    public boolean addFriend(String userToAdd) {

        for (String blockedFriend : this.blockedFriends) {
            if (userToAdd.equals(blockedFriend)) {
                return false;
            }
        }

        this.friends.add(userToAdd);

        if (this.friends.contains("EmptyFriendsList") && !(userToAdd.equals("EmptyFriendsList"))) {
            this.removeFriend("EmptyFriendsList");
        }

        return true;
    }

    /**
     * Remove Friend Method
     *
     * @param userToRemove - the user account to add to the friend list.
     */
    public void removeFriend(String userToRemove) {
        this.friends.remove(userToRemove);

        if (this.friends.size() == 0) {
            this.addFriend("EmptyFriendsList");
        }
    }

    /**
     * Block User Method
     *
     * @param userToBlock - the user account to add to the friend list
     */
    public void blockUser(String userToBlock) {

        for (String friend : this.getFriendsList()) {
            if (userToBlock.equals(friend)) {
                this.removeFriend(friend);
                this.updateFriendsList();
                break;
            }
        }

        this.blockedFriends.add(userToBlock);

        if (this.blockedFriends.contains("EmptyBlockedList") && !(userToBlock.equals("EmptyBlockedList"))) {
            this.removeBlockedUser("EmptyBlockedList");
        }
    }

    /**
     * Unblocks a user from the blocked friends list.
     *
     * @param userToRemoveBlock the username of the user to unblock
     */
    public void removeBlockedUser(String userToRemoveBlock) {

        this.blockedFriends.remove(userToRemoveBlock);

        if (this.blockedFriends.size() == 0) {
            this.blockUser("EmptyBlockedList");
        }
    }

    /**
     * User File Formatting Method
     *
     * @return a string that contains all the user account information to be saved
     */
    public String toFileFormat() {

        String friendsList;
        String blockedList;

        // Convert friends and blockedFriends to strings
        if (friends.isEmpty()) {
            friendsList = "EmptyFriendsList";
        } else {
            friendsList = String.join(";", this.friends);
        }

        if (blockedFriends.isEmpty()) {
            blockedList = "EmptyBlockedList";
        } else {
            blockedList = String.join(";", this.blockedFriends);
        }

        // Return the formatted string with commas separating the main fields
        return this.username + "," + this.email + "," + this.password + "," + friendsList + "," + blockedList;
    }

    /**
     * Save To File Method - writes user account information to the users.txt file
     */
    public synchronized void saveToFile() {
        //use file writer to
        try (FileWriter fw = new FileWriter("users.txt", true); // 'true' for appending
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(this.toFileFormat());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the user's account information as a formatted string.
     *
     * @return a StringBuilder containing the user's account information
     */
    public StringBuilder getAccountInfo() {

        StringBuilder accountInfo = new StringBuilder();
        accountInfo.append(this.getUsername()).append(";");
        accountInfo.append(this.getEmail()).append(";");
        accountInfo.append(this.getPassword()).append(";");
        accountInfo.append(this.getFriends()).append(";");
        accountInfo.append(this.getBlockedFriends()).append(";");
        return accountInfo;
    }

    /**
     * Retrieves the user's posts from the "newsPosts.txt" file.
     *
     * @return a list of NewsPost objects associated with the user
     */
    public ArrayList<NewsPost> getUserPosts() {

        String line;
        ArrayList<NewsPost> userPosts = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("newsPosts.txt"))) {
            while ((line = br.readLine()) != null) {

                String[] userDetails = line.split(",");

                if (userDetails[0].contains(this.getUsername())) {

                    NewsPost tempPost = new NewsPost();
                    tempPost.setAuthor(userDetails[0]);
                    tempPost.setCaption(userDetails[1]);
                    tempPost.setImagePath(userDetails[2]);
                    tempPost.setDate(userDetails[3]);
                    tempPost.setUpvotes(Integer.parseInt(userDetails[4]));
                    tempPost.setDownvotes(Integer.parseInt(userDetails[5]));
                    tempPost.setComments(NewsPost.findComments(userDetails[1]));

                    userPosts.add(tempPost);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return userPosts;
    }

    /**
     * Updates the user's friends list in the "users.txt" file.
     */
    public void updateFriendsList() {
        try {
            // Read the file into a list of strings
            ArrayList<String> fileLines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    fileLines.add(line);
                }
            }

            // Modify the relevant user's line
            boolean userFound = false;
            for (int i = 0; i < fileLines.size(); i++) {
                String[] parts = fileLines.get(i).split(",");
                if (parts[0].equals(username)) {

                    parts[3] = String.join(";", this.getFriendsList()); // Replace with the new friends list

                    fileLines.set(i, String.join(",", parts)); // Rebuild the line
                    userFound = true;
                    break;
                }
            }

            if (!userFound) {
                System.out.println("User not found: " + username);
                return;
            }

            // Write back the updated lines to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"))) {
                for (String line : fileLines) {
                    writer.write(line);
                    writer.newLine();
                }
            }

            System.out.println("Friends list updated successfully for user: " + this.getUsername());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the user's blocked friends list in the "users.txt" file.
     */
    public void updateBlockedList() {
        try {
            // Read the file into a list of strings
            ArrayList<String> fileLines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    fileLines.add(line);
                }
            }

            // Modify the relevant user's line
            boolean userFound = false;
            for (int i = 0; i < fileLines.size(); i++) {
                String[] parts = fileLines.get(i).split(",");
                if (parts[0].equals(username)) {

                    parts[4] = String.join(";", this.getBlockedFriends()); // Replace with the new friends list

                    fileLines.set(i, String.join(",", parts)); // Rebuild the line
                    userFound = true;
                    break;
                }

            }

            if (!userFound) {
                System.out.println("User not found: " + username);
                return;
            }

            // Write back the updated lines to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"))) {
                for (String line : fileLines) {
                    writer.write(line);
                    writer.newLine();
                }
            }

            System.out.println("Blocked list updated successfully for user: " + this.getUsername());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds and retrieves all comments associated with the user from the "newsComments.txt" file.
     *
     * @return a list of NewsComment objects related to the user
     */
    public ArrayList<NewsComment> findCommentsForUser() {

        ArrayList<NewsComment> comments = new ArrayList<>();

        try (BufferedReader readComments = new BufferedReader(new FileReader("newsComments.txt"))) {
            String line;

            while ((line = readComments.readLine()) != null) {
                if (line.split(",")[1].contains(this.getUsername())) {

                    String[] commentInfo = line.split(",");

                    comments.add(new NewsComment(commentInfo[0], commentInfo[1], commentInfo[2],
                            Integer.parseInt(commentInfo[3]), Integer.parseInt(commentInfo[4])));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return comments;
    }
}