import java.awt.*;
import java.sql.*;
import javax.swing.*;

class SignUpPanel extends JPanel {
    private JPanel parentPanel;
    private JPanel prevPanel;
    private JPanel nextPanel;

    public SignUpPanel(JPanel parent, JPanel prePanel) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;
        setLayout(new GridLayout(4, 2, 10, 10));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(20);
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        JButton submitButton = new JButton("Submit");
        JButton backButton = new JButton("Back");

        add(nameLabel);
        add(nameField);
        add(emailLabel);
        add(emailField);
        add(passwordLabel);
        add(passwordField);
        add(backButton);
        add(submitButton);

        backButton.addActionListener(e -> {
            UserPanel.clearUser();
            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        submitButton.addActionListener((e) -> {
            try {
                User u = signup(nameField.getText(), emailField.getText(), new String(passwordField.getPassword()));
                UserPanel.setUser(u);
                nextPanel = new CustomerPanel(u, parent, new UserPanel(parentPanel));
                parentPanel.removeAll();
                parentPanel.add(nextPanel, BorderLayout.CENTER);
                parentPanel.revalidate();
                parentPanel.repaint();
            }
            catch(Exception err){
                JOptionPane.showMessageDialog(null, "Some Error has occured, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
                parentPanel.removeAll();
                parentPanel.add(prevPanel, BorderLayout.CENTER);
                parentPanel.revalidate();
                parentPanel.repaint();
            }
        });

    }
    public static User signup(String name, String email, String passwd) {
        String checkQuery = "SELECT user_id FROM Users WHERE email = ?";
        String insertQuery = "INSERT INTO Users (name, email, password, role) VALUES (?, ?, ?, ?)";
        passwd = PassHash.encode(passwd);
    
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