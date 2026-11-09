import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame {

    JTextField emailField = new JTextField();
    JPasswordField passField = new JPasswordField();

    public LoginWindow() {
        setTitle("CABBY - Login");
        setSize(860, 540);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));      // two halves

        // LEFT HALF: brand
        JPanel left = Theme.gradient();
        left.setLayout(new GridBagLayout());
        JPanel brand = new JPanel(new GridLayout(0, 1, 0, 6));
        brand.setOpaque(false);

        JLabel icon = new JLabel("🚖", JLabel.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        JLabel name = new JLabel("CABBY", JLabel.CENTER);
        name.setFont(Theme.font(Font.BOLD, 48));
        name.setForeground(Color.WHITE);
        JLabel tag = new JLabel("Book a ride in seconds", JLabel.CENTER);
        tag.setFont(Theme.font(Font.PLAIN, 17));
        tag.setForeground(Theme.GOLD);
        brand.add(icon);
        brand.add(name);
        brand.add(tag);
        left.add(brand);

        // RIGHT HALF: login form
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(Color.WHITE);
        JPanel form = new JPanel(new GridLayout(0, 1, 0, 8));
        form.setOpaque(false);
        form.setPreferredSize(new Dimension(320, 400));

        JLabel heading = new JLabel("Welcome Back");
        heading.setFont(Theme.font(Font.BOLD, 30));
        heading.setForeground(Theme.DEEP);
        JLabel sub = new JLabel("Login to book your ride");
        sub.setFont(Theme.font(Font.PLAIN, 14));
        sub.setForeground(Theme.GRAY);
        JLabel newUser = new JLabel("New to CABBY?");
        newUser.setFont(Theme.font(Font.PLAIN, 13));
        newUser.setForeground(Theme.GRAY);

        Theme.field(emailField);
        Theme.field(passField);
        JButton loginBtn = Theme.button("LOGIN", Theme.PURPLE, Color.WHITE);
        JButton registerBtn = Theme.button("CREATE ACCOUNT", Theme.SOFT, Theme.PURPLE);

        form.add(heading);
        form.add(sub);
        form.add(Theme.label("Email"));
        form.add(emailField);
        form.add(Theme.label("Password"));
        form.add(passField);
        form.add(loginBtn);
        form.add(newUser);
        form.add(registerBtn);
        right.add(form);

        add(left);
        add(right);

        getRootPane().setDefaultButton(loginBtn);   // Enter key = Login
        loginBtn.addActionListener(e -> doLogin());
        registerBtn.addActionListener(e -> {
            dispose();
            new RegisterWindow().setVisible(true);
        });
    }

    void doLogin() {
        String email = emailField.getText().trim();
        String password = new String(passField.getPassword());
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter email and password");
            return;
        }
        int userId = UserService.login(email, password);

        if (userId == -1) {
            JOptionPane.showMessageDialog(this, "Invalid email or password!");
        } else {
            String name = UserService.getName(userId);
            dispose();
            new Dashboard(userId, name).setVisible(true);
        }
    }

    public static void main(String[] args) {
        new LoginWindow().setVisible(true);
    }
}