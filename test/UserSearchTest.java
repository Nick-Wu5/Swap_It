import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Team Project - Social Media App
 * <p>
 * This program provides a social networking system that allows users to create password-protected accounts and log in
 * securely. It includes features for searching and viewing other user profiles, as well as options to add, block, or
 * remove friends. The system also supports account and relationship management for an interactive user experience.
 *
 * @version November 3, 2024
 * @authors Nick Wu, Chris Brantley, Ramya Prasanna, and Divya Vemireddy
 */
public class UserSearchTest {

    @Before
    public void setUp() {

        // Create the users.txt file with mock data for testing
        try (PrintWriter writer = new PrintWriter(new FileWriter("users.txt"))) {
            writer.println("johndoe,,,johndoe@example.com,password123");
            writer.println("janedoe,;friend1;friend2;,;blocked1;,janedoe@example.com,password456");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindUserByUsername_UserExists() {
        // Test for a user that exists
        UserProfile result = UserSearch.findUserByUsername("johndoe");

        assertNotNull("The result should not be null for an existing user.", result);
        assertEquals("The username should match 'johndoe'.", "johndoe", result.getUsername());
        assertEquals("The email should match 'johndoe@example.com'.", "johndoe@example.com", result.getEmail());
        assertEquals("The password should match 'password123'.", "password123", result.getPassword());
    }

    @Test
    public void testFindUserByUsername_UserDoesNotExist() {
        // Test for a user that does not exist
        UserProfile result = UserSearch.findUserByUsername("nonexistentuser");

        assertNull("The result should be null for a non-existent user.", result);
    }

    @Test
    public void testFindUserByUsername_UserWithFriendsAndBlocked() {
        // Test for a user with friends and blocked users
        UserProfile result = UserSearch.findUserByUsername("janedoe");

        assertNotNull("The result should not be null for an existing user.", result);
        assertEquals("The username should match 'janedoe'.", "janedoe", result.getUsername());
        assertEquals("The email should match 'janedoe@example.com'.", "janedoe@example.com", result.getEmail());
        assertEquals("The password should match 'password456'.", "password456", result.getPassword());
        assertTrue("The friends list should contain 'friend1'.", result.getFriends().contains("friend1"));
        assertTrue("The friends list should contain 'friend2'.", result.getFriends().contains("friend2"));
        assertTrue("The blocked friends list should contain 'blocked1'.", result.getBlockedFriends().contains("blocked1"));
    }

    @Test
    public void testGetSearchedUser() {
        // Mock user details
        String[] userDetails = {"janedoe", "friend1;friend2", "blocked1", "janedoe@example.com", "password456"};
        String parsedUsername = "janedoe";

        // Call getSearchedUser method
        UserProfile result = UserSearch.getSearchedUser(userDetails, parsedUsername);

        assertNotNull("The result should not be null for valid user details.", result);
        assertEquals("The username should match 'janedoe'.", "janedoe", result.getUsername());
        assertEquals("The email should match 'janedoe@example.com'.", "janedoe@example.com", result.getEmail());
        assertEquals("The password should match 'password456'.", "password456", result.getPassword());
        assertEquals("The friends list should contain 'friend1' and 'friend2'.", new ArrayList<>(Arrays.asList("friend1", "friend2")), result.getFriends());
        assertEquals("The blocked friends list should contain 'blocked1'.", new ArrayList<>(Arrays.asList("blocked1")), result.getBlockedFriends());
    }
}