import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.io.Serializable;

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

    //Getters and Setters
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    public ArrayList<String> getFriends() {
        return this.friends;
    }

    public void setFriends(ArrayList<String> newFriends) {
        this.friends = newFriends;
    }

    public ArrayList<String> getBlockedFriends() {
        return this.blockedFriends;
    }

    public void setBlockedFriends(ArrayList<String> newBlockedFriends) {
        this.blockedFriends = newBlockedFriends;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public String getPassword() {
        return this.password;
    }

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
        return true;
    }

    /**
     * Remove Friend Method
     *
     * @param userToRemove - the user account to add to the friend list.
     */
    public void removeFriend(String userToRemove) {
        this.friends.remove(userToRemove);
    }

    /**
     * Block User Method
     *
     * @param userToBlock - the user account to add to the friend list
     */
    public void blockUser(String userToBlock) {

        for (String friend : this.friends) {
            if (userToBlock.equals(friend)) {
                this.removeFriend(friend);
                break;
            }
        }
        this.blockedFriends.add(userToBlock);
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
}
