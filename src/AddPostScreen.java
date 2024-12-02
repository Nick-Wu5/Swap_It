import javax.swing.*;
import java.awt.*;

public class AddPostScreen extends JPanel {

    private AppGUI appGUI;
    private UserProfile user;

    public AddPostScreen(AppGUI gui, UserProfile userProfile) {

        this.appGUI = gui;
        this.user = userProfile;

        setLayout(new BorderLayout());
        JLabel label = new JLabel("Add Post Screen", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);

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

        add(navBar, BorderLayout.SOUTH);
    }
}