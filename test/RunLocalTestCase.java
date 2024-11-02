import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class RunLocalTestCase {
    private UserProfile user1;
    private UserProfile user2;
    private UserProfile user3;

    @Before
    public void setUp() {
        //Creating user objects to use for test case
        user1 = new UserProfile("ramyap686", new ArrayList<UserProfile>(), new ArrayList<UserProfile>(),
                "ramya.prasanna06@gmail.com", "7685958484","10/2/2024");
        user2 = new UserProfile("ryangosling", new ArrayList<UserProfile>(), new ArrayList<UserProfile>(),
                "ryan.gosling@gmail.com", "emmastone","09/24/2006");
        user3 = new UserProfile("travisscott21", new ArrayList<UserProfile>(), new ArrayList<UserProfile>(),
                "travis.scott@gmail.com", "123456789","03/30/2017");


        // Block user3 for user1
        user1.blockUser(user3);
    }

    @Test
    public void testAddFriend() {
        assertTrue(user1.addFriend(user2)); //return true if the friend is added successfully
        assertTrue(user1.addFriend(user3));
    }

    @Test
    public void testRemoveFriend() {
        if (!user1.getFriends().contains(user2)) {
            user1.addFriend(user2);
        } //checks if user2 is already a friend of user1 and if not, adds user2 to user1's friend list
        user1.removeFriend(user2);
        assertFalse(user1.getFriends().contains(user2)); // if user2 is still a friend in user1's list after being removed, return false

        if (!user1.getFriends().contains(user3)) {
            user1.addFriend(user3);
        } //checks if user3 is already a friend of user1 and if not, adds user3 to user1's friend list
        user1.removeFriend(user3);
        assertFalse(user1.getFriends().contains(user3)); // if user3 is still a friend in user1's list after being removed, return false
    }

    @Test
    public void testBlockUser() {
        user1.blockUser(user3); //block user3 from user1's profile
        assertFalse(user1.getFriends().contains(user3)); //if user3 is still in user1's friends list, return false
        assertTrue(user1.getBlockedFriends().contains(user3)); //if user3 is in user1's blocked friends list, return true

        assertFalse(user1.addFriend(user3)); //returns false if able to add blocked user
    }
}