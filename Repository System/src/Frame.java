import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Frame extends JFrame {

    JButton loginButton;
    JTextField usernameField;
    JTextField gmailField;
    JPasswordField passwordField;
    JCheckBox rememberMe;
    JLabel forgotPassword;

    Frame() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // ── LEFT PANEL ──────────────────────────────────────────────
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(0x2E7D4F));
        leftPanel.setPreferredSize(new Dimension(220, 420));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 30, 20));

        // Logo placeholder (replace with your actual ImageIcon if needed)
        JLabel logoLabel = new JLabel();
        try {
            ImageIcon raw = new ImageIcon(getClass().getResource("/logo.png"));
            Image scaled = raw.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaled));
        } catch (Exception ex) {
            logoLabel.setText("🏫");
            logoLabel.setFont(new Font("Arial", Font.PLAIN, 60));
            logoLabel.setForeground(Color.WHITE);
        }
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel schoolName = new JLabel(
                "<html><center>MABAYUAN ELEMENTARY SCHOOL<br>CANTEEN INVENTORY SYSTEM</center></html>");
        schoolName.setFont(new Font("Arial", Font.BOLD, 13));
        schoolName.setForeground(Color.WHITE);
        schoolName.setAlignmentX(Component.CENTER_ALIGNMENT);
        schoolName.setHorizontalAlignment(JLabel.CENTER);

        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(logoLabel);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(schoolName);
        leftPanel.add(Box.createVerticalGlue());

        // ── RIGHT PANEL ─────────────────────────────────────────────
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(0xF2F2F2));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));

        // Title bar
        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titleBar.setBackground(new Color(0x2E7D4F));
        titleBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        JLabel titleLabel = new JLabel("🔒  SYSTEM LOGIN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleBar.add(titleLabel);
        titleBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username
        JLabel userLabel = new JLabel("USERNAME");
        styleFormLabel(userLabel);

        usernameField = new JTextField();
        styleField(usernameField, "👤  ENTER YOUR USERNAME");

        // Email
        JLabel emailLabel = new JLabel("EMAIL");
        styleFormLabel(emailLabel);

        gmailField = new JTextField();
        styleField(gmailField, "✉  ENTER YOUR EMAIL");

        // Password
        JLabel passLabel = new JLabel("PASSWORD");
        styleFormLabel(passLabel);

        passwordField = new JPasswordField();
        styleField(passwordField, "🔑  ENTER YOUR PASSWORD");

        // Remember Me + Forgot Password row
        JPanel rememberRow = new JPanel(new BorderLayout());
        rememberRow.setBackground(new Color(0xF2F2F2));
        rememberRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        rememberMe = new JCheckBox("REMEMBER ME");
        rememberMe.setBackground(new Color(0xF2F2F2));
        rememberMe.setFont(new Font("Arial", Font.PLAIN, 11));
        rememberMe.setFocusable(false);

        forgotPassword = new JLabel("FORGOT PASSWORD?");
        forgotPassword.setFont(new Font("Arial", Font.BOLD, 11));
        forgotPassword.setForeground(new Color(0x2E7D4F));
        forgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));

        rememberRow.add(rememberMe, BorderLayout.WEST);
        rememberRow.add(forgotPassword, BorderLayout.EAST);

        // Login Button
        loginButton = new JButton("➜  LOG IN");
        loginButton.setBackground(new Color(0x2E7D4F));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 15));
        loginButton.setFocusable(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Assemble right panel
        rightPanel.add(titleBar);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(userLabel);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(usernameField);
        rightPanel.add(Box.createVerticalStrut(12));
        rightPanel.add(emailLabel);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(gmailField);
        rightPanel.add(Box.createVerticalStrut(12));
        rightPanel.add(passLabel);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(passwordField);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(rememberRow);
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(loginButton);

        this.add(leftPanel, BorderLayout.WEST);
        this.add(rightPanel, BorderLayout.CENTER);

        // ── ACTION LISTENER ─────────────────────────────────────────
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

    private void styleFormLabel(JLabel label) {
        label.setFont(new Font("Arial", Font.BOLD, 11));
        label.setForeground(Color.DARK_GRAY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void styleField(JTextField field, String placeholder) {
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xCCCCCC), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        field.setForeground(Color.GRAY);
        field.setText(placeholder);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
    }
}
