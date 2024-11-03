import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class UserProfile implements User {

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

        saveToFile();

    }

    public UserProfile() {}

    //Getters and Setters
    public String getUsername() {return this.username;}
    public void setUsername(String newUsername) {this.username = newUsername;}

    public ArrayList<String> getFriends() {return this.friends;}
    public void setFriends(ArrayList<String> newFriends) {this.friends = newFriends;}

    public ArrayList<String> getBlockedFriends() {return this.blockedFriends;}
    public void setBlockedFriends(ArrayList<String> newBlockedFriends) {this.blockedFriends = newBlockedFriends;}

    public String getEmail() {return this.email;}
    public void setEmail(String newEmail) {this.email = newEmail;}

    public String getPassword() {return this.password;}
    public void setPassword(String newPassword) {this.password = newPassword;}


    /**
     * Add Friend Method
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
     * @param userToRemove - the user account to add to the friend list.
     */
    public void removeFriend(String userToRemove) {
        this.friends.remove(userToRemove);
    }

    /**
     * Block User Method
     * @param userToBlock - the user account to add to the friend list
     * */
    public void blockUser(String userToBlock) {

        for (String friend : this.friends) {
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

        // Convert friends and blockedFriends to strings
        String friendsList = String.join(";", this.friends);
        String blockedList = String.join(";", this.blockedFriends);

        // Return the formatted string with commas separating the main fields
        return this.username + "," + friendsList + "," + blockedList + "," + this.email + "," +
                this.password;
    }

    /**
     * Save To File Method - writes user account information to the users.txt file
     */
    public void saveToFile() {
        //use file writer to
        try (FileWriter fw = new FileWriter("users.txt", true); // 'true' for appending
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(this.toFileFormat());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
