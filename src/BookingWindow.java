import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class BookingWindow extends JFrame {

    int userId;
    JComboBox<String> cabBox = new JComboBox<>();
    JTextField pickupField = new JTextField();
    JTextField dropField = new JTextField();
    JTextField distanceField = new JTextField();
    JLabel fareLabel = new JLabel("Rs. 0.00", JLabel.CENTER);

    public BookingWindow(int userId) {
        this.userId = userId;

        setTitle("CABBY - Book a Cab");
        setSize(900, 580);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // LEFT: live fare panel
        JPanel left = Theme.gradient();
        left.setPreferredSize(new Dimension(300, 0));
        left.setLayout(new GridBagLayout());
        JPanel info = new JPanel(new GridLayout(0, 1, 0, 8));
        info.setOpaque(false);

        JLabel icon = new JLabel("💰", JLabel.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        JLabel small = new JLabel("ESTIMATED FARE", JLabel.CENTER);
        small.setFont(Theme.font(Font.BOLD, 14));
        small.setForeground(Theme.GOLD);
        fareLabel.setFont(Theme.font(Font.BOLD, 38));
        fareLabel.setForeground(Color.WHITE);
        JLabel hint = new JLabel("Fare = Distance x Rate per KM", JLabel.CENTER);
        hint.setFont(Theme.font(Font.PLAIN, 13));
        hint.setForeground(new Color(216, 200, 255));
        info.add(icon);
        info.add(small);
        info.add(fareLabel);
        info.add(hint);
        left.add(info);

        // RIGHT: booking form
        JPanel right = new JPanel(new BorderLayout(0, 10));
        right.setBackground(Color.WHITE);
        right.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        JLabel heading = new JLabel("Book Your Ride");
        heading.setFont(Theme.font(Font.BOLD, 28));
        heading.setForeground(Theme.DEEP);
        right.add(heading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0, 1, 0, 5));
        form.setOpaque(false);
        cabBox.setFont(Theme.font(Font.PLAIN, 14));
        cabBox.setBackground(Color.WHITE);
        Theme.field(pickupField);
        Theme.field(dropField);
        Theme.field(distanceField);

        JButton fareBtn = Theme.button("CALCULATE FARE", Theme.SOFT, Theme.PURPLE);
        JButton bookBtn = Theme.button("CONFIRM BOOKING", Theme.PURPLE, Color.WHITE);
        JPanel buttons = new JPanel(new GridLayout(1, 2, 12, 0));
        buttons.setOpaque(false);
        buttons.add(fareBtn);
        buttons.add(bookBtn);

        form.add(Theme.label("Select Cab"));
        form.add(cabBox);
        form.add(Theme.label("Pickup Location"));
        form.add(pickupField);
        form.add(Theme.label("Drop Location"));
        form.add(dropField);
        form.add(Theme.label("Distance (KM)"));
        form.add(distanceField);
        form.add(buttons);
        right.add(form, BorderLayout.CENTER);

        add(left, BorderLayout.WEST);
        add(right, BorderLayout.CENTER);

        loadCabs();
        fareBtn.addActionListener(e -> calculateFare());
        bookBtn.addActionListener(e -> confirmBooking());

        setVisible(true);
    }

    // Fill the drop-down list with available cabs
    void loadCabs() {
        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                "SELECT cab_id, cab_number, cab_type, rate_per_km FROM cabs WHERE status = 'Available'");
            while (rs.next()) {
                cabBox.addItem(rs.getInt(1) + " - " + rs.getString(2) + " - "
                        + rs.getString(3) + " - Rs." + rs.getDouble(4) + "/km");
            }
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // Fare = distance x rate per km. Returns -1 if something is wrong
    double calculateFare() {
        try {
            String selected = (String) cabBox.getSelectedItem();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "No cabs available!");
                return -1;
            }
            int cabId = Integer.parseInt(selected.split(" - ")[0]);
            int km = Integer.parseInt(distanceField.getText().trim());
            if (km <= 0) {
                JOptionPane.showMessageDialog(this, "Distance must be more than 0");
                return -1;
            }

            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT rate_per_km FROM cabs WHERE cab_id = ?");
            ps.setInt(1, cabId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            double fare = km * rs.getDouble(1);
            con.close();

            fareLabel.setText(String.format("Rs. %.2f", fare));
            return fare;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter distance as a number");
            return -1;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            return -1;
        }
    }

    // Save the booking in the database
    void confirmBooking() {
        String pickup = pickupField.getText().trim();
        String drop = dropField.getText().trim();
        if (pickup.isEmpty() || drop.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter pickup and drop location");
            return;
        }

        double fare = calculateFare();
        if (fare < 0) return;

        try {
            String selected = (String) cabBox.getSelectedItem();
            int cabId = Integer.parseInt(selected.split(" - ")[0]);
            String cabNo = selected.split(" - ")[1];
            int km = Integer.parseInt(distanceField.getText().trim());

            Connection con = DBConnection.getConnection();

            // 1. Insert the booking
            String sql = "INSERT INTO bookings (user_id, cab_id, pickup, drop_location, distance_km, fare) "
                       + "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, userId);
            ps.setInt(2, cabId);
            ps.setString(3, pickup);
            ps.setString(4, drop);
            ps.setInt(5, km);
            ps.setDouble(6, fare);
            ps.executeUpdate();

            // 2. Get the new booking ID
            ResultSet keys = ps.getGeneratedKeys();
            keys.next();
            int bookingId = keys.getInt(1);

            // 3. Mark the cab as Booked
            PreparedStatement ps2 = con.prepareStatement("UPDATE cabs SET status = 'Booked' WHERE cab_id = ?");
            ps2.setInt(1, cabId);
            ps2.executeUpdate();
            con.close();

            // 4. Show the receipt
            String receipt = "<html><body style='width:240px'>"
                + "<h2>Booking Confirmed</h2>"
                + "Booking ID: <b>" + bookingId + "</b><br>"
                + "Cab: <b>" + cabNo + "</b><br>"
                + "Pickup: <b>" + pickup + "</b><br>"
                + "Drop: <b>" + drop + "</b><br>"
                + "Distance: <b>" + km + " km</b><br>"
                + "Fare: <b>Rs. " + String.format("%.2f", fare) + "</b><br>"
                + "Status: <b>Booked</b>"
                + "</body></html>";
            JOptionPane.showMessageDialog(this, receipt, "Booking Confirmation", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Booking failed: " + e.getMessage());
        }
    }
}