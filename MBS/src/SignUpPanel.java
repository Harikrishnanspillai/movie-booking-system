import java.awt.*;
import java.sql.*;
import javax.swing.*;

class SignUpPanel extends JPanel {
    private JPanel parentPanel;
    private UserPanel userPanel;

    public SignUpPanel(JPanel parent, UserPanel userPanel) {
        this.parentPanel = parent;
        this.userPanel = userPanel;
        setLayout(new FlowLayout());

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        JButton submitButton = new JButton("Submit");
        JButton backButton = new JButton("Back");

        add(emailLabel);
        add(emailField);
        add(passwordLabel);
        add(passwordField);
        add(submitButton);
        add(backButton);

        backButton.addActionListener(e -> {
            parentPanel.removeAll();
            parentPanel.add(userPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });
    }
    public static User signup(String name, String email, String passwd) {
        String checkQuery = "SELECT user_id FROM Users WHERE email = ?";
        String insertQuery = "INSERT INTO Users (name, email, password, role) VALUES (?, ?, ?, ?)";
    
        try (Connection conn = DBC.Connect()) {
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, email);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    System.out.println("Email already in use.");
                    return null;
                }
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, name);
                insertStmt.setString(2, email);
                insertStmt.setString(3, passwd);
                insertStmt.setString(4, "CUSTOMER");

                int rowsAffected = insertStmt.executeUpdate();
                if (rowsAffected > 0) {
                    ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int newUserId = generatedKeys.getInt(1);
                        return new Customer(newUserId, name, email, passwd);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
    }
    return null;
    }
}
