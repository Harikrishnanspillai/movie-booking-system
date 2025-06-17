import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class AdminSnackPanel extends JPanel {
    private JPanel parentPanel;
    private JPanel prevPanel;
public AdminSnackPanel(JPanel parent, JPanel prePanel) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;
        setLayout(new GridLayout(4, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(20);

        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField(20);

        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField(20);

        JButton backButton = styledButton("Back");
        JButton submitButton = styledButton("Submit");

        submitButton.addActionListener(e -> {
            addSnack(nameField.getText(), Double.parseDouble(priceField.getText().trim()), Integer.parseInt(quantityField.getText().trim()));
            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        backButton.addActionListener(e -> {
            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        add(nameLabel);
        add(nameField);
        add(priceLabel);
        add(priceField);
        add(quantityLabel);
        add(quantityField);
        add(backButton);
        add(submitButton);
    }

    public AdminSnackPanel(JPanel parent, JPanel prePanel, int snackID, String newName, double newPrice, int newQuantity) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;
        setLayout(new GridLayout(4, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(newName, 20);

        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField(String.valueOf(newPrice), 20);

        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField(String.valueOf(newQuantity), 20);

        JButton backButton = styledButton("Back");
        JButton submitButton = styledButton("Submit");

        submitButton.addActionListener(e -> {
            editSnack(snackID, nameField.getText(), Double.parseDouble(priceField.getText().trim()), Integer.parseInt(quantityField.getText().trim()));
            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        backButton.addActionListener(e -> {
            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        add(nameLabel);
        add(nameField);
        add(priceLabel);
        add(priceField);
        add(quantityLabel);
        add(quantityField);
        add(backButton);
        add(submitButton);
    }

    public AdminSnackPanel(JPanel parent, JPanel prePanel, int snackID, String snackName) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel confirmLabel = new JLabel("Are you sure you want to delete, " + snackName + "?");
        confirmLabel.setFont(new Font("Courier", Font.BOLD, 16));

        JButton backButton = styledButton("Back");
        JButton confirmButton = styledButton("Confirm");

        confirmButton.addActionListener(e -> {
            removeSnack(snackID);
            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        backButton.addActionListener(e -> {
            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        add(confirmLabel);
        add(Box.createRigidArea(new Dimension(20, 0)));
        add(backButton);
        add(confirmButton);
    }

    public JButton styledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBackground(Color.LIGHT_GRAY);
        button.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        return button;
    }



    public void addSnack(String name, double price, int quantity) {
        String sql = "INSERT INTO Snacks (name, price, quantity) VALUES (?, ?, ?)";
        try (Connection conn = DBC.Connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setInt(3, quantity);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Snack added", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Some Error has occured, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void editSnack(int SnackID, String newName, double newPrice, int quantity) {
        String sql = "UPDATE Snacks SET name = ?, price = ?, quantity = ? WHERE snack_id = ?";
        try (Connection conn = DBC.Connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setDouble(2, newPrice);
            stmt.setInt(3, quantity);
            stmt.setInt(4, SnackID);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0){
                JOptionPane.showMessageDialog(null, "Snack edited", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(null, "Snack not found", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Some Error has occured, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void removeSnack(int snackId) {
        String sql = "DELETE FROM Snacks WHERE snack_id = ?";
        try (Connection conn = DBC.Connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, snackId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0){
                JOptionPane.showMessageDialog(null, "Snack removed", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(null, "Snack not found", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Some Error has occured, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
    }
}