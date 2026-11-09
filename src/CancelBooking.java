import java.sql.*;
import javax.swing.*;

public class CancelBooking {

    public static void start(JFrame parent, int userId) {
        String input = JOptionPane.showInputDialog(parent, "Enter Booking ID to cancel:");
        if (input == null) return;   // user pressed Cancel

        try {
            int bookingId = Integer.parseInt(input.trim());
            Connection con = DBConnection.getConnection();

            // Find the booking (it must belong to this user)
            PreparedStatement ps = con.prepareStatement(
                "SELECT cab_id, status FROM bookings WHERE booking_id = ? AND user_id = ?");
            ps.setInt(1, bookingId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(parent, "Booking not found!");
            } else if (rs.getString("status").equals("Cancelled")) {
                JOptionPane.showMessageDialog(parent, "This booking is already cancelled.");
            } else {
                int cabId = rs.getInt("cab_id");

                // 1. Mark the booking as Cancelled
                PreparedStatement ps1 = con.prepareStatement(
                    "UPDATE bookings SET status = 'Cancelled' WHERE booking_id = ?");
                ps1.setInt(1, bookingId);
                ps1.executeUpdate();

                // 2. Make the cab Available again
                PreparedStatement ps2 = con.prepareStatement(
                    "UPDATE cabs SET status = 'Available' WHERE cab_id = ?");
                ps2.setInt(1, cabId);
                ps2.executeUpdate();

                JOptionPane.showMessageDialog(parent, "Booking " + bookingId + " cancelled successfully.");
            }
            con.close();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(parent, "Please enter a valid number.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Error: " + e.getMessage());
        }
    }
}
