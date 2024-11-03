import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class UserProfile implements User {

    private String username;  //unique name associated with user
    private ArrayList<UserProfile> friends;  //list of friends of user
    private ArrayList<UserProfile> blockedFriends;  //list of blocked friends of user
    private String email;  //email associated with user
    private String password;  //password associated with user
    private String dateJoined;  //date the user created their account

    public UserProfile(String username, ArrayList<UserProfile> friends, ArrayList<UserProfile> blockedFriends, String email,
                       String password, String dateJoined) {

        this.username = username;
        this.friends = friends;
        this.blockedFriends = blockedFriends;
        this.email = email;
        this.password = password;
        this.dateJoined = dateJoined;

        saveToFile();

    }

    //Getters and Setters
    public String getUsername() {return this.username;}
    public void setUsername(String newUsername) {this.username = newUsername;}

    public ArrayList<UserProfile> getFriends() {return this.friends;}
    public void setFriends(ArrayList<UserProfile> newFriends) {this.friends = newFriends;}

    public ArrayList<UserProfile> getBlockedFriends() {return this.blockedFriends;}
    public void setBlockedFriends(ArrayList<UserProfile> newBlockedFriends) {this.blockedFriends = newBlockedFriends;}

    public String getEmail() {return this.email;}
    public void setEmail(String newEmail) {this.email = newEmail;}

    public String getPassword() {return this.password;}
    public void setPassword(String newPassword) {this.password = newPassword;}

    public String getDateJoined() {return this.dateJoined;}
    public void setDateJoined(String newDateJoined) {this.dateJoined = newDateJoined;}

    /**
     * Add Friend Method
     * @param userToAdd - the user account to add to the friend list.
     * @return True if the user passed has been successfully added, false if otherwise.
     */
    public boolean addFriend(UserProfile userToAdd) {

        for (UserProfile blockedFriend : blockedFriends) {
            if (userToAdd.equals(blockedFriend)) {
                return false;
            }
        }

        this.friends.add(userToAdd);
        return true;
    }

    /**
     * Remove Friend Method
     * @param userToRemove - the user account to add to the friend list.
     */
    public void removeFriend(UserProfile userToRemove) {
        this.friends.remove(userToRemove);
    }

    /**
     * Block User Method
     * @param userToBlock - the user account to add to the friend list
     * */
    public void blockUser(UserProfile userToBlock) {

        for (UserProfile friend : friends) {
            if (userToBlock.equals(friend)) {
                this.removeFriend(friend);
            }
        }
        this.blockedFriends.add(userToBlock);
    }

    /**
     * User File Formatting Method
     * @return a string that contains all the user account information to be saved
     */
    public String toFileFormat() {

        // Convert friends and blockedFriends to simple strings, e.g., just usernames
        StringBuilder friendsList = new StringBuilder();
        for (UserProfile friend : friends) {
            friendsList.append(friend.username).append(";");
        }

        StringBuilder blockedList = new StringBuilder();
        for (UserProfile blocked : blockedFriends) {
            blockedList.append(blocked.username).append(";");
        }

        return username + "," + friendsList.toString() + "," + blockedList.toString() + "," + email + "," + password + "," + dateJoined;
    }

    /**
     * Save To File Method - writes user account information to the users.txt file
     */
    private void saveToFile() {
        try (FileWriter fw = new FileWriter("users.txt", true); // 'true' for appending
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(this.toFileFormat());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
