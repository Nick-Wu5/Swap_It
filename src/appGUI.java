import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class appGUI {

    //Home Screen Components
    private JFrame frame;
    private JPanel mainPanel;

    private JLabel welcomeLabel;
    private JLabel titleLabel;
    private JPanel titlePanel;

    private JPanel loginPanel;
    private JButton loginButton;
    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;

    private JPanel dividerPanel;
    private JSeparator leftSeparator;
    private JLabel orLabel;
    private JSeparator rightSeparator;

    private JTextField registerUsernameField;

    //Network / File IO
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private ObjectInputStream objectReader;

    public appGUI() {
        // Initialize GUI
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Swap_It");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 700);
            connectToServer();
            initializeSignIn();
            frame.setVisible(true);
            frame.setLayout(new BorderLayout());
        });
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

    private void initializeSignIn() {

        // Main Panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        frame.add(mainPanel, BorderLayout.CENTER);

        // Title Section
        welcomeLabel = new JLabel("Welcome To", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titleLabel = new JLabel("Swap_It", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(255, 102, 51)); // Orange underscore

        titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 30))); // Spacing
        titlePanel.add(welcomeLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing
        titlePanel.add(titleLabel);

        mainPanel.add(titlePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacing

        // Login Section
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

        loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBackground(Color.WHITE);
        loginPanel.add(loginUsernameField);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing
        loginPanel.add(loginPasswordField);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacing
        loginPanel.add(loginButton);

        mainPanel.add(loginPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacing

        // Divider Section
        dividerPanel = new JPanel();
        dividerPanel.setLayout(new BoxLayout(dividerPanel, BoxLayout.X_AXIS));
        dividerPanel.setBackground(Color.WHITE);

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

        mainPanel.add(dividerPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacing

        // Register Section
        registerUsernameField = new JTextField("Username");
        registerUsernameField.setMaximumSize(new Dimension(300, 40));
        JTextField registerEmailField = new JTextField("Email");
        registerEmailField.setMaximumSize(new Dimension(300, 40));
        JPasswordField registerPasswordField = new JPasswordField("Password");
        registerPasswordField.setMaximumSize(new Dimension(300, 40));

        JButton registerButton = new JButton("Register");
        registerButton.setBackground(new Color(255, 178, 102)); // Light orange
        registerButton.setForeground(Color.BLACK);
        registerButton.setFocusPainted(false);
        registerButton.setMaximumSize(new Dimension(300, 40));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));
        registerPanel.setBackground(Color.WHITE);
        registerPanel.add(registerUsernameField);
        registerPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing
        registerPanel.add(registerEmailField);
        registerPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing
        registerPanel.add(registerPasswordField);
        registerPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacing
        registerPanel.add(registerButton);

        mainPanel.add(registerPanel);

        frame.getContentPane().removeAll();
        frame.add(mainPanel);
        frame.revalidate();
        frame.repaint();
    }

    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> {
                try {
                    String username = loginUsernameField.getText();
                    String password = new String(loginPasswordField.getPassword());

                    writer.println("1"); // Indicating login
                    writer.println(username);
                    writer.println(password);

                    boolean loginComplete = objectReader.readBoolean();

                    if (loginComplete) {
                        System.out.println("Login Successful");
                    } else {
                        JOptionPane.showMessageDialog(mainPanel,
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

    public static void main(String[] args) {
        new appGUI();
    }
}
