import java.util.*;

public interface User {

    String getUsername();
    void setUsername(String newUsername);

    ArrayList<UserProfile> getFriends();
    void setFriends(ArrayList<UserProfile> newFriends);

    String getEmail();
    void setEmail(String newEmail);

    String getPassword();
    void setPassword(String newPassword);

    String getDateJoined();
    void setDateJoined(String newDateJoined);

}
