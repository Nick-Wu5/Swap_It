import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class SearchScreen extends JPanel implements SearchScreenInterface {

    // Network communication components
    private PrintWriter writer;                // To send data to the server
    private ObjectInputStream objectReader;    // To read data from the server

    private AppGUI appGUI;                     // Reference to the main GUI
    private UserProfile user;                  // Current user profile

    // UI components
    JTextField userToSearch;                   // Text field for entering a username
    JPanel displayPanel;                       // Panel to display search results
    JScrollPane displayScrollPane;             // Scroll pane for displaying posts

    // Constructor to initialize components
    public SearchScreen(PrintWriter writer, ObjectInputStream objectReader, AppGUI gui, UserProfile userProfile) {
        this.writer = writer;
        this.objectReader = objectReader;
        this.appGUI = gui;
        this.user = userProfile;

        setLayout(new BorderLayout());

        // Create and add the header label
        JLabel label = new JLabel("Search Screen", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);

        // Create the search panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search bar
        userToSearch = new JTextField("Enter Username Here");
        userToSearch.setMaximumSize(new Dimension(300, 40));
        searchPanel.add(userToSearch);

        // Search button
        JButton searchButton = new JButton("Search");
        searchButton.setMaximumSize(new Dimension(300, 40));
        searchButton.addActionListener(new SearchLoginListener());
        searchPanel.add(searchButton);

        // Create the display panel for search results
        displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));

        // Add panels to the main layout
        add(searchPanel, BorderLayout.NORTH);
        add(displayPanel, BorderLayout.CENTER);
        add(createNavBar(), BorderLayout.SOUTH);
    }

    // Action listener for the search button
    private class SearchLoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> {
                writer.println(userToSearch.getText());

                Object searchedProfile = null;

                try {
                    searchedProfile = objectReader.readObject();
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }

                if (searchedProfile instanceof String) {
                    JOptionPane.showMessageDialog(SearchScreen.this,
                            "User doesn't exist",
                            "Search Error",
                            JOptionPane.ERROR_MESSAGE);
                } else if (searchedProfile instanceof UserProfile) {
                    UserProfile searchedUser = (UserProfile) searchedProfile;

                    // Display user information
                    displayUserInfo(searchedUser);

                    // Display user's posts
                    JPanel postsPanel = new JPanel();
                    postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));

                    for (NewsPost post : searchedUser.getUserPosts()) {
                        postsPanel.add(searchPostPanel(post));
                    }

                    // Scroll pane for posts
                    displayScrollPane = new JScrollPane(postsPanel);
                    displayScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                    displayScrollPane.setMinimumSize(new Dimension(displayPanel.getWidth(), 300));
                    displayScrollPane.setMaximumSize(new Dimension(displayPanel.getWidth(), 300));

                    displayPanel.add(displayScrollPane);
                    displayPanel.revalidate();
                    displayPanel.repaint();
                } else {
                    JOptionPane.showMessageDialog(SearchScreen.this,
                            "Unable to find user",
                            "Search Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }).start();
        }
    }

    // Display user profile information
    private void displayUserInfo(UserProfile user) {
        displayPanel.removeAll();

        JLabel userNameLabel = new JLabel("Profile: " + user.getUsername());
        userNameLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        JPanel userFriendsPostsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));

        JLabel friendsLabel = new JLabel("Friends: " + user.getFriends().size());
        friendsLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        JLabel postsLabel = new JLabel("Posts: " + user.getUserPosts().size());
        postsLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        userFriendsPostsPanel.add(friendsLabel);
        userFriendsPostsPanel.add(postsLabel);

        displayPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        displayPanel.add(userNameLabel);
        displayPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        displayPanel.add(userFriendsPostsPanel);
        displayPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        displayPanel.revalidate();
        displayPanel.repaint();
    }

    // Create a panel for each post
    private JPanel searchPostPanel(NewsPost post) {
        JPanel postPanel = new JPanel();
        postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
        postPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        postPanel.setBackground(new Color(230, 230, 230));

        // Display resized image
        ImageIcon originalIcon = new ImageIcon(post.getImagePath());
        Image resizedImage = originalIcon.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        JLabel imageLabel = new JLabel(resizedIcon);
        imageLabel.setPreferredSize(new Dimension(300, 150));

        // Display caption
        JLabel captionLabel = new JLabel("@" + post.getAuthor() + " - " + post.getCaption());

        // Comment and view comments buttons
        JPanel commentPanel = new JPanel(new FlowLayout());
        JButton commentButton = new JButton("Comment");
        JButton viewCommentsButton = new JButton("View Comments");

        commentPanel.add(commentButton);
        commentPanel.add(viewCommentsButton);

        postPanel.add(imageLabel);
        postPanel.add(captionLabel);
        postPanel.add(commentPanel);

        return postPanel;
    }

    // Create the navigation bar
    private JPanel createNavBar() {
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        navBar.setBackground(Color.WHITE);

        String[] pageNames = {"SearchScreen", "ContentScreen", "HomeScreen", "FriendScreen", "ProfileScreen"};
        String[] icons = {"searchIcon.png", "contentIcon.png", "homeIcon.png", "friendsIcon.png", "profileIcon.png"};

        for (int i = 0; i < pageNames.length; i++) {
            ImageIcon icon = new ImageIcon("images/navBarIcons/" + icons[i]);
            Image scaledImage = icon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            JButton navButton = new JButton(new ImageIcon(scaledImage));
            navButton.setFocusPainted(false);
            navButton.setContentAreaFilled(false);
            navButton.setBorder(BorderFactory.createEmptyBorder());

            final String pageName = pageNames[i];
            navButton.addActionListener(e -> appGUI.showPage(pageName));

            navBar.add(navButton);
        }

        return navBar;
    }
}