import java.awt.*;
import javax.swing.*;

public class UserPanel extends JPanel {
    JPanel parentPanel;
    static User u;
    JButton loginButton;
    JButton signUpButton;

    public UserPanel(JPanel parent) {
        this.parentPanel = parent;
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        loginButton = styledButton("Login");
        signUpButton = styledButton("Sign Up");

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(loginButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(signUpButton, gbc);

        loginButton.addActionListener(e -> {
            LoginPanel loginPanel = new LoginPanel(parentPanel, this);
            parentPanel.removeAll();
            parentPanel.add(loginPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        signUpButton.addActionListener(e -> {
            SignUpPanel signUpPanel = new SignUpPanel(parentPanel, this);
            parentPanel.removeAll();
            parentPanel.add(signUpPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });
    }

    private JButton styledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(58, 100, 155));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });

        return button;
    }

    public static void setUser(User user) {
        u = user;
    }

    public static User getUser() {
        return u;
    }

    public static void clearUser() {
        u = null;
    }
}
