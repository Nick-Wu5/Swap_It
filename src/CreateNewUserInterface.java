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
public interface CreateNewUserInterface {

    //Getters and setters
    String getUsername();

    void setUsername(String username);

    ArrayList<String> getFriends();

    void setFriends(ArrayList<String> friends);

    String getPassword();

    void setPassword(String password);

    ArrayList<String> getBlockedFriends();

    void setBlockedFriends(ArrayList<String> blockedFriends);

    String getEmail();

    void setEmail(String email);

    boolean addFriend();

    void removeFriend(String userToRemove);

    void blockUser(String userToBlock);

    void unblockUser(String userToUnblock);

    String toFileFormat();

    void saveToFile();
}
