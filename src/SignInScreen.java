import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;

/**
 * SignInScreen
 * <p>
 * Provides the GUI for the user to sign in or register in the social media application.
 * This panel includes fields for entering login credentials and registration details,
 * along with buttons to perform login or registration actions.
 *
 * @author Nick Wu, Chris Brantley, Ramya Prasanna, and Divya Vemireddy
 * @version December 7, 2024
 */
public class SignInScreen extends JPanel implements SignInScreenInterface {

    private BufferedReader reader;  // Reads data from the server
    private PrintWriter writer;  // Sends data to the server
    private ObjectInputStream objectReader;  // Receives object data from the server
    private AppGUI appGUI;  // Reference to the main application GUI for navigation

    private JTextField loginUsernameField;  // Input field for login username
    private JPasswordField loginPasswordField;  // Input field for login password
    private JTextField registerUsernameField;  // Input field for registration username
    private JTextField registerEmailField;  // Input field for registration email
    private JPasswordField registerPasswordField;  // Input field for registration password

    public SignInScreen(BufferedReader reader, PrintWriter writer, ObjectInputStream objectReader, AppGUI appGUI) {

        this.reader = reader;
        this.writer = writer;
        this.objectReader = objectReader;
        this.appGUI = appGUI;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Title Panel Section
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel welcomeLabel = new JLabel("Welcome To", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Swap_It", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(255, 102, 51)); // Orange

        titlePanel.add(welcomeLabel);
        titlePanel.add(titleLabel);

        add(Box.createRigidArea(new Dimension(0, 40))); // Spacing
        add(titlePanel);

        // Login Panel Section
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

        loginUsernameField = new JTextField("Username");
        loginUsernameField.setMaximumSize(new Dimension(300, 40));
        loginPasswordField = new JPasswordField("Password");
        loginPasswordField.setMaximumSize(new Dimension(300, 40));

        JButton loginButton = new JButton("Log in");
        loginButton.setBackground(new Color(255, 178, 102));
        loginButton.setForeground(Color.BLACK);
        loginButton.setFocusPainted(false);
        loginButton.setMaximumSize(new Dimension(300, 40));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(new LoginActionListener());

        loginPanel.add(loginUsernameField);
        loginPanel.add(loginPasswordField);
        loginPanel.add(loginButton);

        add(Box.createRigidArea(new Dimension(0, 20))); // Spacing
        add(loginPanel);  // Add login panel to main panel
        add(Box.createRigidArea(new Dimension(0, 20))); // Spacing

        // Divider Section
        JPanel dividerPanel = new JPanel();
        dividerPanel.setLayout(new BoxLayout(dividerPanel, BoxLayout.X_AXIS));

        JSeparator leftSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        leftSeparator.setPreferredSize(new Dimension(100, 1));

        JLabel orLabel = new JLabel("OR");
        orLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        orLabel.setForeground(Color.GRAY);
        orLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator rightSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        rightSeparator.setPreferredSize(new Dimension(100, 1));

        dividerPanel.add(leftSeparator);
        dividerPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        dividerPanel.add(orLabel);
        dividerPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        dividerPanel.add(rightSeparator);

        add(dividerPanel);  // Add divider panel to main panel
        add(Box.createRigidArea(new Dimension(0, 20))); // Spacing

        // Register Section
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));

        registerUsernameField = new JTextField("Username");
        registerUsernameField.setMaximumSize(new Dimension(300, 40));
        registerEmailField = new JTextField("Email");
        registerEmailField.setMaximumSize(new Dimension(300, 40));
        registerPasswordField = new JPasswordField("Password");
        registerPasswordField.setMaximumSize(new Dimension(300, 40));

        JButton registerButton = new JButton("Register");
        registerButton.setBackground(new Color(255, 178, 102)); // Light orange
        registerButton.setForeground(Color.BLACK);
        registerButton.setFocusPainted(false);
        registerButton.setMaximumSize(new Dimension(300, 40));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.addActionListener(new RegisterActionListener());

        registerPanel.add(registerUsernameField);
        registerPanel.add(registerEmailField);
        registerPanel.add(registerPasswordField);
        registerPanel.add(registerButton);

        add(registerPanel);  // Add register panel to main panel
    }

    /**
     * Handles the login button's action. Sends login credentials to the server and
     * navigates to the home screen on successful login. Displays an error message
     * if login fails.
     */
    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> {
                boolean loginComplete;
                try {
                    String username = loginUsernameField.getText();
                    String password = new String(loginPasswordField.getPassword());

                    // Send login request
                    writer.println("1"); // Indicating login
                    writer.println(username);
                    System.out.println("sent: " + username);
                    writer.println(password);
                    System.out.println("sent: " + password);
                    writer.flush();

                    loginComplete = objectReader.readBoolean();  // Read server response

                    if (loginComplete) {
                        System.out.println("Login Successful");
                        appGUI.showHomeScreen(reader, objectReader, writer, UserSearch.findUserByUsername(username));
                    } else {
                        JOptionPane.showMessageDialog(SignInScreen.this,
                                "Incorrect username or password. Please try again.",
                                "Login Failed",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();
        }
    }

    /**
     * Handles the register button's action. Sends registration details to the server
     * and navigates to the home screen on successful registration. Displays an error
     * message if the user already exists.
     */
    private class RegisterActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> {
                boolean registrationComplete;
                try {
                    String username = registerUsernameField.getText();
                    String email = registerEmailField.getText();
                    String password = new String(registerPasswordField.getPassword());

                    // Send register request
                    writer.println("2"); // Indicating registration
                    writer.println(username);
                    writer.println(email);
                    writer.println(password);
                    writer.flush();

                    registrationComplete = objectReader.readBoolean();  // Read server response

                    if (registrationComplete) {
                        appGUI.showHomeScreen(reader, objectReader, writer, UserSearch.findUserByUsername(username));
                    } else {
                        JOptionPane.showMessageDialog(SignInScreen.this,
                                "User already exists, please login",
                                "Registration Failed",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();
        }
    }
}