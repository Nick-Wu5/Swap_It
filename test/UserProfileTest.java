import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
public class UserProfileTest {

    private UserProfile user1;
    private UserProfile user2;
    private UserProfile user3;

    @Before
    public void setUp() {
        //Creating user objects to use for test case
        user1 = new UserProfile("taylorswift246", "taylor.swift@gmail.com", "7685958484");
        user2 = new UserProfile("ryangosling", "ryan.gosling@gmail.com", "emmastone");
        user3 = new UserProfile("travisscott21", "travis.scott@gmail.com", "123456789");
    }

    @Test
    public void testAddFriend() {
        assertTrue(user1.addFriend("ryangosling")); //return true if the friend is added successfully
        //assertTrue(user1.addFriend("travisscott21"));
    }

    @Test
    public void testRemoveFriend() {
        //checks if user2 is already a friend of user1 and if not, adds user2 to user1's friend list
        if (!user1.getFriends().contains("ryangosling")) {
            user1.addFriend("ryangosling");
        }
        user1.removeFriend("ryangosling");
        // if user2 is still a friend in user1's list after being removed, return false
        assertFalse(user1.getFriends().contains("ryangosling"));

        //checks if user3 is already a friend of user1 and if not, adds user3 to user1's friend list
        if (!user1.getFriends().contains("travisscott21")) {
            user1.addFriend("travisscott21");
        }
        user1.removeFriend("travisscott21");
        // if user3 is still a friend in user1's list after being removed, return false
        assertFalse(user1.getFriends().contains("travisscott21"));
    }

    @Test
    public void testBlockUser() {
        //block user3 from user1's profile
        user1.blockUser("travisscott21");
        //if user3 is still in user1's friends list, return false
        assertFalse(user1.getFriends().contains("travisscott21"));
        //if user3 is in user1's blocked friends list, return true
        assertTrue(user1.getBlockedFriends().contains("travisscott21"));
        //returns false if able to add blocked user
        assertFalse(user1.addFriend("travisscott21"));
    }

    @Test
    public void testToFileFormat() {
        // Test the toFileFormat method for user1
        String expected1 = "taylorswift246,,,taylor.swift@gmail.com,7685958484,10/2/2024";
        assertEquals(expected1, user1.toFileFormat());

        // Test the toFileFormat method for user2
        String expected2 = "ryangosling,,,ryan.gosling@gmail.com,emmastone,09/24/2006";
        assertEquals(expected2, user2.toFileFormat());

        // Test the toFileFormat method for user3
        String expected3 = "travisscott21,,,travis.scott@gmail.com,123456789,03/30/2017";
        assertEquals(expected3, user3.toFileFormat());

    }

    @Test
    public void testPrintUserInfo() {
        // when user initially has 0 friends
        String expectedInfo = "Username: taylorswift246\nFriends: 0\n";
        assertEquals(expectedInfo, user1.printUserInfo());
        // add a friend and check again
        user1.addFriend("ryangosling");
        expectedInfo ="Username: taylorswift246\nFriends: 1\n";
        assertEquals(expectedInfo, user1.printUserInfo());
        // add another friend to user1 and check the output
        user1.addFriend("travisscott21");
        expectedInfo ="Username: taylorswift246\nFriends: 2\n";
        assertEquals(expectedInfo, user1.printUserInfo());
    }
}
