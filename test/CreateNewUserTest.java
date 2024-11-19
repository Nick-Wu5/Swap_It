import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

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
class CreateNewUserTest {

    @BeforeEach
    void setUp() {
        // Clear the file and userProfiles list before each test to ensure no data carries over
        CreateNewUser.loadUsersFromFile();
        CreateNewUser.getUserProfiles().clear();
        try {
            Files.write(Paths.get("users.txt"), new byte[0]); // Clear users.txt file
        } catch (IOException e) {
            System.out.println("Setup error: " + e.getMessage());
        }
    }

    @Test
    void testCreateNewUserNotAlreadyRegistered() {
        // Test creating a user that doesn't already exist
        CreateNewUser newUser = new CreateNewUser("newUser", "newUserEmail@example.com", "password123");
        assertFalse(newUser.isAlreadyRegistered(), "User should not be registered initially");

        // Check if the user profile was created
        UserProfile profile = newUser.getUser();
        assertNotNull(profile, "User profile should be created");
        assertEquals("newUser", profile.getUsername(), "Username should match");
    }

    @Test
    void testDuplicateUserRegistration() {
        new CreateNewUser("duplicateUser", "duplicateEmail@example.com", "password123");

        // Attempt to create the same user again
        CreateNewUser duplicateUser = new CreateNewUser("duplicateUser", "anotherEmail@example.com", "anotherPassword");
        assertTrue(duplicateUser.isAlreadyRegistered(), "User should be marked as already registered");

        // Ensure no profile was created for the duplicate user
        assertNull(duplicateUser.getUser(), "No profile should be created for duplicate user");
    }

    @Test
    void testPersistenceWithSaveAndLoad() {
        try {
            Files.write(Paths.get("users.txt"), new byte[0]); // Clear users.txt file
        } catch (IOException e) {
            fail("Error clearing users file before test: " + e.getMessage());
        }

        // Create and save a user
        new CreateNewUser("persistentUser", "persistentEmail@example.com", "persistentPass");

        // Clear the in-memory list and reload from file
        CreateNewUser.getUserProfiles().clear();
        CreateNewUser.loadUsersFromFile();

        // Check if the user was loaded correctly
        assertEquals(1, CreateNewUser.getUserProfiles().size(), "One user should be loaded from file");
        if (!CreateNewUser.getUserProfiles().isEmpty()) {
            UserProfile loadedUser = CreateNewUser.getUserProfiles().get(0);
            assertEquals("persistentUser", loadedUser.getUsername(), "Loaded username should match");
            assertEquals("persistentEmail@example.com", loadedUser.getEmail(), "Loaded email should match");
        }
    }
}
//push this