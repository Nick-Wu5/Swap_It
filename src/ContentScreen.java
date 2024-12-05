import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ContentScreen extends JPanel implements ContentScreenInterface {

    private AppGUI appGUI;
    private UserProfile user;
    private ObjectInputStream objectReader;
    private BufferedReader reader;
    private PrintWriter writer;
    private Object userPosts;

    public ContentScreen(BufferedReader reader, PrintWriter writer, ObjectInputStream objectReader, AppGUI gui,
                         UserProfile userProfile) {

        this.appGUI = gui;
        this.user = userProfile;
        this.objectReader = objectReader;
        this.writer = writer;
        this.reader = reader;

        setLayout(new BorderLayout());
        JLabel label = new JLabel("Content", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setBorder(new EmptyBorder(30, 0, 0, 0));
        label.setForeground(new Color(255, 178, 102));

        // CardLayout to swap panels
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        // Panels for each functionality
        JPanel menuPanel = createMenuPanel(objectReader, writer, mainPanel, cardLayout);
        JPanel createPostPanel = createCreatePostPanel(writer, mainPanel, cardLayout);
        JPanel deletePostPanel = createDeletePostPanel (mainPanel, cardLayout);
        JPanel deleteCommentPanel = createDeleteCommentPanel(writer, mainPanel, cardLayout);

        // Add panels to CardLayout
        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(createPostPanel, "Create Post");
        mainPanel.add(deletePostPanel, "Delete Post");
        mainPanel.add(deleteCommentPanel, "Delete Comment");

        // Show the main menu panel initially
        cardLayout.show(mainPanel, "Menu");

        add(label, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(createNavBar(), BorderLayout.SOUTH);

    }

    // Create Menu Panel
    private static JPanel createMenuPanel(ObjectInputStream objectReader, PrintWriter writer, JPanel mainPanel, CardLayout cardLayout) {

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        Dimension buttonSize = new Dimension(200, 50);

        JButton createPostButton = new JButton("Create Post");
        createPostButton.setAlignmentX(CENTER_ALIGNMENT);
        createPostButton.setPreferredSize(buttonSize);
        createPostButton.setMaximumSize(buttonSize);

        JButton deletePostButton = new JButton("Delete Post");
        deletePostButton.setAlignmentX(CENTER_ALIGNMENT);
        deletePostButton.setPreferredSize(buttonSize);
        deletePostButton.setMaximumSize(buttonSize);

        JButton deleteCommentButton = new JButton("Delete Comment");
        deleteCommentButton.setAlignmentX(CENTER_ALIGNMENT);
        deleteCommentButton.setPreferredSize(buttonSize);
        deleteCommentButton.setMaximumSize(buttonSize);

        // Button actions to swap panels
        createPostButton.addActionListener(e -> {

            cardLayout.show(mainPanel, "Create Post");
            writer.println("1");

        });

        deletePostButton.addActionListener(e -> {

            cardLayout.show(mainPanel, "Delete Post");
            writer.println("2");

        });

        deleteCommentButton.addActionListener(e -> {

            cardLayout.show(mainPanel, "Delete Comment");
            writer.println("3");

        });

        menuPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        menuPanel.add(createPostButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(deletePostButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(deleteCommentButton);

        return menuPanel;
    }

    // Create Post Panel
    private JPanel createCreatePostPanel(PrintWriter writer, JPanel mainPanel, CardLayout cardLayout) {

        JPanel createPostPanel = new JPanel();
        createPostPanel.setLayout(new BoxLayout(createPostPanel, BoxLayout.Y_AXIS));

        // Caption field
        JTextField captionField = new JTextField("Caption Your Post!");
        captionField.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        captionField.setMaximumSize(new Dimension(300, 40));

        JLabel commaWarningLabel = new JLabel("No commas please. Commas are scary");
        commaWarningLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Dropdown
        String[] imageOptions = {"Chris_dog1.png", "Chris_dog2.png", "Chris_dog3.png", "divya_scene1.png",
                "divya_scene2.png", "divya_scene3.png", "nick_dog.png", "Ramya_dog1.png", "Ramya_dog2.png"};
        JComboBox<String> imageDropdown = new JComboBox<>(imageOptions);
        imageDropdown.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        imageDropdown.setMaximumSize(new Dimension(300, 30));

        // Preview panel
        JLabel imagePreview = new JLabel();
        imagePreview.setHorizontalAlignment(JLabel.CENTER);
        imagePreview.setVerticalAlignment(JLabel.CENTER);
        imagePreview.setPreferredSize(new Dimension(300, 150));
        imagePreview.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        // Load and display the initial image
        updateImagePreview(imageDropdown, imagePreview);

        // Add ActionListener for dropdown
        imageDropdown.addActionListener(e -> updateImagePreview(imageDropdown, imagePreview));

        // Create button
        JButton createButton = new JButton("Create");
        createButton.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        createButton.addActionListener(e -> {
            String caption = captionField.getText();
            String selectedImage = "images/" + imageDropdown.getSelectedItem();
            captionField.setText("Caption Your Post!");
            writer.println(caption);
            writer.println(selectedImage);
            JOptionPane.showMessageDialog(createPostPanel, "Post Created!\nCaption: " + caption + "\nImage Path: " + selectedImage);
            cardLayout.show(mainPanel, "Menu");
            writer.println("2");
        });

        // Add components with spacing
        createPostPanel.add(Box.createVerticalStrut(20)); // Spacer
        createPostPanel.add(imagePreview);
        createPostPanel.add(Box.createVerticalStrut(10)); // Spacer
        createPostPanel.add(imageDropdown);
        createPostPanel.add(Box.createVerticalStrut(10)); // Spacer
        createPostPanel.add(captionField);
        createPostPanel.add(commaWarningLabel);
        createPostPanel.add(Box.createVerticalStrut(20)); // Spacer
        createPostPanel.add(createButton);
        createPostPanel.add(Box.createVerticalGlue()); // Push everything upward

        return createPostPanel;
    }

    // Delete Post Panel
    private JPanel createDeletePostPanel(JPanel mainPanel, CardLayout cardLayout) {

        JPanel deletePostPanel = new JPanel();
        deletePostPanel.setLayout(new BoxLayout(deletePostPanel, BoxLayout.Y_AXIS));

        ArrayList<String> captionsOfPosts = new ArrayList<>();
        ArrayList<NewsPost> userPosts = user.getUserPosts();

        for (NewsPost post : userPosts) {
            captionsOfPosts.add(post.getCaption());
        }

        // Initial dropdown data
        JComboBox<String> postDropdown = new JComboBox<>(captionsOfPosts.toArray(new String[0]));
        postDropdown.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        postDropdown.setMaximumSize(new Dimension(300, 30));

        JButton deleteButton = new JButton("Delete");
        deleteButton.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        deleteButton.addActionListener(e -> {
            String captionToDelete = (String) postDropdown.getSelectedItem();

            if (captionToDelete != null) {
                // Remove the post from userPosts, captions, dropdown if needed
                userPosts.removeIf(post -> post.getCaption().equals(captionToDelete));
                captionsOfPosts.remove(captionToDelete);
                postDropdown.setModel(new DefaultComboBoxModel<>(captionsOfPosts.toArray(new String[0])));

                writer.println(captionToDelete);
                writer.flush();

                // Inform the user
                JOptionPane.showMessageDialog(deletePostPanel, "Post Deleted: " + captionToDelete);

                // Optionally return to the menu
                cardLayout.show(mainPanel, "Menu");
            } else {
                JOptionPane.showMessageDialog(deletePostPanel, "No post selected to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        deletePostPanel.add(Box.createVerticalStrut(20)); // Spacer
        deletePostPanel.add(postDropdown);
        deletePostPanel.add(Box.createVerticalStrut(20)); // Spacing
        deletePostPanel.add(deleteButton);
        deletePostPanel.add(Box.createVerticalGlue()); // Push everything upward

        return deletePostPanel;
    }

    // Delete Comment Panel
    private JPanel createDeleteCommentPanel(PrintWriter writer, JPanel mainPanel, CardLayout cardLayout) {

        ArrayList<NewsComment> commentsOfUser = user.findCommentsForUser();

        JPanel deleteCommentPanel = new JPanel();
        deleteCommentPanel.setLayout(new BoxLayout(deleteCommentPanel, BoxLayout.Y_AXIS));

        JLabel commentLabel = new JLabel("Select one of your comments to delete:");
        commentLabel.setBorder(new EmptyBorder(20, 5, 0, 5));
        commentLabel.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        // Dummy comments (replace with data from backend)
        ArrayList<String> comments = new ArrayList<>();
        for (NewsComment comment : commentsOfUser) {
            comments.add(comment.getContent());
        }

        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (String comment : comments) {
            listModel.addElement(comment);
        }

        JList<String> commentList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(commentList);
        scrollPane.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(300, 120));

        JButton deleteButton = new JButton("Delete");
        deleteButton.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        deleteButton.addActionListener(e -> {

            int selectedIndex = commentList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedComment = comments.get(selectedIndex);
                // Simulate sending to backend
                JOptionPane.showMessageDialog(deleteCommentPanel, "Comment Deleted: " + selectedComment);
                comments.remove(selectedIndex);
                listModel.remove(selectedIndex);
                writer.println(selectedComment);
            } else {
                JOptionPane.showMessageDialog(deleteCommentPanel, "Please select a comment to delete.");
            }

            writer.println("2");
            cardLayout.show(mainPanel, "Menu");
        });

        deleteCommentPanel.add(commentLabel);
        deleteCommentPanel.add(Box.createVerticalStrut(20)); // Spacing
        deleteCommentPanel.add(scrollPane);
        deleteCommentPanel.add(Box.createVerticalStrut(20)); // Spacing
        deleteCommentPanel.add(deleteButton);

        return deleteCommentPanel;
    }

    // Function to update the image preview
    private void updateImagePreview(JComboBox<String> imageDropdown, JLabel imagePreview) {
        String selectedImage = (String) imageDropdown.getSelectedItem();
        if (selectedImage != null) {
            // Replace "path/to/images/" with the actual directory where your images are stored
            String imagePath = "images/" + selectedImage;
            ImageIcon imageIcon = new ImageIcon(imagePath);

            // Resize the image to fit the preview area
            Image scaledImage = imageIcon.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH);

            imagePreview.setIcon(new ImageIcon(scaledImage));
        } else {
            imagePreview.setIcon(null);
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

    private ArrayList<NewsComment> getCommentsFromServer() {

        Object comments = null;
        ArrayList<NewsComment> commentsList = new ArrayList<>();

        try {
            comments = objectReader.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (comments instanceof ArrayList) {
            for (Object comment : (ArrayList) comments) {
                commentsList.add((NewsComment) comment);
            }
        }

        return commentsList;

    }
}
