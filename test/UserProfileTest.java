import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.*;

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
    public void testSaveToFile() {
        user1.saveToFile();

        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("taylorswift246")) {
                    found = true;
                    assertEquals(line, user1.toFileFormat());
                    break;
                }
            }
            assertTrue("User should be saved to file", found);
        } catch (IOException e) {
            fail("Exception should not occur during file reading: " + e.getMessage());
        }
    }

    @Test
    public void testGetUserPosts() {
        // Simulate the existence of a file with posts
        try (PrintWriter pw = new PrintWriter(new FileWriter("newsPosts.txt"))) {
            pw.println("taylorswift246,Hello World!,path/to/image,2024-11-01,10,2");
            pw.println("ryangosling,Good Morning,path/to/image2,2024-11-02,5,1");
        } catch (IOException e) {
            fail("Exception should not occur while setting up the test: " + e.getMessage());
        }

        ArrayList<NewsPost> posts = user1.getUserPosts();

        assertEquals(1, posts.size());
        assertEquals("Hello World!", posts.get(0).getCaption());
        assertEquals(10, posts.get(0).getUpvotes());
        assertEquals(2, posts.get(0).getDownvotes());
    }

    @Test
    public void testUpdateFriendsList() {
        user1.addFriend("ryangosling");
        user1.saveToFile();

        user1.setFriends(new ArrayList<>(Arrays.asList("travisscott21")));
        user1.updateFriendsList();

        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            boolean updated = false;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("taylorswift246")) {
                    assertTrue(line.contains("travisscott21"));
                    assertFalse(line.contains("ryangosling"));
                    updated = true;
                    break;
                }
            }
            assertTrue("Friends list should be updated in file", updated);
        } catch (IOException e) {
            fail("Exception should not occur during file reading: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateBlockedList() {
        user1.blockUser("ryangosling");
        user1.saveToFile();

        user1.setBlockedFriends(new ArrayList<>(Arrays.asList("travisscott21")));
        user1.updateBlockedList();

        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            boolean updated = false;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("taylorswift246")) {
                    assertTrue(line.contains("travisscott21"));
                    assertFalse(line.contains("ryangosling"));
                    updated = true;
                    break;
                }
            }
            assertTrue("Blocked list should be updated in file", updated);
        } catch (IOException e) {
            fail("Exception should not occur during file reading: " + e.getMessage());
        }
    }

}
