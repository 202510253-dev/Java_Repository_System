import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Frame extends JFrame {

    JButton loginButton;
    JTextField usernameField;
    JTextField gmailField;
    JPasswordField passwordField;
    JLabel label_img, label_img2, label_img3, label, label2, label3;
    ImageIcon user, mail, pass;

    Frame() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);

        user = new ImageIcon(getClass().getResource("/user_icon.jpg"));
        pass = new ImageIcon(getClass().getResource("/pass_icon.jpg"));
        mail = new ImageIcon(getClass().getResource("/mail_icon.jpg"));

        label_img = new JLabel();
        label_img2 = new JLabel();
        label_img3 = new JLabel();
        label_img.setIcon(user);
        label_img2.setIcon(mail);
        label_img3.setIcon(pass);

        label = new JLabel("Username");
        label2 = new JLabel("Gmail");
        label3 = new JLabel("Password");

        usernameField = new JTextField();
        gmailField = new JTextField();
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");

        this.add(label_img);
        this.add(label_img2);
        this.add(label_img3);
        this.add(label);
        this.add(label2);
        this.add(label3);
        this.add(usernameField);
        this.add(gmailField);
        this.add(passwordField);
        this.add(loginButton);

        loginButton.setBackground(new Color(0xF54927));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 15));
        loginButton.setFocusable(false);

        for (JLabel lbl : new JLabel[] { label, label2, label3 }) {
            lbl.setBackground(new Color(0xF54927));
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Arial", Font.BOLD, 15));
            lbl.setOpaque(true);
            lbl.setHorizontalAlignment(JLabel.CENTER);
        }

        for (JTextField field : new JTextField[] { usernameField, gmailField, passwordField }) {
            field.setFont(new Font("Arial", Font.PLAIN, 14));
        }

        label_img.setBounds(45, 95, 50, 50);
        label_img2.setBounds(45, 185, 50, 50);
        label_img3.setBounds(45, 275, 50, 50);

        label.setBounds(100, 60, 150, 32);
        label2.setBounds(100, 150, 150, 32);
        label3.setBounds(100, 240, 150, 32);

        usernameField.setBounds(100, 100, 300, 40);
        gmailField.setBounds(100, 190, 300, 40);
        passwordField.setBounds(100, 280, 300, 40);

        loginButton.setBounds(175, 350, 100, 35);

        // ACTION LISTENER INSIDE CONSTRUCTOR
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String gmail = gmailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || gmail.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "SELECT * FROM entity_table WHERE entity_name=? AND entity_email=? AND entity_passcode=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, gmail);
                ps.setString(3, password);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    dispose();
                    new Repository();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.",
                            "Login Failed", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        this.setVisible(true);
    }
}