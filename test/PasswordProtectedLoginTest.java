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
class PasswordProtectedLoginTest {

    private static final String filename = "users.txt";

    @BeforeEach
    void setUp() throws IOException {
        // Set up a temporary file with sample user data for testing
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("testUser:password123\n");
            writer.write("admin:adminPass\n");
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up by deleting the temporary file after each test
        Files.deleteIfExists(Paths.get(filename));
    }

    @Test
    void testLoadUsersFromFile() {
        //Clear the array list before intializing a new one
        PasswordProtectedLogin.getUsers().clear();

        // Initialize the login system, which should load users from the file
        PasswordProtectedLogin loginSystem = new PasswordProtectedLogin();

        // Check if users and passwords were loaded correctly
        assertEquals(2, PasswordProtectedLogin.getUsers().size(), "Two users should be loaded from file");
        assertEquals("testUser", PasswordProtectedLogin.getUsers().get(0), "First username should match");
        assertEquals("password123", PasswordProtectedLogin.getPasses().get(0), "First password should match");
    }

    @Test
    void testAuthenticateValidCredentials() {
        // Initialize the login system with users loaded from the file
        PasswordProtectedLogin loginSystem = new PasswordProtectedLogin();

        // Test valid login credentials
        assertTrue(loginSystem.authenticate("testUser", "password123"), "Valid credentials should authenticate successfully");
        assertTrue(loginSystem.authenticate("admin", "adminPass"), "Valid credentials should authenticate successfully");
    }

    @Test
    void testAuthenticateInvalidCredentials() {
        // Initialize the login system with users loaded from the file
        PasswordProtectedLogin loginSystem = new PasswordProtectedLogin();

        // Test invalid login credentials
        assertFalse(loginSystem.authenticate("testUser", "wrongPassword"), "Invalid password should not authenticate");
        assertFalse(loginSystem.authenticate("unknownUser", "password123"), "Unknown username should not authenticate");
    }
}