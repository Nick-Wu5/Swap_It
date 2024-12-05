import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class HomeScreen extends JPanel {

    private AppGUI appGUI;
    private JPanel postsPanel;
    private UserProfile user;

    public HomeScreen(BufferedReader reader, ObjectInputStream objectReader, PrintWriter writer, AppGUI appGUI, UserProfile user) {

        appGUI.initializeOtherPages(user, reader, objectReader, writer);

        this.appGUI = appGUI;
        this.user = user;

        setLayout(new BorderLayout());

        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));

        // Header section
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS)); // Horizontal arrangement

        // Title label
        JLabel titleLabel = new JLabel("Swap_It");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 35));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel usernameLabel = new JLabel("Welcome @" + user.getUsername());
        usernameLabel.setForeground(Color.BLACK);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add labels to the header
        headerPanel.add(titleLabel); // Add the title panel first
        headerPanel.add(Box.createHorizontalGlue()); // Add space between title and username
        headerPanel.add(usernameLabel); // Add the username panel

        // Posts Section
        postsPanel = new JPanel();
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));

        // Add sample posts
        postsPanel.add(createPostPanel());
        postsPanel.add(Box.createVerticalStrut(10));
        postsPanel.add(createPostPanel());// Add 10px vertical space

        // "Load More Posts" Button
        JButton loadMoreButton = new JButton("SWAP IT");
        loadMoreButton.setBackground(new Color(255, 178, 102));
        loadMoreButton.setForeground(Color.BLACK);
        loadMoreButton.setFocusPainted(false);
        loadMoreButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button horizontally
        loadMoreButton.addActionListener(new loadMoreActionListener());

        // Add components to the main panel
        mainContentPanel.add(headerPanel); // Header
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacer
        mainContentPanel.add(postsPanel); // posts section
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacer
        mainContentPanel.add(loadMoreButton); // "Load more posts" button

        add(mainContentPanel, BorderLayout.CENTER);
        add(createNavBar(), BorderLayout.SOUTH); // Navigation bar
    }

    // Helper Method to Create a Post Panel
    private JPanel createPostPanel() {

        JPanel postPanel = new JPanel();
        postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
        postPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        postPanel.setBackground(new Color(230, 230, 230));

        JLabel imageLabel = new JLabel();

        // Caption Label
        JLabel captionLabel = new JLabel();
        captionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        captionLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        ArrayList<NewsPost> availablePosts = this.generateAvailablePosts();

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

    private ArrayList<NewsPost> generateAvailablePosts() {

        ArrayList<NewsPost> availablePosts = new ArrayList<>();
        ArrayList<String> userFriends = user.getFriends();

        try (BufferedReader br = new BufferedReader(new FileReader("newsPosts.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                for (String friend : userFriends) {
                    if (line.contains(friend)) {

                        String[] postInfo = line.split(",");

                        NewsPost tempPost = new NewsPost();
                        tempPost.setAuthor(postInfo[0]);
                        tempPost.setCaption(postInfo[1]);
                        tempPost.setImagePath(postInfo[2]);
                        tempPost.setDate(postInfo[3]);
                        tempPost.setUpvotes(Integer.parseInt(postInfo[4]));
                        tempPost.setDownvotes(Integer.parseInt(postInfo[5]));
                        tempPost.setComments(NewsPost.findComments(postInfo[1]));

                        availablePosts.add(tempPost);

                    }
                }

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return availablePosts;
    }

    private class loadMoreActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> {
                SwingUtilities.invokeLater(() -> {
                    // Clear existing posts
                    postsPanel.removeAll();

                    // Add new posts
                    postsPanel.add(createPostPanel());
                    postsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing
                    postsPanel.add(createPostPanel());

                    // Refresh the UI to reflect the changes
                    postsPanel.revalidate();
                    postsPanel.repaint();
                });
            }).start();
        }
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
