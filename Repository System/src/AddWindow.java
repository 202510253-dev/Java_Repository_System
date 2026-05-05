import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.*;

public class AddWindow extends JDialog {

    private JTextField nameField = new JTextField();
    private JTextField priceField = new JTextField();
    private JTextField quantityField = new JTextField();
    private JTextField categoryField = new JTextField();
    private JButton addButton = new JButton("Add");
    private Repository parent;

    public AddWindow(Repository parent) {
        super(parent.frame, "Add New Product", true);
        this.parent = parent;

        setSize(350, 420);
        setLocationRelativeTo(parent.frame);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(50, 70, 90));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(50, 70, 90));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));

        formPanel.add(makeLabel("Name"));
        formPanel.add(Box.createVerticalStrut(5));
        styleField(nameField);
        formPanel.add(nameField);

        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(makeLabel("Price"));
        formPanel.add(Box.createVerticalStrut(5));
        styleField(priceField);
        formPanel.add(priceField);

        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(makeLabel("Quantity"));
        formPanel.add(Box.createVerticalStrut(5));
        styleField(quantityField);
        formPanel.add(quantityField);

        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(makeLabel("Category"));
        formPanel.add(Box.createVerticalStrut(5));
        styleField(categoryField);
        formPanel.add(categoryField);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(50, 70, 90));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 20));

        styleButton(addButton);
        addButton.addActionListener(e -> handleAdd());
        bottomPanel.add(addButton);

        add(formPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JLabel makeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBackground(new Color(220, 80, 40));
        label.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(220, 80, 40));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusable(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(100, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void handleAdd() {
        String name = nameField.getText().trim();
        String priceText = priceField.getText().trim();
        String quantityText = quantityField.getText().trim();
        String category = categoryField.getText().trim();

        if (name.isEmpty() || priceText.isEmpty() || quantityText.isEmpty() || category.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(quantityText);

            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "INSERT INTO product_list (Item_name, Item_price, Item_quantity, Item_category) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, name);
                ps.setDouble(2, price);
                ps.setInt(3, quantity);
                ps.setString(4, category);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Product added successfully!");
                parent.loadTableData();
                dispose();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Price and Quantity must be numbers.", "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}