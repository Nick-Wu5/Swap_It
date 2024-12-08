import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * ProfileScreen
 * <p>
 * Represents the user profile screen in the social media application. This screen displays
 * user-specific information, including their username, post count, friends count, and personal details.
 * Users can view their posts or personal information in a dynamic interface.
 *
 * @author Nick Wu, Chris Brantley, Ramya Prasanna, and Divya Vemireddy
 * @version December 7, 2024
 */
public class ProfileScreen extends JPanel implements ProfileScreenInterface {

    private AppGUI appGUI;  // Reference to the main application GUI
    private UserProfile user;  // Profile of the logged-in user
    private BufferedReader reader;  // Stream for reading text data from the server
    private ObjectInputStream objectInputStream;  // Stream for receiving serialized object data
    private PrintWriter writer;  // Stream for sending text data to the server
    private JPanel displayInfoPanel;  // Panel for displaying user-specific information
    private JPanel postsPanel;  // Panel for displaying the user's posts
    private JScrollPane scrollPanel;  // Scrollable panel for navigating through posts

    public ProfileScreen(BufferedReader reader, PrintWriter writer, ObjectInputStream objectReader, AppGUI gui,
                         UserProfile userProfile) {

        this.appGUI = gui;
        this.user = userProfile;
        this.reader = reader;
        this.objectInputStream = objectReader;
        this.writer = writer;

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

        JLabel postsLabel = new JLabel("Posts: " + userProfile.getUserPosts().size());
        postsLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        JLabel friendsLabel = new JLabel();
        if (userProfile.getFriends().getFirst().equals("EmptyFriendsList")) {
            friendsLabel.setText("Friends: 0");
        } else {
            friendsLabel.setText("Friends: " + userProfile.getFriends().size());
        }
        friendsLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        userInfoPanel.add(postsLabel);
        userInfoPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        userInfoPanel.add(friendsLabel);

        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainContentPanel.add(usernameLabel);
        mainContentPanel.add(userInfoPanel);

        JButton viewPostsButton = new JButton("View Your Posts");
        viewPostsButton.setBackground(new Color(255, 178, 102));
        viewPostsButton.setForeground(Color.BLACK);
        viewPostsButton.setFocusPainted(false);
        viewPostsButton.setFont(new Font("Arial", Font.PLAIN, 20));
        viewPostsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        displayInfoPanel = new JPanel();
        displayInfoPanel.setLayout(new BorderLayout());

        viewPostsButton.addActionListener(e -> {

            displayInfoPanel.removeAll();
            // Send that the user wants to display posts to server
            writer.println("1");

            //Retrieve posts from processed user from server
            Object posts;

            try {
                posts = objectReader.readObject();
                System.out.println("Successfully read object: " + posts.getClass().getName());
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }

            if (posts instanceof ArrayList<?>) {

                if (((ArrayList<?>) posts).isEmpty()) {

                    JOptionPane.showMessageDialog(ProfileScreen.this,
                            "You have no posts!",
                            "Post Display Error",
                            JOptionPane.ERROR_MESSAGE);

                    writer.println("4");
                } else  {

                    postsPanel = new JPanel();
                    postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));
                    postsPanel.setAutoscrolls(true);
                    ArrayList<NewsPost> userPosts = new ArrayList<>();
                    for (Object obj : (ArrayList<?>) posts) {
                        if (obj instanceof NewsPost) {
                            userPosts.add((NewsPost) obj);
                        } else {
                            System.out.println("Found non-NewsPost object");
                            return;
                        }
                    }

                    //Display posts in scrollable fashion to screen
                    for (NewsPost post : userPosts) {
                        postsPanel.add(createGeneralPostPanel(post));
                        postsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                    }

                    scrollPanel = new JScrollPane(postsPanel);
                    scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                    scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

                    displayInfoPanel.add(scrollPanel, BorderLayout.CENTER);
                    displayInfoPanel.revalidate();
                    displayInfoPanel.repaint();
                    writer.println("4");
                }
            }
        });

        JButton viewPersonalInfoButton = new JButton("View Personal Info");
        viewPersonalInfoButton.setBackground(new Color(255, 178, 102));
        viewPersonalInfoButton.setForeground(Color.BLACK);
        viewPersonalInfoButton.setFocusPainted(false);
        viewPersonalInfoButton.setFont(new Font("Arial", Font.PLAIN, 20));
        viewPersonalInfoButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        viewPersonalInfoButton.addActionListener(e -> {

            // Send that the user wants to display posts to server
            writer.println("2");

            try {

                displayInfoPanel.removeAll();

                String[] userInfo = reader.readLine().split(";");

                // Create a panel with vertical layout for stacking labels
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

                JLabel userNameLabel = new JLabel("Username: " + userInfo[0]);
                userNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                infoPanel.add(userNameLabel);
                infoPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer

                JLabel emailLabel = new JLabel("Email: " + userInfo[1]);
                emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                infoPanel.add(emailLabel);
                infoPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer

                JLabel passwordLabel = new JLabel("Password: " + userInfo[2]);
                passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                infoPanel.add(passwordLabel);
                infoPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer

                JLabel userFriendsLabel = new JLabel("Friends: " + userInfo[3]);
                userFriendsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                infoPanel.add(userFriendsLabel);
                infoPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer

                JLabel blockedLabel = new JLabel("Blocked: " + userInfo[4]);
                blockedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                infoPanel.add(blockedLabel);

                // Add the info panel to the main display panel
                displayInfoPanel.setLayout(new BorderLayout());
                displayInfoPanel.add(infoPanel, BorderLayout.CENTER);

                displayInfoPanel.add(infoPanel);
                displayInfoPanel.revalidate();
                displayInfoPanel.repaint();

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            writer.println("4");
        });

        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainContentPanel.add(viewPostsButton); // Existing View Posts button
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainContentPanel.add(viewPersonalInfoButton);
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainContentPanel.add(displayInfoPanel);

        add(mainContentPanel, BorderLayout.CENTER);
        add(createNavBar(), BorderLayout.SOUTH);
    }

    /**
     * Creates a panel to display a specific post's details, including its image and caption.
     * The panel is styled and formatted for a clean and organized presentation.
     *
     * @param post The NewsPost object representing the post to display
     * @return A JPanel containing the post's image and caption
     */
    private JPanel createGeneralPostPanel(NewsPost post) {

        JPanel postPanel = new JPanel();
        postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
        postPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        postPanel.setBackground(new Color(230, 230, 230));

        JLabel imageLabel = new JLabel();

        // Caption Label
        JLabel captionLabel = new JLabel();
        captionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        captionLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Load original image
        ImageIcon originalIcon = new ImageIcon(post.getImagePath());
        Image originalImage = originalIcon.getImage();

        //Resize
        Image resizedImage = originalImage.getScaledInstance(300, 150, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        imageLabel = new JLabel(resizedIcon);
        imageLabel.setPreferredSize(new Dimension(300, 150));
        imageLabel.setOpaque(true);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Caption Label
        captionLabel.setText("@" + post.getAuthor() + " - " + post.getCaption());

        postPanel.add(imageLabel);
        postPanel.add(captionLabel);

        return postPanel;
    }

    /**
     * Creates the navigation bar at the bottom of the screen. The navigation bar allows users
     * to switch between main application screens, such as Search, Home, and Profile screens.
     *
     * @return A JPanel representing the navigation bar with navigation buttons
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
}