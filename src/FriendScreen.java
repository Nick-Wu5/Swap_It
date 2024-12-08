import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;

/**
 * FriendScreen
 * <p>
 * A JPanel that represents the "Friends" screen in the social media application.
 * It allows users to view their friends and blocked users, as well as perform
 * actions like adding, removing, blocking, or unblocking users.
 *
 * @author Nick Wu, Chris Brantley, Ramya Prasanna, and Divya Vemireddy
 * @version December 7, 2024
 */
public class FriendScreen extends JPanel {

    private AppGUI appGUI;  // Reference to the main application GUI
    private UserProfile user;  // Profile of the logged-in user
    private BufferedReader reader;  // Stream for reading text data from the server
    private ObjectInputStream objectInputStream;  // Stream for receiving serialized object data
    private PrintWriter writer;  // Stream for sending text data to the server

    private JPanel mainContentPanel;
    private JList<String> friendsListUI = new JList<>();  // UI component to display the friends list
    private JList<String> blockedListUI = new JList<>();  // UI component to display the blocked users list
    private DefaultListModel<String> friendsListModel = new DefaultListModel<>();  // Model for the friends list UI
    private DefaultListModel<String> blockedListModel = new DefaultListModel<>();  // Model for the blocked users list
    // UI

    public FriendScreen(BufferedReader reader, PrintWriter writer, ObjectInputStream objectReader, AppGUI gui,
                        UserProfile userProfile) {

        this.reader = reader;
        this.writer = writer;
        this.objectInputStream = objectReader;
        this.appGUI = gui;
        this.user = userProfile;

        setLayout(new BorderLayout());

        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));

        JLabel usernameLabel = new JLabel(userProfile.getUsername(), SwingConstants.CENTER);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 40));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.X_AXIS));
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        userInfoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel listsPanel = new JPanel();
        listsPanel.setLayout(new BoxLayout(listsPanel, BoxLayout.X_AXIS));
        listsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        listsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel friendsLabel = new JLabel();

        if (userProfile.getFriends().getFirst().equals("EmptyFriendsList")) {
            friendsLabel.setText("Friends: 0");
        } else {
            friendsLabel.setText("Friends: " + userProfile.getFriends().size());
        }
        friendsLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        JLabel blockedLabel = new JLabel();
        if (userProfile.getBlockedFriends().getFirst().equals("EmptyBlockedList")) {
            blockedLabel.setText("Blocked: 0");
        } else {
            blockedLabel.setText("Blocked: " + userProfile.getBlockedFriends().size());
        }
        blockedLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        userInfoPanel.add(friendsLabel);
        userInfoPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        userInfoPanel.add(blockedLabel);

        this.refreshFriendBlockedLists();
        friendsListUI.setModel(friendsListModel);
        blockedListUI.setModel(blockedListModel);

        JScrollPane friendsScrollPane = new JScrollPane(friendsListUI);
        friendsScrollPane.setMaximumSize(new Dimension(150, 200));

        JScrollPane blockedScrollPane = new JScrollPane(blockedListUI);
        blockedScrollPane.setMaximumSize(new Dimension(150, 200));

        friendsScrollPane.setBorder(BorderFactory.createTitledBorder("Friends List"));
        blockedScrollPane.setBorder(BorderFactory.createTitledBorder("Blocked Users"));

        listsPanel.add(friendsScrollPane);
        listsPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        listsPanel.add(blockedScrollPane);

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
                friendsLabel.setText("Friends: " + userProfile.getFriends().size());
                this.refreshFriendBlockedLists();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            writer.println("3");
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
                if (userProfile.getBlockedFriends().getFirst().equals("EmptyBlockedList")) {
                    blockedLabel.setText("Blocked: 0");
                } else {
                    blockedLabel.setText("Blocked: " + userProfile.getBlockedFriendsFromFile().size());
                }
                if (userProfile.getFriends().getFirst().equals("EmptyFriendsList")) {
                    friendsLabel.setText("Friends: 0");
                } else {
                    friendsLabel.setText("Friends: " + userProfile.getFriends().size());
                }
                this.refreshFriendBlockedLists();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            writer.println("3");
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
                if (userProfile.getFriends().getFirst().equals("EmptyFriendsList")) {
                    friendsLabel.setText("Friends: 0");
                } else {
                    friendsLabel.setText("Friends: " + userProfile.getFriends().size());
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            this.refreshFriendBlockedLists();
            writer.println("3");
        });

        JButton unblockUserButton = new JButton("Unblock User");
        unblockUserButton.setBackground(new Color(255, 178, 102));
        unblockUserButton.setForeground(Color.BLACK);
        unblockUserButton.setFocusPainted(false);
        unblockUserButton.setFont(new Font("Arial", Font.PLAIN, 20));
        unblockUserButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        unblockUserButton.addActionListener(e -> {

            writer.println("4");
            writer.println(searchUser());

            String message = null;

            try {
                message = reader.readLine();
                displayMessage(message);
                friendsLabel.setText("Friends: " + userProfile.getFriends().size());
                blockedLabel.setText("Blocked: " + userProfile.getBlockedFriendsFromFile().size());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            this.refreshFriendBlockedLists();
            writer.println("3");
        });

        mainContentPanel.add(listsPanel);
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

    /**
     * Prompts the user to enter a username for search or actions (e.g., add, block, remove).
     *
     * @return the username entered by the user
     */
    private String searchUser() {
        return JOptionPane.showInputDialog(mainContentPanel,
                "Enter Username Here");
    }

    /**
     * Displays a message to the user in a dialog box.
     *
     * @param message the message to display
     */
    private void displayMessage(String message) {
        JOptionPane.showMessageDialog(mainContentPanel, message, "Friend Actions", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Creates the navigation bar at the bottom of the screen with buttons
     * for navigating between main application screens.
     *
     * @return the navigation bar as a JPanel
     */
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

    /**
     * Updates the friends and blocked users lists displayed in the UI
     * based on the data in the user's profile.
     */
    private void refreshFriendBlockedLists() {

        friendsListModel.clear();
        blockedListModel.clear();

        if (!user.getFriends().getFirst().equals("EmptyFriendsList")) {
            for (String friend : user.getFriends()) {
                friendsListModel.addElement(friend);
            }
        } else {
            friendsListModel.addElement("No friends :(");
        }

        if (!user.getBlockedFriendsFromFile().getFirst().equals("EmptyBlockedList")) {
            for (String blockedUser : user.getBlockedFriendsFromFile()) {
                blockedListModel.addElement(blockedUser);
            }
        } else {
            blockedListModel.addElement("No blocked users");
        }

    }

}