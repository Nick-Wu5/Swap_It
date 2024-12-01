import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Random;

public class HomeScreen extends JPanel {

    private AppGUI appGUI;

    public HomeScreen(PrintWriter writer, AppGUI appGUI, UserProfile user) {

        this.appGUI = appGUI;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //Header section
        JPanel headerPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Swap_It");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 35));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create an orange background panel for the username
        JPanel usernamePanel = new JPanel();
        usernamePanel.setBackground(new Color(255, 178, 102)); // Light orange
        usernamePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10)); // Align username label to the right

        JLabel usernameLabel = new JLabel("@" + user.getUsername());
        usernameLabel.setForeground(Color.BLACK);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernamePanel.add(usernameLabel); // Add username label to the orange panel

        //Add title and username to the header
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(usernamePanel, BorderLayout.CENTER);

        // Posts Section
        JPanel postsPanel = new JPanel();
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));
        postsPanel.setBackground(Color.WHITE);

        // Add sample posts
        for (int i = 0; i < 2; i++) {
            postsPanel.add(createPostPanel(user));
        }

        // "Load More Posts" Button
        JButton loadMoreButton = new JButton("Load more posts");
        loadMoreButton.setBackground(new Color(255, 178, 102));
        loadMoreButton.setForeground(Color.BLACK);
        loadMoreButton.setFocusPainted(false);
        loadMoreButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button horizontally

        // Navigation Bar
        JPanel navBar = new JPanel();
        navBar.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10)); // Centered navigation bar
        navBar.setBackground(Color.WHITE);

        String[] navIcons = {"🔍", "🏠", "➕", "👤"}; // Emoji placeholders for navigation icons
        for (String icon : navIcons) {
            JButton navButton = new JButton(icon);
            navButton.setFocusPainted(false);
            navButton.setContentAreaFilled(false);
            navButton.setBorder(BorderFactory.createEmptyBorder());
            navButton.setFont(new Font("Arial", Font.PLAIN, 20));
            navBar.add(navButton);
        }

        // Add components to the main panel
        add(headerPanel); // Header
        add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        add(new JScrollPane(postsPanel)); // Scrollable posts section
        add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        add(loadMoreButton); // "Load more posts" button
        add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        add(navBar); // Navigation bar
    }

    // Helper Method to Create a Post Panel
    private JPanel createPostPanel(UserProfile user) {

        JPanel postPanel = new JPanel();
        postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
        postPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        postPanel.setBackground(Color.WHITE);

        // Image Placeholder
        JLabel imagePlaceholder = new JLabel();
        imagePlaceholder.setPreferredSize(new Dimension(300, 150));
        imagePlaceholder.setOpaque(true);
        imagePlaceholder.setBackground(Color.LIGHT_GRAY);
        imagePlaceholder.setHorizontalAlignment(SwingConstants.CENTER);

        // Caption Label
        JLabel captionLabel = new JLabel();
        captionLabel.setHorizontalAlignment(SwingConstants.LEFT);
        captionLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        ArrayList<NewsPost> availablePosts = this.generateAvailablePosts(user);

        if (!availablePosts.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(availablePosts.size()); //Generates a number between [0,size)
            NewsPost randomFriendPost = availablePosts.get(randomIndex);
            // Image Placeholder
            imagePlaceholder.setText(randomFriendPost.getImagePath());
            // Caption Label
            captionLabel.setText("@" + randomFriendPost.getAuthor() + " - " + randomFriendPost.getCaption());
        } else {
            System.out.println("No posts available");
        }

        postPanel.add(imagePlaceholder);
        postPanel.add(captionLabel);

        return postPanel;
    }

    private ArrayList<NewsPost> generateAvailablePosts(UserProfile user) {

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
}
