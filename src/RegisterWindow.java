import javax.swing.*;
import java.awt.*;

public class RegisterWindow extends JFrame {

    JTextField nameField = new JTextField();
    JTextField emailField = new JTextField();
    JTextField phoneField = new JTextField();
    JPasswordField passField = new JPasswordField();

    public RegisterWindow() {
        setTitle("CABBY - Register");
        setSize(520, 640);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(Theme.header("Create Account", "Join CABBY and book your first ride"), BorderLayout.NORTH);

        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(Color.WHITE);
        JPanel form = new JPanel(new GridLayout(0, 1, 0, 6));
        form.setOpaque(false);
        form.setPreferredSize(new Dimension(360, 440));

        Theme.field(nameField);
        Theme.field(emailField);
        Theme.field(phoneField);
        Theme.field(passField);
        JButton registerBtn = Theme.button("REGISTER", Theme.PURPLE, Color.WHITE);
        JButton backBtn = Theme.button("BACK TO LOGIN", Theme.SOFT, Theme.PURPLE);

        form.add(Theme.label("Full Name"));
        form.add(nameField);
        form.add(Theme.label("Email"));
        form.add(emailField);
        form.add(Theme.label("Phone"));
        form.add(phoneField);
        form.add(Theme.label("Password"));
        form.add(passField);
        form.add(registerBtn);
        form.add(backBtn);
        body.add(form);
        add(body, BorderLayout.CENTER);

        registerBtn.addActionListener(e -> doRegister());
        backBtn.addActionListener(e -> {
            dispose();
            new LoginWindow().setVisible(true);
        });
    }

    void doRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = new String(passField.getPassword());

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all the fields");
            return;
        }

        if (UserService.register(name, email, password, phone)) {
            JOptionPane.showMessageDialog(this, "Registration successful! Please login.");
            dispose();
            new LoginWindow().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed! Email may already exist.");
        }
    }
}