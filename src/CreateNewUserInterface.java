import java.util.*;

/**
 * CreateNewUserInterface
 * <p>
 * Defines the basic operations for managing the creation of new user profiles
 * in the social media application. This includes checking for duplicate usernames,
 * retrieving user profiles, and loading profiles from a file.
 *
 * @version December 7, 2024
 * @author Nick Wu, Chris Brantley, Ramya Prasanna, and Divya Vemireddy
 */
public interface CreateNewUserInterface {

    ArrayList<String> getFriends();

    void setFriends(ArrayList<String> friends);

    ArrayList<String> getBlockedFriends();

    void setBlockedFriends(ArrayList<String> blockedFriends);

    String getEmail();

    void setEmail(String email);

    String toFileFormat();

    void saveToFile();
}