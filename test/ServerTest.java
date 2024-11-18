import org.junit.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Timeout;

import java.io.*;
import java.net.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServerTest {

    private static final int PORT = 1234;
    private Thread serverThread;

    @BeforeEach
    public void setUp() {
        // Start the server in a separate thread
        serverThread = new Thread(() -> {
            try {
                Server.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        serverThread.start();

        // Give the server some time to start
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @AfterEach
    public void tearDown() {
        serverThread.interrupt(); // Stop the server thread
    }

    @Test
    @Timeout(1)
    public void testServerConnection() {
        String input = ""; // No specific input required for server connection verification
        String expected = "Server: Client connected\n";

        // Redirect System.out to capture the output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try (Socket clientSocket = new Socket("localhost", PORT)) {
            // Wait briefly for the server to print the connection message
            Thread.sleep(100);

            // Capture the output
            String stuOut = outContent.toString().replace("\r\n", "\n").trim();
            expected = expected.trim();

            assertEquals("Expected the server to confirm client connection", expected, stuOut);

        } catch (IOException | InterruptedException e) {
            fail("Exception during client-server communication: " + e.getMessage());
        } finally {
            // Restore System.out
            System.setOut(originalOut);
        }
    }

    @Test
    @Timeout(1)
    public void testInvalidPortHandling() {
        // Redirect System.err to capture the output
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errContent));

        try {
            // Attempt to create a server on an invalid port (-1)
            new ServerSocket(-1);
            fail("Expected an exception for invalid port number");

        } catch (IOException e) {
            String errorOutput = errContent.toString().replace("\r\n", "\n").trim();
            assertTrue(errorOutput.contains("java.net.BindException"),
                    "Expected a BindException for invalid port number");
        } finally {
            // Restore System.err
            System.setErr(originalErr);
        }
    }

    @Test
    @Timeout(1)
    public void testInvalidSocketHandling() {
        try {
            // Test the Server constructor with a null socket
            Server server = new Server(null);
            fail("Expected NullPointerException for null socket");

        } catch (NullPointerException e) {
            // Test passes because the exception is expected
        }
    }
}


