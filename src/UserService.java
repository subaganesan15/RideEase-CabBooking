import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserService {

    // Saves a new user. Returns true if saved, false if failed
    public static boolean register(String name, String email, String password, String phone) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO users (name, email, password, phone) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, phone);
            ps.executeUpdate();
            con.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Checks email and password. Returns user_id if correct, -1 if wrong
    public static int login(String email, String password) {
        int userId = -1;
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT user_id FROM users WHERE email = ? AND password = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("user_id");
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return userId;
    }

    // Gets the user's name for the welcome message
    public static String getName(int userId) {
        String name = "";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT name FROM users WHERE user_id = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                name = rs.getString("name");
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return name;
    }
}