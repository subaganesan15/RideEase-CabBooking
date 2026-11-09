import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Dashboard extends JFrame {

    int userId;

    public Dashboard(int userId, String name) {
        this.userId = userId;

        setTitle("CABBY - Dashboard");
        setSize(940, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // LEFT SIDEBAR
        JPanel side = Theme.gradient();
        side.setPreferredSize(new Dimension(250, 0));
        side.setLayout(new BorderLayout());
        side.setBorder(BorderFactory.createEmptyBorder(30, 22, 30, 22));

        JPanel top = new JPanel(new GridLayout(0, 1, 0, 4));
        top.setOpaque(false);
        JLabel icon = new JLabel("🚖");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        JLabel brand = new JLabel("CABBY");
        brand.setFont(Theme.font(Font.BOLD, 32));
        brand.setForeground(Color.WHITE);
        JLabel as = new JLabel("Logged in as");
        as.setFont(Theme.font(Font.PLAIN, 12));
        as.setForeground(new Color(216, 200, 255));
        JLabel user = new JLabel(name);
        user.setFont(Theme.font(Font.BOLD, 20));
        user.setForeground(Theme.GOLD);
        top.add(icon);
        top.add(brand);
        top.add(as);
        top.add(user);

        JButton logoutBtn = Theme.button("LOGOUT", Theme.GOLD, Theme.DEEP);
        logoutBtn.setPreferredSize(new Dimension(0, 46));
        side.add(top, BorderLayout.NORTH);
        side.add(logoutBtn, BorderLayout.SOUTH);

        // RIGHT MAIN AREA
        JPanel main = new JPanel(new BorderLayout(0, 25));
        main.setBackground(Theme.PAGE);
        main.setBorder(BorderFactory.createEmptyBorder(35, 35, 35, 35));

        JPanel greet = new JPanel(new GridLayout(0, 1, 0, 4));
        greet.setOpaque(false);
        JLabel hello = new JLabel("Hello, " + name + " 👋");
        hello.setFont(Theme.font(Font.BOLD, 30));
        hello.setForeground(Theme.TEXT);
        JLabel ask = new JLabel("Where would you like to go today?");
        ask.setFont(Theme.font(Font.PLAIN, 15));
        ask.setForeground(Theme.GRAY);
        greet.add(hello);
        greet.add(ask);
        main.add(greet, BorderLayout.NORTH);

        JPanel tiles = new JPanel(new GridLayout(2, 2, 20, 20));
        tiles.setOpaque(false);
        tiles.add(tile("🚕", "Available Cabs", "See all cabs ready to ride", () -> new CabsWindow()));
        tiles.add(tile("📍", "Book a Cab", "Pick a cab and get the fare", () -> new BookingWindow(userId)));
        tiles.add(tile("📋", "My Bookings", "View all your trips", () -> new MyBookingsWindow(userId)));
        tiles.add(tile("❌", "Cancel Booking", "Cancel a booking by its ID", () -> CancelBooking.start(this, userId)));
        main.add(tiles, BorderLayout.CENTER);

        add(side, BorderLayout.WEST);
        add(main, BorderLayout.CENTER);

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginWindow().setVisible(true);
        });
    }

    // One big clickable card
    JPanel tile(String icon, String title, String desc, Runnable action) {
        JPanel p = new JPanel(new GridLayout(0, 1, 0, 4));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.SOFT, 2),
                BorderFactory.createEmptyBorder(20, 22, 20, 22)));
        p.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel i = new JLabel(icon);
        i.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        JLabel t = new JLabel(title);
        t.setFont(Theme.font(Font.BOLD, 20));
        t.setForeground(Theme.DEEP);
        JLabel d = new JLabel(desc);
        d.setFont(Theme.font(Font.PLAIN, 13));
        d.setForeground(Theme.GRAY);
        p.add(i);
        p.add(t);
        p.add(d);

        p.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { action.run(); }
            public void mouseEntered(MouseEvent e) { p.setBackground(Theme.SOFT); }
            public void mouseExited(MouseEvent e) { p.setBackground(Color.WHITE); }
        });
        return p;
    }
}