import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class FriendScreen extends JPanel {
    private AppGUI appGUI;
    private UserProfile user;
    private BufferedReader reader;
    private ObjectInputStream objectInputStream;
    private PrintWriter writer;
    private JTextField userToSearch;

    public FriendScreen(BufferedReader reader, PrintWriter writer, ObjectInputStream objectReader, AppGUI gui, UserProfile userProfile) {
        this.reader = reader;
        this.writer = writer;
        this.objectInputStream = objectReader;
        this.appGUI = gui;
        this.user = userProfile;

        setLayout(new BorderLayout());

        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));

        JLabel usernameLabel = new JLabel(userProfile.getUsername(), SwingConstants.CENTER);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 40));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.X_AXIS));
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        userInfoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel friendsLabel = new JLabel("Friends: " + userProfile.getFriends().size());
        friendsLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        JLabel blockedLabel = new JLabel("Blocked: " + userProfile.getBlockedFriends().size());
        blockedLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        userInfoPanel.add(friendsLabel);
        userInfoPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        userInfoPanel.add(blockedLabel);

        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainContentPanel.add(usernameLabel);
        mainContentPanel.add(userInfoPanel);

        JButton addFriendButton = new JButton("Add Friend");
        addFriendButton.setBackground(new Color(255, 178, 102));
        addFriendButton.setForeground(Color.BLACK);
        addFriendButton.setFocusPainted(false);
        addFriendButton.setFont(new Font("Arial", Font.PLAIN, 20));
        addFriendButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        addFriendButton.addActionListener(e -> {
            // Send that the user wants to display posts to server
            writer.println("1");
            writer.println(searchUser());

            //Retrieve posts from processed user from server
            String message = null;

            try {
                message = reader.readLine();
                displayMessage(message);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            writer.println("4");
        });

        JButton blockUserButton = new JButton("Block User");
        blockUserButton.setBackground(new Color(255, 178, 102));
        blockUserButton.setForeground(Color.BLACK);
        blockUserButton.setFocusPainted(false);
        blockUserButton.setFont(new Font("Arial", Font.PLAIN, 20));
        blockUserButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        blockUserButton.addActionListener(e -> {
            // Send that the user wants to display posts to server
            writer.println("2");
            writer.println(searchUser());

            //Retrieve posts from processed user from server
            String message = null;

            try {
                message = reader.readLine();
                displayMessage(message);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            writer.println("4");
        });

        JButton removeFriendButton = new JButton("Remove Friend");
        removeFriendButton.setBackground(new Color(255, 178, 102));
        removeFriendButton.setForeground(Color.BLACK);
        removeFriendButton.setFocusPainted(false);
        removeFriendButton.setFont(new Font("Arial", Font.PLAIN, 20));
        removeFriendButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        removeFriendButton.addActionListener(e -> {
            // Send that the user wants to display posts to server
            writer.println("3");
            writer.println(searchUser());

            //Retrieve posts from processed user from server
            String message = null;

            try {
                message = reader.readLine();
                displayMessage(message);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            writer.println("4");
        });

        JButton unblockUserButton = new JButton("Unblock User");
        unblockUserButton.setBackground(new Color(255, 178, 102));
        unblockUserButton.setForeground(Color.BLACK);
        unblockUserButton.setFocusPainted(false);
        unblockUserButton.setFont(new Font("Arial", Font.PLAIN, 20));
        unblockUserButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        unblockUserButton.addActionListener(e -> {
            // Send that the user wants to display posts to server
            writer.println("4");
            writer.println(searchUser());

            //Retrieve posts from processed user from server
            String message = null;

            try {
                message = reader.readLine();
                displayMessage(message);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            writer.println("4");
        });

        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainContentPanel.add(addFriendButton); // Existing View Posts button
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainContentPanel.add(removeFriendButton);
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainContentPanel.add(blockUserButton);
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainContentPanel.add(unblockUserButton);

        add(mainContentPanel, BorderLayout.CENTER);
        add(createNavBar(), BorderLayout.SOUTH);
    }

    private String searchUser() {
        return JOptionPane.showInputDialog(null, "Enter Username Here");
    }

    private void displayMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Friend Actions", JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createNavBar() {
        // Navigation Bar
        JPanel navBar = new JPanel();
        navBar.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10)); // Centered navigation bar
        navBar.setBackground(Color.WHITE);

        String[] pageNames = {"SearchScreen", "ContentScreen", "HomeScreen", "FriendScreen", "ProfileScreen"};
        String[] icons = {"searchIcon.png", "contentIcon.png", "homeIcon.png", "friendsIcon.png", "profileIcon.png"};

        for (int i = 0; i < pageNames.length; i++) {
            // Load the icon
            ImageIcon icon = new ImageIcon("images/navBarIcons/" + icons[i]); // Replace "path/to/icons/" with the actual path

            // Optionally scale the icon to fit the button size
            Image scaledImage = icon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            // Create a button with the icon
            JButton navButton = new JButton(scaledIcon);
            navButton.setFocusPainted(false);
            navButton.setContentAreaFilled(false);
            navButton.setBorder(BorderFactory.createEmptyBorder());

            // Add ActionListener to navigate to the corresponding page
            final String pageName = pageNames[i];
            navButton.addActionListener(e -> appGUI.showPage(pageName));

            navBar.add(navButton);
        }

        return navBar;
    }
}