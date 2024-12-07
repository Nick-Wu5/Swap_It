import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class SearchScreen extends JPanel implements SearchScreenInterface {

    private PrintWriter writer;  // Used for sending data to the server
    private ObjectInputStream objectReader;
    private AppGUI appGUI;
    private UserProfile user;
    JTextField userToSearch;
    JPanel displayPanel;
    JScrollPane displayScrollPane;

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

        //SEARCHED USER DISPLAY PANEL

        displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
        displayPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Final layout
        add(searchPanel, BorderLayout.NORTH);
        add(displayPanel, BorderLayout.CENTER);
        add(createNavBar(), BorderLayout.SOUTH);
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

                    UserProfile searchedUser = (UserProfile) searchedProfile;

                    displayUserInfo(searchedUser);

                    JPanel postsPanel = new JPanel();
                    postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));

                    for (NewsPost post : searchedUser.getUserPosts()) {
                        postsPanel.add(searchPostPanel(post));
                    }

                    displayScrollPane = new JScrollPane(postsPanel);
                    displayScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                    displayScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    displayScrollPane.setMinimumSize(new Dimension(displayPanel.getWidth(), 300));
                    displayScrollPane.setMaximumSize(new Dimension(displayPanel.getWidth(), 300));

                    displayPanel.add(displayScrollPane, BorderLayout.CENTER);
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

    private void displayUserInfo(UserProfile user) {
        // Clear previous results
        displayPanel.removeAll();

        JLabel userNameLabel = new JLabel("Profile: " + user.getUsername());
        userNameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        userNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel userFriendsPostsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));

        JLabel friendsLabel = new JLabel("Friends: " + user.getFriends().size());
        friendsLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        friendsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel postsLabel = new JLabel("Posts: " + user.getUserPosts().size());
        postsLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        postsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        userFriendsPostsPanel.add(friendsLabel);
        userFriendsPostsPanel.add(postsLabel);

        // Add new labels to display user info
        displayPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacer
        displayPanel.add(userNameLabel);
        displayPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacer
        displayPanel.add(userFriendsPostsPanel);
        displayPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacer

        // Refresh the panel to show updated results
        displayPanel.revalidate();
        displayPanel.repaint();
    }

    private JPanel searchPostPanel (NewsPost post) {

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

        JPanel commentPanel = new JPanel();
        commentPanel.setLayout(new FlowLayout());
        commentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton commentButton = new JButton("Comment");
        commentPanel.add(commentButton);

        commentButton.addActionListener(e -> {
            String commentText = JOptionPane.showInputDialog(postPanel, "Enter your comment:", "Comment", JOptionPane.PLAIN_MESSAGE);
            if (commentText != null && !commentText.trim().isEmpty()) {
                writer.println("COMMENT"); // Command to indicate a comment is being sent
                writer.println(post.getCaption()); // Send the post caption
                writer.println(commentText); // Send the comment text
                writer.flush();
            }
            writer.println("1");
        });

        JButton viewCommentsButton = new JButton("View Comments");
        commentPanel.add(viewCommentsButton);

        viewCommentsButton.addActionListener(e -> {
            // Fetch comments for the post
            writer.println("VIEW");

            ArrayList<NewsComment> comments = NewsPost.findComments(post.getCaption());

            // Create a dialog to display comments
            JDialog commentsDialog = new JDialog((Frame) null, "Comments", true);
            commentsDialog.setSize(200, 300);
            commentsDialog.setLocationRelativeTo(displayPanel); // Center on screen
            commentsDialog.setLayout(new BorderLayout());

            // Main panel to hold all comments
            JPanel commentsPanel = new JPanel();
            commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.Y_AXIS));
            commentsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Add each comment to the panel
            if (comments.isEmpty()) {
                JLabel noCommentsLabel = new JLabel("No comments yet.");
                noCommentsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                commentsPanel.add(noCommentsLabel);
            } else {
                for (NewsComment comment : comments) {
                    JPanel postCommentPanel = createCommentPanel(comment);
                    commentsPanel.add(postCommentPanel);
                    commentsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between comments
                }
            }

            // Wrap the comments panel in a scroll pane
            JScrollPane scrollPane = new JScrollPane(commentsPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            // Add the scroll pane to the dialog
            commentsDialog.add(scrollPane, BorderLayout.CENTER);

            // Show the dialog
            commentsDialog.setVisible(true);
            writer.println("1");
        });

        postPanel.add(imageLabel);
        postPanel.add(captionLabel);
        postPanel.add(commentPanel);

        return postPanel;
    }

    private JPanel createCommentPanel(NewsComment comment) {
        JPanel commentPanel = new JPanel();
        commentPanel.setLayout(new BoxLayout(commentPanel, BoxLayout.Y_AXIS));
        commentPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        commentPanel.setBackground(new Color(245, 245, 245));
        commentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Author label
        JLabel authorLabel = new JLabel("Author: " + comment.getAuthor());
        authorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        authorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Comment text label
        JLabel contentLabel = new JLabel(comment.getContent());
        contentLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        contentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Likes and dislikes label
        JLabel likesLabel = new JLabel("Upvotes: " + comment.getUpvotes() + " | Downvotes: " +
                comment.getDownvotes());
        likesLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        likesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add labels to the panel
        commentPanel.add(authorLabel);
        commentPanel.add(contentLabel);
        commentPanel.add(likesLabel);

        return commentPanel;
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