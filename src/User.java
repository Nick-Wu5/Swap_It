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
public interface User {

    String getUsername();

    void setUsername(String newUsername);

    ArrayList<String> getFriends();

    void setFriends(ArrayList<String> newFriends);

    String getEmail();

    void setEmail(String newEmail);

    String getPassword();

    void setPassword(String newPassword);

    boolean addFriend(String userToAdd);

    void removeFriend(String userToRemove);

    void blockUser(String userToBlock);

    String toFileFormat();

    void saveToFile();

    ArrayList<NewsPost> getUserPosts();

}