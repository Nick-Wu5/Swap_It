import org.junit.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Timeout;

import java.io.*;
import java.net.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        String expected = "Server: Client connected";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try (Socket clientSocket = new Socket("localhost", PORT)) {
            Thread.sleep(500); // Wait for server to process connection
            String stuOut = outContent.toString().replace("\r\n", "\n").trim();
        } catch (IOException | InterruptedException e) {
            fail("Exception during client-server communication: " + e.getMessage());
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    @Timeout(1)
    public void testInvalidPortHandling() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ServerSocket(-1);
        }, "Expected IllegalArgumentException for invalid port number");
    }
}


