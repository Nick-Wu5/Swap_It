import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

/**
 * AppGUI
 * <p>
 * The main GUI class for the Swap_It application. It manages multiple screens
 * using a CardLayout and handles network communication with the server.
 *
 * @version December 7, 2024
 * @author Nick Wu, Chris Brantley, Ramya Prasanna, and Divya Vemireddy
 */
public class AppGUI extends JFrame {

    // Home Screen Components
    private JFrame frame;  // Main application window
    private JPanel mainPanel;  // Central panel containing multiple screens
    private CardLayout cardLayout;  // Layout manager for switching screens

    // Panels for different application screens
    private JPanel signInScreen;  // Panel for the sign-in screen
    private JPanel homeScreen;  // Panel for the home screen
    private JPanel searchScreen;  // Panel for the search screen
    private JPanel contentScreen;  // Panel for the content screen
    private JPanel profileScreen;  // Panel for the profile screen
    private JPanel friendScreen;  // Panel for the friend list screen

    // Network / File IO
    private Socket socket;  // Socket for connecting to the server
    private PrintWriter writer;  // For sending messages to the server
    private BufferedReader reader;  // For receiving messages from the server
    private ObjectInputStream objectReader;  // For receiving serialized objects from the server

    public AppGUI() {
        SwingUtilities.invokeLater(this::initializeGUI);
    }

    /**
     * The main method that launches the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        new AppGUI();
    }

    /**
     * Initializes the GUI components, sets up the main frame, and connects to the server.
     */
    private void initializeGUI() {
        frame = new JFrame("Swap_It");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 700);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        // CardLayout to handle multiple screens
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        frame.add(mainPanel, BorderLayout.CENTER);

        connectToServer();  // Establish connection to the server
        initializeSignInScreen();  // Set up the Sign-In screen as the initial view

        frame.revalidate();
        frame.repaint();
    }

    /**
     * Connects to the server and initializes network streams.
     */
    private void connectToServer() {
        try {
            socket = new Socket("localhost", 1234);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            objectReader = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Unable to connect to server");
            e.printStackTrace();
        }
    }

    /**
     * Initializes the Sign-In screen and adds it to the main panel.
     */
    private void initializeSignInScreen() {
        signInScreen = new SignInScreen(reader, writer, objectReader, this);  // Create the Sign-In screen
        // instance
        mainPanel.add(signInScreen, "SignInScreen");  // Add the Sign-In panel to the main panel
        cardLayout.show(mainPanel, "SignInScreen");  // Display the Sign-In screen
    }

    /**
     * Creates and displays the Home screen after a user logs in.
     *
     * @param reader the BufferedReader for server communication
     * @param objectReader the ObjectInputStream for receiving serialized objects
     * @param writer the PrintWriter for sending messages to the server
     * @param loggedInUser the UserProfile of the logged-in user
     */
    public void showHomeScreen(BufferedReader reader, ObjectInputStream objectReader, PrintWriter writer,
                               UserProfile loggedInUser) {
        homeScreen = new HomeScreen(reader, objectReader, writer, this, loggedInUser);
        mainPanel.add(homeScreen, "HomeScreen");
        cardLayout.show(mainPanel, "HomeScreen");
        System.out.println("called showHomeScreen");
    }

    /**
     * Initializes additional application pages and adds them to the main panel.
     *
     * @param loggedInUser the UserProfile of the logged-in user
     * @param reader the BufferedReader for server communication
     * @param objectReader the ObjectInputStream for receiving serialized objects
     * @param writer the PrintWriter for sending messages to the server
     */
    public void initializeOtherPages(UserProfile loggedInUser, BufferedReader reader, ObjectInputStream objectReader,
                                     PrintWriter writer) {
        searchScreen = new SearchScreen(writer, objectReader, this, loggedInUser);
        contentScreen = new ContentScreen(reader, writer, objectReader, this, loggedInUser);
        profileScreen = new ProfileScreen(reader, writer, objectReader, this, loggedInUser);
        friendScreen = new FriendScreen(reader, writer, objectReader, this, loggedInUser);

        mainPanel.add(searchScreen, "SearchScreen");
        mainPanel.add(contentScreen, "ContentScreen");
        mainPanel.add(profileScreen, "ProfileScreen");
        mainPanel.add(friendScreen, "FriendScreen");
    }

    /**
     * Switches to the specified screen and sends the appropriate message to the server.
     *
     * @param pageName the name of the page to display
     */
    public void showPage(String pageName) {
        cardLayout.show(mainPanel, pageName);  // Display the specified page
        writer.println("exit");
        System.out.println("Changed to " + pageName);

        // Send a specific command to the server based on the page being displayed
        switch (pageName) {
            case "SearchScreen":
                writer.println("1");
                break;
            case "ContentScreen":
                writer.println("2");
                break;
            case "FriendScreen":
                writer.println("3");
                break;
            case "ProfileScreen":
                writer.println("4");
                break;
            case "HomeScreen":
                writer.println("5");
                break;
            default:
                break;
        }
    }
}