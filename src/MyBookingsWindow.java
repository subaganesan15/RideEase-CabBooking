import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MyBookingsWindow extends JFrame {

    public MyBookingsWindow(int userId) {
        setTitle("CABBY - My Bookings");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(Theme.header("My Bookings", "All your trips in one place"), BorderLayout.NORTH);

        String[] columns = {"Booking ID", "Cab Number", "Pickup", "Drop", "KM", "Fare", "Status", "Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        Theme.table(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.SOFT));
        scroll.getViewport().setBackground(Color.WHITE);

        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Theme.PAGE);
        body.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        body.add(scroll);
        add(body, BorderLayout.CENTER);

        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT b.booking_id, c.cab_number, b.pickup, b.drop_location, "
                       + "b.distance_km, b.fare, b.status, b.booking_date "
                       + "FROM bookings b JOIN cabs c ON b.cab_id = c.cab_id "
                       + "WHERE b.user_id = ? ORDER BY b.booking_id DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                    rs.getInt(5), rs.getDouble(6), rs.getString(7), rs.getString(8)
                });
            }
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }

        setVisible(true);
    }
}