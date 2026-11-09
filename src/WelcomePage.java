import java.awt.*;
import javax.swing.*;

public class WelcomePage extends JFrame {

    public WelcomePage() {
        setTitle("CABBY - Online Cab Booking System");
        setSize(800, 560);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel bg = Theme.gradient();
        bg.setLayout(new GridBagLayout());

        JPanel box = new JPanel();
        box.setOpaque(false);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));

        JLabel icon = new JLabel("🚖");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 90));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("CABBY");
        title.setFont(Theme.font(Font.BOLD, 64));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tag = new JLabel("TAP. RIDE. SMILE.");
        tag.setFont(Theme.font(Font.BOLD, 18));
        tag.setForeground(Theme.GOLD);
        tag.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel features = new JLabel("Book in Seconds   •   Pay Fair   •   Ride Safe");
        features.setFont(Theme.font(Font.PLAIN, 15));
        features.setForeground(Color.WHITE);
        features.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton start = Theme.button("GET STARTED  →", Theme.GOLD, Theme.DEEP);
        start.setMaximumSize(new Dimension(240, 52));
        start.setPreferredSize(new Dimension(240, 52));
        start.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel footer = new JLabel("Online Cab Booking System  |  Java Swing + MySQL");
        footer.setFont(Theme.font(Font.PLAIN, 12));
        footer.setForeground(new Color(200, 190, 240));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);

        box.add(icon);
        box.add(title);
        box.add(Box.createVerticalStrut(6));
        box.add(tag);
        box.add(Box.createVerticalStrut(30));
        box.add(features);
        box.add(Box.createVerticalStrut(40));
        box.add(start);
        box.add(Box.createVerticalStrut(60));
        box.add(footer);

        bg.add(box);
        add(bg);

        start.addActionListener(e -> {
            dispose();
            new LoginWindow().setVisible(true);
        });
    }

    public static void main(String[] args) {
        new WelcomePage().setVisible(true);
    }
}