import javax.swing.*;
import java.awt.*;

public class ContentScreen extends JPanel {

    private AppGUI appGUI;
    private UserProfile user;

    public ContentScreen(AppGUI gui, UserProfile userProfile) {

        this.appGUI = gui;
        this.user = userProfile;

        setLayout(new BorderLayout());
        JLabel label = new JLabel("Content Screen", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);

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

        add(navBar, BorderLayout.SOUTH);
    }
}