import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class CreateNewUserTest {

    @BeforeEach
    void setUp() {
        // Clear the file and userProfiles list before each test to ensure no data carries over
        CreateNewUser.userProfiles.clear();
        try {
            Files.write(Paths.get("users.txt"), new byte[0]); // Clear users.txt file
        } catch (IOException e) {
            System.out.println("Setup error: " + e.getMessage());
        }
    }

    @Test
    void testCreateNewUserNotAlreadyRegistered() {
        // Test creating a user that doesn't already exist
        CreateNewUser newUser = new CreateNewUser("newUser", "password123");
        assertFalse(newUser.isAlreadyRegistered(), "User should not be registered initially");

        // Check if the user profile was created
        UserProfile profile = newUser.getUserProfile();
        assertNotNull(profile, "User profile should be created");
        assertEquals("newUser", profile.getUsername(), "Username should match");
    }

    @Test
    void testDuplicateUserRegistration() {
        // Create the first user
        new CreateNewUser("duplicateUser", "password123");

        // Attempt to create the same user again
        CreateNewUser duplicateUser = new CreateNewUser("duplicateUser", "anotherPassword");
        assertTrue(duplicateUser.isAlreadyRegistered(), "User should be marked as already registered");
        assertNull(duplicateUser.userProfiles, "No profile should be created for duplicate user");
    }

    @Test
    void testPersistenceWithSaveAndLoad() {
        // Create and save a user
        new CreateNewUser("persistentUser", "persistentPass");

        // Clear the in-memory list and reload from file
        CreateNewUser.userProfiles.clear();
        CreateNewUser.loadUsersFromFile();

        // Check if the user was loaded correctly
        assertEquals(1, CreateNewUser.userProfiles.size(), "One user should be loaded from file");
        UserProfile loadedUser = CreateNewUser.userProfiles.get(0);
        assertEquals("persistentUser", loadedUser.getUsername(), "Loaded username should match");
    }
}