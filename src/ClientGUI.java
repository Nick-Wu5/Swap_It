import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class ClientGUI {
    private Client client;
    private JTextArea displayArea;
    private JTextField inputField;

    public ClientGUI(Client client) {
        this.client = client;
        setupGUI();
    }

    private void setupGUI() {
        JFrame frame = new JFrame("Client GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        frame.add(new JScrollPane(displayArea), BorderLayout.CENTER);

        // Input panel with fields for login/register
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));

        JTextField usernameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField passwordField = new JPasswordField();

        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(usernameField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            try {
                /* create in client.java
                public boolean login(String text, String text1){
                return false;
                }
                 */
                if (client.login(usernameField.getText(), passwordField.getText())) {
                    displayArea.append("Login successful.\n");
                } else {
                    displayArea.append("Login failed.\n");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            try {
                /* create in client.java
                public boolean register(String text, String text 1){
                return false;
                }
                 */
                // fix register to match client & server register
                if (client.register(usernameField.getText(), emailField.getText(), passwordField.getText())) {
                    displayArea.append("Registration successful.\n");
                } else {
                    displayArea.append("Registration failed.\n");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        inputPanel.add(loginButton);
        inputPanel.add(registerButton);
        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            Client clientImpl = new Client("localhost", 4242);
            new ClientGUI(clientImpl);
            /* create method in client.java
            public void connectToServer(){}
             */
            //should reflect client & server connecting
            clientImpl.connectToServer();
        } catch (IOException e) {
            System.out.println("Failed to connect to server: " + e.getMessage());
        }
    }
}