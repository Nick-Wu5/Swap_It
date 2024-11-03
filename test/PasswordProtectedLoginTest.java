import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

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
        // Initialize the login system, which should load users from the file
        PasswordProtectedLogin loginSystem = new PasswordProtectedLogin();

        // Check if users and passwords were loaded correctly
        assertEquals(2, PasswordProtectedLogin.users.size(), "Two users should be loaded from file");
        assertEquals("testUser", PasswordProtectedLogin.users.get(0), "First username should match");
        assertEquals("password123", PasswordProtectedLogin.passes.get(0), "First password should match");
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