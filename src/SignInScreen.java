import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.ObjectInputStream;
import java.io.IOException;

public class SignInScreen extends JPanel {

    private PrintWriter writer;  // Used for sending data to the server
    private ObjectInputStream objectReader; // Used for receiving data from the server
    private AppGUI appGUI;  // Reference to AppGUI to be able to call showHomeScreen()

    private JPanel titlePanel;
    private JLabel welcomeLabel;
    private JLabel titleLabel;

    private JPanel loginPanel;
    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JButton loginButton;

    private JPanel dividerPanel;
    private JSeparator leftSeparator;
    private JLabel orLabel;
    private JSeparator rightSeparator;

    private JPanel registerPanel;
    private JTextField registerUsernameField;
    private JTextField registerEmailField;
    private JPasswordField registerPasswordField;
    private JButton registerButton;

    public SignInScreen(PrintWriter writer, ObjectInputStream objectReader, AppGUI appGUI) {

        this.writer = writer;          // Pass the writer from AppGUI
        this.objectReader = objectReader; // Pass the objectReader from AppGUI
        this.appGUI = appGUI;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Title Panel Section
        titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        welcomeLabel = new JLabel("Welcome To", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titleLabel = new JLabel("Swap_It", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(255, 102, 51)); // Orange

        titlePanel.add(welcomeLabel);
        titlePanel.add(titleLabel);

        add(Box.createRigidArea(new Dimension(0, 40))); // Spacing
        add(titlePanel);  //Add login panel to main panel

        // Login Panel Section
        loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

        loginUsernameField = new JTextField("Username");
        loginUsernameField.setMaximumSize(new Dimension(300, 40));
        loginPasswordField = new JPasswordField("Password");
        loginPasswordField.setMaximumSize(new Dimension(300, 40));

        loginButton = new JButton("Log in");
        loginButton.setBackground(new Color(255, 178, 102)); // Light orange
        loginButton.setForeground(Color.BLACK);
        loginButton.setFocusPainted(false);
        loginButton.setMaximumSize(new Dimension(300, 40));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(new LoginActionListener());

        loginPanel.add(loginUsernameField);
        loginPanel.add(loginPasswordField);
        loginPanel.add(loginButton);

        add(Box.createRigidArea(new Dimension(0, 20))); // Spacing
        add(loginPanel);  //Add login panel to main panel
        add(Box.createRigidArea(new Dimension(0, 20))); // Spacing

        // Divider Section
        dividerPanel = new JPanel();
        dividerPanel.setLayout(new BoxLayout(dividerPanel, BoxLayout.X_AXIS));

        leftSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        leftSeparator.setPreferredSize(new Dimension(100, 1));

        orLabel = new JLabel("OR");
        orLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        orLabel.setForeground(Color.GRAY);
        orLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        rightSeparator.setPreferredSize(new Dimension(100, 1));

        dividerPanel.add(leftSeparator);
        dividerPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        dividerPanel.add(orLabel);
        dividerPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        dividerPanel.add(rightSeparator);

        add(dividerPanel);  //Add divider panel to main panel
        add(Box.createRigidArea(new Dimension(0, 20))); // Spacing

        // Register Section
        registerPanel = new JPanel();
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));

        registerUsernameField = new JTextField("Username");
        registerUsernameField.setMaximumSize(new Dimension(300, 40));
        registerEmailField = new JTextField("Email");
        registerEmailField.setMaximumSize(new Dimension(300, 40));
        registerPasswordField = new JPasswordField("Password");
        registerPasswordField.setMaximumSize(new Dimension(300, 40));

        registerButton = new JButton("Register");
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

        add(registerPanel);  //add register panel to main panel
    }

    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> {
                try {
                    String username = loginUsernameField.getText();
                    String password = new String(loginPasswordField.getPassword());

                    // Send login request
                    writer.println("1"); // Indicating login
                    writer.println(username);
                    writer.println(password);

                    boolean loginComplete = objectReader.readBoolean();  // Read server response

                    if (loginComplete) {
                        System.out.println("Login Successful");
                        appGUI.showHomeScreen();
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

    private class RegisterActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> {
                try {
                    String username = registerUsernameField.getText();
                    String email = registerEmailField.getText();
                    String password = new String(registerPasswordField.getPassword());

                    // Send register request
                    writer.println("2"); // Indicating registration
                    writer.println(username);
                    writer.println(email);
                    writer.println(password);

                    boolean registrationComplete = objectReader.readBoolean();  // Read server response

                    if (registrationComplete) {
                        System.out.println("Registration Successful");
                        appGUI.showHomeScreen();
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