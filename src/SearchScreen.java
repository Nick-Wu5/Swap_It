import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SearchScreen extends JPanel {

    private PrintWriter writer;  // Used for sending data to the server
    private ObjectInputStream objectReader;
    private AppGUI appGUI;
    private UserProfile user;
    JTextField userToSearch;

    public SearchScreen(PrintWriter writer, ObjectInputStream objectReader, AppGUI gui, UserProfile userProfile) {

        this.appGUI = gui;
        this.user = userProfile;
        this.objectReader = objectReader;

        setLayout(new BorderLayout());
        JLabel label = new JLabel("Search Screen", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);

        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainContentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Search Bar
        userToSearch = new JTextField("Enter Username Here");
        userToSearch.setMaximumSize(new Dimension(300, 40));
        userToSearch.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainContentPanel.add(userToSearch);

        JButton searchButton = new JButton("Search");
        searchButton.setMaximumSize(new Dimension(300, 40));
        searchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchButton.addActionListener(new SearchLoginListener());
        mainContentPanel.add(searchButton);

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
        add(mainContentPanel, BorderLayout.CENTER);
        add(navBar, BorderLayout.SOUTH);
    }

    private class SearchLoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> {

                writer.println(userToSearch.getText());

                Object userProfile = null;

                try {
                    userProfile = objectReader.readObject();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }

                if (userProfile instanceof String) {
                    JOptionPane.showMessageDialog(SearchScreen.this,
                            "User doesn't exist",
                            "Search Error",
                            JOptionPane.ERROR_MESSAGE);
                } else if (userProfile instanceof User) {
                    User user = (User) userProfile;

                    System.out.println("\nUser Exists!");
                    System.out.println("Username: " + user.getUsername());
                    System.out.println("Friends: " + user.getFriends().size());
                    System.out.println("Posts: " + user.getUserPosts().size());


                } else {
                    JOptionPane.showMessageDialog(SearchScreen.this,
                            "Unable to find user",
                            "Search Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }).start();
        }
    }
}