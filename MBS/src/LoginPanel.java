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
        setLayout(new GridLayout(3, 2, 10, 10));

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
        add(backButton);
        add(submitButton);

        submitButton.addActionListener((e) -> {
            try {
                User u = login(emailField.getText(), new String(passwordField.getPassword()));
                if(("Admin".equals(u.getClass().getName()))){
                    nextPanel = new AdminPanel(u, parentPanel, this);
                    parentPanel.removeAll();
                    parentPanel.add(nextPanel, BorderLayout.CENTER);
                    parentPanel.revalidate();
                    parentPanel.repaint();
                }
                else if("Customer".equals(u.getClass().getName())){
                    
                }
            }
            catch(Exception err){
                parentPanel.removeAll();
                parentPanel.add(prevPanel, BorderLayout.CENTER);
                parentPanel.revalidate();
                parentPanel.repaint();
            }
        });
        backButton.addActionListener(e -> {
            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });
    }
    public static User login(String email, String passwd) {
        String query = "SELECT * FROM Users WHERE email = ? AND password = ?";
        try (Connection conn = DBC.Connect(); PreparedStatement stmt = conn.prepareStatement(query);){
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
            }
            else{
                JOptionPane.showMessageDialog(null, "Wrong credentials, Try again", "SQLError", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Some Error has occured, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public JPanel getPrevPanel() {
        return prevPanel;
    }
}
