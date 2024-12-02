import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ProfileScreen extends JPanel {

    private AppGUI appGUI;
    private UserProfile user;

    public ProfileScreen(AppGUI gui, UserProfile userProfile) {

        this.appGUI = gui;
        this.user = userProfile;

        ArrayList<NewsPost> userPosts = userProfile.getUserPosts();
        ArrayList<String> userFriends = userProfile.getFriends();

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

        JLabel postsLabel = new JLabel("Posts: " + userPosts.size());
        postsLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        JLabel friendsLabel = new JLabel("Friends: " + userFriends.size());
        friendsLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        userInfoPanel.add(postsLabel);
        userInfoPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        userInfoPanel.add(friendsLabel);

        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainContentPanel.add(usernameLabel);
        mainContentPanel.add(userInfoPanel);

        //NAV BAR

        JPanel navBar = new JPanel();
        navBar.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
        navBar.setBackground(Color.WHITE);

        String[] navIcons = {"🔍", "🏠", "➕", "👤"};
        String[] pageNames = {"SearchScreen", "HomeScreen", "AddPostScreen", "ProfileScreen"};

        for (int i = 0; i < navIcons.length; i++) {
            JButton navButton = new JButton(navIcons[i]);
            navButton.setFocusPainted(false);
            navButton.setContentAreaFilled(false);
            navButton.setBorder(BorderFactory.createEmptyBorder());
            navButton.setFont(new Font("Arial", Font.PLAIN, 20));

            // Add ActionListener to navigate to the corresponding page
            final String pageName = pageNames[i];
            navButton.addActionListener(e -> appGUI.showPage(pageName));

            navBar.add(navButton);
        }

        add(mainContentPanel, BorderLayout.CENTER);
        add(navBar, BorderLayout.SOUTH);
    }
}