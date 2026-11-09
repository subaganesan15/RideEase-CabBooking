import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CabsWindow extends JFrame {

    public CabsWindow() {
        setTitle("CABBY - Available Cabs");
        setSize(820, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(Theme.header("Available Cabs", "Choose from our fleet of ready-to-ride cabs"), BorderLayout.NORTH);

        String[] columns = {"Cab ID", "Cab Number", "Type", "Rate per KM", "Driver"};
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
            String sql = "SELECT c.cab_id, c.cab_number, c.cab_type, c.rate_per_km, d.name "
                       + "FROM cabs c JOIN drivers d ON c.driver_id = d.driver_id "
                       + "WHERE c.status = 'Available'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getDouble(4), rs.getString(5)
                });
            }
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }

        setVisible(true);
    }
}