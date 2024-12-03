import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class SearchScreen extends JPanel {

    private PrintWriter writer;  // Used for sending data to the server
    private ObjectInputStream objectReader;
    private AppGUI appGUI;
    private UserProfile user;
    JTextField userToSearch;
    JPanel displayPanel;

    public SearchScreen(PrintWriter writer, ObjectInputStream objectReader, AppGUI gui, UserProfile userProfile) {

        this.appGUI = gui;
        this.user = userProfile;
        this.objectReader = objectReader;
        this.writer = writer;

        setLayout(new BorderLayout());
        JLabel label = new JLabel("Search Screen", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);

        //SEARCH PANEL

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Search Bar
        userToSearch = new JTextField("Enter Username Here");
        userToSearch.setMaximumSize(new Dimension(300, 40));
        userToSearch.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchPanel.add(userToSearch);

        JButton searchButton = new JButton("Search");
        searchButton.setMaximumSize(new Dimension(300, 40));
        searchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchButton.addActionListener(new SearchLoginListener());
        searchPanel.add(searchButton);

        //DISPLAY PANEL

        displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
        displayPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

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

        //Final layout
        add(searchPanel, BorderLayout.NORTH);
        add(displayPanel, BorderLayout.CENTER);
        add(navBar, BorderLayout.SOUTH);
    }

    private class SearchLoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> {

                writer.println(userToSearch.getText());

                Object searchedProfile = null;

                try {
                    searchedProfile = objectReader.readObject();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }

                if (searchedProfile instanceof String) {
                    JOptionPane.showMessageDialog(SearchScreen.this,
                            "User doesn't exist",
                            "Search Error",
                            JOptionPane.ERROR_MESSAGE);
                } else if (searchedProfile instanceof UserProfile) {

                    System.out.println(user.getUsername());

                    UserProfile searchedUser = (UserProfile) searchedProfile;

                    displayUserInfo(searchedUser);

                } else {
                    JOptionPane.showMessageDialog(SearchScreen.this,
                            "Unable to find user",
                            "Search Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }).start();
        }
    }

    private void displayUserInfo(UserProfile user) {
        // Clear previous results
        displayPanel.removeAll();

        JLabel userNameLabel = new JLabel(user.getUsername());
        userNameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        userNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel friendsLabel = new JLabel("Friends: " + user.getFriends().size());
        friendsLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        friendsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel postsLabel = new JLabel("Posts: " + user.getUserPosts().size());
        postsLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        postsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add new labels to display user info
        displayPanel.add(userNameLabel);
        displayPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacer
        displayPanel.add(friendsLabel);
        displayPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacer
        displayPanel.add(postsLabel);

        // Refresh the panel to show updated results
        displayPanel.revalidate();
        displayPanel.repaint();
    }

    private JPanel createPostPanel(UserProfile user) {

        JPanel postPanel = new JPanel();
        postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
        postPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        postPanel.setBackground(new Color(230, 230, 230));

        JLabel imageLabel = new JLabel();

        // Caption Label
        JLabel captionLabel = new JLabel();
        captionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        captionLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        ArrayList<NewsPost> availablePosts = this.user.getUserPosts();

        if (!availablePosts.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(availablePosts.size()); //Generates a number between [0,size)
            NewsPost randomFriendPost = availablePosts.get(randomIndex);

            // Load original image
            ImageIcon originalIcon = new ImageIcon(randomFriendPost.getImagePath());
            Image originalImage = originalIcon.getImage();

            //Resize
            Image resizedImage = originalImage.getScaledInstance(300, 150, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);

            imageLabel = new JLabel(resizedIcon);
            imageLabel.setPreferredSize(new Dimension(300, 150));
            imageLabel.setOpaque(true);
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            // Caption Label
            captionLabel.setText("@" + randomFriendPost.getAuthor() + " - " + randomFriendPost.getCaption());
        } else {
            System.out.println("No posts available");
        }

        postPanel.add(imageLabel);
        postPanel.add(captionLabel);


        return postPanel;
    }
}