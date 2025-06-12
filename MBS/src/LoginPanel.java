import java.awt.*;
import java.sql.*;
import javax.swing.*;

class LoginPanel extends JPanel {
    private JPanel parentPanel;
    private JPanel prevPanel;
    private JPanel nextPanel;

    public LoginPanel(JPanel parent, JPanel prePanel) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(emailLabel, gbc);

        JTextField emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(passwordField, gbc);

        JButton submitButton = styledButton("Submit");
        JButton backButton = styledButton("Back");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(backButton);
        buttonPanel.add(submitButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        submitButton.addActionListener(e -> {
            try {
                User u = login(emailField.getText(), new String(passwordField.getPassword()));
                UserPanel.setUser(u);
                if (u instanceof Admin) {
                    nextPanel = new AdminPanel(u, parentPanel, new UserPanel(parentPanel));
                    parentPanel.removeAll();
                    parentPanel.add(nextPanel, BorderLayout.CENTER);
                    parentPanel.revalidate();
                    parentPanel.repaint();
                } else if (u instanceof Customer) {
                    nextPanel = new CustomerPanel(u, parentPanel, new UserPanel(parentPanel));
                    parentPanel.removeAll();
                    parentPanel.add(nextPanel, BorderLayout.CENTER);
                    parentPanel.revalidate();
                    parentPanel.repaint();
                }
            } catch (Exception ex) {
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
        button.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        return button;
    }

    public static User login(String email, String passwd) {
        String query = "SELECT * FROM Users WHERE email = ? AND password = ?";
        passwd = PassHash.encode(passwd);
        try (Connection conn = DBC.Connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, passwd);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("user_id");
                String name = rs.getString("name");
                String role = rs.getString("role");

                if ("ADMIN".equals(role)) {
                    return new Admin(id, name, email, passwd);
                } else if ("CUSTOMER".equals(role)) {
                    return new Customer(id, name, email, passwd);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Wrong credentials, Try again", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Some Error has occurred, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}
