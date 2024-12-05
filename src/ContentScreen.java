import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ContentScreen extends JPanel implements ContentScreenInterface {

    private AppGUI appGUI;
    private UserProfile user;
    private ObjectInputStream objectReader;
    private BufferedReader reader;
    private PrintWriter writer;

    public ContentScreen(BufferedReader reader, ObjectInputStream objectReader, PrintWriter writer, AppGUI gui, UserProfile userProfile) {

        this.appGUI = gui;
        this.user = userProfile;
        this.objectReader = objectReader;
        this.writer = writer;
        this.reader = reader;

        setLayout(new BorderLayout());
        JLabel label = new JLabel("Content Screen", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));

        // CardLayout to swap panels
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        // Panels for each functionality
        JPanel menuPanel = createMenuPanel(writer, mainPanel, cardLayout);
        JPanel createPostPanel = createCreatePostPanel(writer, mainPanel, cardLayout);
        JPanel deletePostPanel = createDeletePostPanel(writer, mainPanel, cardLayout);
        JPanel deleteCommentPanel = createDeleteCommentPanel(writer, mainPanel, cardLayout);

        // Add panels to CardLayout
        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(createPostPanel, "Create Post");
        mainPanel.add(deletePostPanel, "Delete Post");
        mainPanel.add(deleteCommentPanel, "Delete Comment");

        // Show the main menu panel initially
        cardLayout.show(mainPanel, "Menu");

        //NAV BAR

        JPanel navBar = new JPanel();
        navBar.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
        navBar.setBackground(Color.WHITE);

        String[] pageNames = {"SearchScreen", "HomeScreen", "FriendScreen", "ContentScreen", "ProfileScreen"};

        for (int i = 0; i < pageNames.length; i++) {
            JButton navButton = new JButton(pageNames[i]);
            navButton.setFocusPainted(false);
            navButton.setContentAreaFilled(false);
            navButton.setBorder(BorderFactory.createEmptyBorder());
            navButton.setFont(new Font("Arial", Font.PLAIN, 5));

            // Add ActionListener to navigate to the corresponding page
            final String pageName = pageNames[i];
            navButton.addActionListener(e -> appGUI.showPage(pageName));

            navBar.add(navButton);
        }

        add(label, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(createNavBar(), BorderLayout.SOUTH);

    }

    // Create Menu Panel
    private static JPanel createMenuPanel(PrintWriter writer, JPanel mainPanel, CardLayout cardLayout) {

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        JButton createPostButton = new JButton("Create Post");
        createPostButton.setAlignmentX(CENTER_ALIGNMENT);

        JButton deletePostButton = new JButton("Delete Post");
        deletePostButton.setAlignmentX(CENTER_ALIGNMENT);

        JButton deleteCommentButton = new JButton("Delete Comment");
        deleteCommentButton.setAlignmentX(CENTER_ALIGNMENT);

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
    private static JPanel createCreatePostPanel(PrintWriter writer, JPanel mainPanel, CardLayout cardLayout) {

        JPanel createPostPanel = new JPanel();
        createPostPanel.setLayout(new BoxLayout(createPostPanel, BoxLayout.Y_AXIS));

        // Caption field
        JTextField captionField = new JTextField("Caption Your Post!");
        captionField.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        captionField.setMaximumSize(new Dimension(300, 40));

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
        createPostPanel.add(Box.createVerticalStrut(20)); // Spacer
        createPostPanel.add(createButton);
        createPostPanel.add(Box.createVerticalGlue()); // Push everything upward

        return createPostPanel;
    }

    // Delete Post Panel
    private static JPanel createDeletePostPanel(PrintWriter writer, JPanel mainPanel, CardLayout cardLayout) {
        JPanel deletePostPanel = new JPanel(new GridLayout(3, 1));
        JLabel captionLabel = new JLabel("Enter Caption of Post to Delete:");
        JTextField captionField = new JTextField();

        JButton deleteButton = new JButton("Delete");
        JButton backButton = new JButton("Back");

        deleteButton.addActionListener(e -> {
            String captionToDelete = captionField.getText();
            // Simulate sending to backend
            JOptionPane.showMessageDialog(deletePostPanel, "Post Deleted: " + captionToDelete);
            captionField.setText("");
            cardLayout.show(mainPanel, "Menu");
        });

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        deletePostPanel.add(captionLabel);
        deletePostPanel.add(captionField);
        deletePostPanel.add(deleteButton);
        deletePostPanel.add(backButton);

        return deletePostPanel;
    }

    // Delete Comment Panel
    private static JPanel createDeleteCommentPanel(PrintWriter writer, JPanel mainPanel, CardLayout cardLayout) {
        JPanel deleteCommentPanel = new JPanel(new BorderLayout());
        JLabel commentLabel = new JLabel("Select a comment to delete:");

        // Dummy comments (replace with data from backend)
        ArrayList<String> comments = new ArrayList<>();
        comments.add("Comment 1: This is a comment.");
        comments.add("Comment 2: Another comment.");
        comments.add("Comment 3: Yet another comment.");

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String comment : comments) {
            listModel.addElement(comment);
        }

        JList<String> commentList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(commentList);

        JButton deleteButton = new JButton("Delete");
        JButton backButton = new JButton("Back");

        deleteButton.addActionListener(e -> {
            int selectedIndex = commentList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedComment = comments.get(selectedIndex);
                // Simulate sending to backend
                JOptionPane.showMessageDialog(deleteCommentPanel, "Comment Deleted: " + selectedComment);
                comments.remove(selectedIndex);
                listModel.remove(selectedIndex);
            } else {
                JOptionPane.showMessageDialog(deleteCommentPanel, "Please select a comment to delete.");
            }
        });

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        deleteCommentPanel.add(commentLabel, BorderLayout.NORTH);
        deleteCommentPanel.add(scrollPane, BorderLayout.CENTER);
        deleteCommentPanel.add(deleteButton, BorderLayout.SOUTH);
        deleteCommentPanel.add(backButton, BorderLayout.SOUTH);

        return deleteCommentPanel;
    }

    // Function to update the image preview
    private static void updateImagePreview(JComboBox<String> imageDropdown, JLabel imagePreview) {
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
}
