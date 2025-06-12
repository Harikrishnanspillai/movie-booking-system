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

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(nameLabel, gbc);

        JTextField nameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(nameField, gbc);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(emailLabel, gbc);

        JTextField emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(passwordField, gbc);

        JButton submitButton = styledButton("Submit");
        JButton backButton = styledButton("Back");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(backButton);
        buttonPanel.add(submitButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        submitButton.addActionListener(e -> {
            try {
                User u = signup(nameField.getText(), emailField.getText(), new String(passwordField.getPassword()));
                UserPanel.setUser(u);
                nextPanel = new CustomerPanel(u, parentPanel, new UserPanel(parentPanel));
                parentPanel.removeAll();
                parentPanel.add(nextPanel, BorderLayout.CENTER);
                parentPanel.revalidate();
                parentPanel.repaint();
            } catch (Exception err) {
                JOptionPane.showMessageDialog(null, "Some Error has occurred, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
                parentPanel.removeAll();
                parentPanel.add(prevPanel, BorderLayout.CENTER);
                parentPanel.revalidate();
                parentPanel.repaint();
            }
        });

        backButton.addActionListener(e -> {
            UserPanel.clearUser();
            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });
    }

    private JButton styledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBackground(Color.LIGHT_GRAY);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return button;
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