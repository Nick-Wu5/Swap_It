import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class AppGUI extends JFrame {

    //Home Screen Components
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;  // For switching between screens

    private JPanel signInScreen;  // Panel for the Sign-In screen
    private JPanel homeScreen;    // Panel for the home screen (or next screen)
    private JPanel searchScreen;
    private JPanel addPostScreen;
    private JPanel profileScreen;

    //Network / File IO
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private ObjectInputStream objectReader;

    public AppGUI() {
        // Initialize GUI
        SwingUtilities.invokeLater(this::initializeGUI);
    }

    private void initializeGUI() {
        frame = new JFrame("Swap_It");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 700);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        //CardLayout to handle multiple screens
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        frame.add(mainPanel, BorderLayout.CENTER);

        connectToServer();
        initializeSignInScreen();  // Add the Sign-In screen initially

        frame.revalidate();
        frame.repaint();
    }

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

    private void initializeSignInScreen() {

        signInScreen = new SignInScreen(writer, objectReader, this);  // Create the SignInScreen instance

        // Add the sign-in panel to the main panel
        mainPanel.add(signInScreen, "SignInScreen");

        // Start the application with the Sign-In screen visible
        cardLayout.show(mainPanel, "SignInScreen");
    }

    public void showHomeScreen(ObjectInputStream objectReader, PrintWriter writer, UserProfile loggedInUser) {
        // Create the home screen (next screen after login)
        homeScreen = new HomeScreen(objectReader, writer,this, loggedInUser);

        // Add the home screen to the main panel
        mainPanel.add(homeScreen, "HomeScreen");

        // Switch to the home screen
        cardLayout.show(mainPanel, "HomeScreen");
    }

    public void initializeOtherPages(UserProfile loggedInUser, ObjectInputStream objectReader, PrintWriter writer) {

        searchScreen = new SearchScreen(writer, objectReader,  this, loggedInUser);
        addPostScreen = new AddPostScreen(this, loggedInUser);
        profileScreen = new ProfileScreen(this, loggedInUser);

        mainPanel.add(searchScreen, "SearchScreen");
        mainPanel.add(addPostScreen, "AddPostScreen");
        mainPanel.add(profileScreen, "ProfileScreen");
    }

    public void showPage(String pageName) {

        cardLayout.show(mainPanel, pageName);
        System.out.println("Changed to " + pageName);

        switch (pageName){
            case "SearchScreen":
                writer.println("1");
                break;
            case "AddPostScreen":
                writer.println("2");
                break;
            case "ProfileScreen":
                writer.println("3");
                break;
            case "HomeScreen":
                break;
        }
    }

    public static void main(String[] args) {
        new AppGUI();
    }
}
