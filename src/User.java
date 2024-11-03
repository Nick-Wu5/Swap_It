import java.util.*;

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
}
