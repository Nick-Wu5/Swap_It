import java.util.*;

public class UserProfile implements User {

    private String username;  //unique name associated with user
    private ArrayList<String> friends;  //list of friends of user
    private ArrayList<String> blockedFriends;  //list of blocked friends of user
    private String email;  //email associated with user
    private String password;  //password associated with user
    private String dateJoined;  //date the user created their account

    public UserProfile(String username, ArrayList<String> friends, ArrayList<String> blockedFriends, String email, String password, String dateJoined) {

        this.username = username;
        this.friends = friends;
        this.blockedFriends = blockedFriends;
        this.email = email;
        this.password = password;
        this.dateJoined = dateJoined;
    }

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

    public String getDateJoined() {return this.dateJoined;}
    public void setDateJoined(String newDateJoined) {this.dateJoined = newDateJoined;}

    public static void main(String[] args) {

        System.out.println("seems to be working");

    }

}
