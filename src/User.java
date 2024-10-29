import java.util.*;

public interface User {

    String getUsername();
    void setUsername(String newUsername);

    ArrayList<String> getFriends();
    void setFriends(ArrayList<String> newFriends);

    ArrayList<String> getBlockedFriends();
    void setBlockedFriends(ArrayList<String> newBlockedFriends);

    String getEmail();
    void setEmail(String newEmail);

    String getPassword();
    void setPassword(String newPassword);

    String getDateJoined();
    void setDateJoined(String newDateJoined);

}
