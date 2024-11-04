import java.util.*;

public interface CreateNewUserInterface {
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
