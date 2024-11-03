import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class UserProfileTest {
    private UserProfile user1;
    private UserProfile user2;
    private UserProfile user3;

    @Before
    public void setUp() {
        //Creating user objects to use for test case
        user1 = new UserProfile("taylorswift246", "taylor.swift@gmail.com", "7685958484",
                "10/2/2024");
        user2 = new UserProfile("ryangosling", "ryan.gosling@gmail.com", "emmastone",
                "09/24/2006");
        user3 = new UserProfile("travisscott21", "travis.scott@gmail.com", "123456789",
                "03/30/2017");
    }

    @Test
    public void testAddFriend() {
        assertTrue(user1.addFriend("ryangosling")); //return true if the friend is added successfully
        //assertTrue(user1.addFriend("travisscott21"));
    }

    @Test
    public void testRemoveFriend() {
        if (!user1.getFriends().contains("ryangosling")) {
            user1.addFriend("ryangosling");
        } //checks if user2 is already a friend of user1 and if not, adds user2 to user1's friend list
        user1.removeFriend("ryangosling");
        assertFalse(user1.getFriends().contains("ryangosling")); // if user2 is still a friend in user1's list after being removed, return false

        if (!user1.getFriends().contains("travisscott21")) {
            user1.addFriend("travisscott21");
        } //checks if user3 is already a friend of user1 and if not, adds user3 to user1's friend list
        user1.removeFriend("travisscott21");
        assertFalse(user1.getFriends().contains("travisscott21")); // if user3 is still a friend in user1's list after being removed, return false
    }

    @Test
    public void testBlockUser() {
        user1.blockUser("travisscott21"); //block user3 from user1's profile
        assertFalse(user1.getFriends().contains("travisscott21")); //if user3 is still in user1's friends list, return false
        assertTrue(user1.getBlockedFriends().contains("travisscott21")); //if user3 is in user1's blocked friends list, return true

        assertFalse(user1.addFriend("travisscott21")); //returns false if able to add blocked user
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
    public void testFindUserByUsername_UserExists() {

        // Create users.txt file
        try (PrintWriter writer = new PrintWriter(new FileWriter("users.txt"))) {
            writer.println("johndoe,,,johndoe@example.com,password123,2024-11-01");
            writer.println("janedoe,,,janedoe@example.com,password456,2024-11-02");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test finding an existing user
        UserProfile result = UserSearch.findUserByUsername("johndoe");
        assertEquals("The username should match 'johndoe'.", "johndoe", result.getUsername());
        assertEquals("The email should match 'johndoe@example.com'.","johndoe@example.com", result.getEmail());
        assertEquals("The password should match 'password123'.","password123", result.getPassword());
        assertEquals("The dateJoined should match '2024-11-01'.","2024-11-01", result.getDateJoined());
    }

    @Test
    public void testFindUserByUsername_UserDoesNotExist() {

        // Create users.txt file
        try (PrintWriter writer = new PrintWriter(new FileWriter("users.txt"))) {
            writer.println("johndoe,,,johndoe@example.com,password123,2024-11-01");
            writer.println("janedoe,,,janedoe@example.com,password456,2024-11-02");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test finding a non-existing user
        UserProfile result = UserSearch.findUserByUsername("nonexistentuser");
        assertNull("The result should be null for a non-existent user.", result);
    }
}