import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class UserSearchTest {

    @Before
    public void setUp() {

        // Create users.txt file
        try (PrintWriter writer = new PrintWriter(new FileWriter("users.txt"))) {
            writer.println("johndoe,,,johndoe@example.com,password123");
            writer.println("janedoe,,,janedoe@example.com,password456");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindUserByUsername_UserExists() {

        // Test finding an existing user
        UserProfile result = UserSearch.findUserByUsername("johndoe");
        assertEquals("The username should match 'johndoe'.", "johndoe", result.getUsername());
        assertEquals("The email should match 'johndoe@example.com'.","johndoe@example.com", result.getEmail());
        assertEquals("The password should match 'password123'.","password123", result.getPassword());
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