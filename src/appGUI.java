import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class appGUI {

    private JFrame frame;
    private Client client;

    public appGUI() {
        // Initialize GUI
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Swap_It");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 800);
            initializeLoginScreen();
            frame.setVisible(true);
        });
    }

    private void initializeLoginScreen() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(loginButton);

        frame.getContentPane().removeAll();
        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    public static void main(String[] args) {
        new appGUI();
    }

}
