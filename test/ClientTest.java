import org.junit.jupiter.api.*;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {

    private String input;
    private InputStream inputStream;
    private PrintStream originalOut;
    private ByteArrayOutputStream outputStream;
    private PrintStream newOut;

    @BeforeEach
    public void setUp() {
        // Initialize the fields before each test
        this.input = "";
        this.inputStream = new ByteArrayInputStream(input.getBytes());  // InputStream for user input
        this.originalOut = System.out;  // Save original System.out
        this.outputStream = new ByteArrayOutputStream();  // Capture the output
        this.newOut = new PrintStream(outputStream);  // New PrintStream to redirect System.out
    }

    @AfterEach
    public void tearDown() {
        // Reset System.out to its original state after each test
        System.setOut(originalOut);
        System.setIn(System.in); // Reset System.in to its original state
    }

    @Test
    public void testClientRegistration() throws Exception {
        // Simulate user input for registration
        this.input = "2\n" // Choose Register option
                + "purdue pete\n" // Username
                + "purduepete@outlook.com\n" // Email
                + "boilerup123\n"; // Password

        // Update inputStream with the new input
        this.inputStream = new ByteArrayInputStream(input.getBytes());

        // Redirect System.in to simulate user input
        System.setIn(inputStream);
        // Redirect System.out to capture the program's output
        System.setOut(newOut);

        // Expected output from the program
        String expectedOutput = "\nPlease Specify An Authentication Method"
                + "\n1 : Login\n2 : Register"
                + "\nEnter a username:"
                + "\nEnter an email:"
                + "\nEnter a password:"
                + "\nRegistration complete.";

        // Run the main method of the Client class
        Client.main(new String[0]);

        // Capture the actual output from the program
        String actualOutput = outputStream.toString();

        // Assert that the output matches the expected output
        assertEquals(expectedOutput, actualOutput, "Test failed: Output did not match.");
    }

    @Test
    public void testClientLogin() throws Exception {
        // Simulate user input for login
        this.input = "1\n" // Choose Login option
                + "purdue pete\n" // Username
                + "boilerup123\n"; // Password

        // Update inputStream with the new input
        this.inputStream = new ByteArrayInputStream(input.getBytes());

        // Redirect System.in to simulate user input
        System.setIn(inputStream);
        // Redirect System.out to capture the program's output
        System.setOut(newOut);

        // Expected output from the program
        String expectedOutput = "\nPlease Specify An Authentication Method"
                + "\n1 : Login\n2 : Register"
                + "\nEnter your username:"
                + "\nEnter your password:"
                + "\nLogin Successful";

        // Run the main method of the Client class
        Client.main(new String[0]);

        // Capture the actual output from the program
        String actualOutput = outputStream.toString();

        // Assert that the output matches the expected output
        assertEquals(expectedOutput, actualOutput, "Test failed: Output did not match.");
    }

    @Test
    public void testInvalidOptionInMainMenu() throws Exception {
        // Simulate user input for an invalid option in the main menu
        this.input = "5\n"; // Invalid option

        // Update inputStream with the new input
        this.inputStream = new ByteArrayInputStream(input.getBytes());

        // Redirect System.in to simulate user input
        System.setIn(inputStream);
        // Redirect System.out to capture the program's output
        System.setOut(newOut);

        // Expected output from the program
        String expectedOutput = "\n>>> Main Menu : Please Enter A Number <<<"
                + "\n1 : Search\n2 : Post\n3 : Friends\n4 : Account\n5 : Exit\n"
                + "Invalid option. Try again.";

        // Run the main method of the Client class
        Client.main(new String[0]);

        // Capture the actual output from the program
        String actualOutput = outputStream.toString();

        // Assert that the output matches the expected output
        assertEquals(expectedOutput, actualOutput, "Test failed: Output did not match.");
    }
}