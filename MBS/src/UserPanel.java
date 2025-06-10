import java.awt.*;
import javax.swing.*;

public class UserPanel extends JPanel{
    JPanel parentPanel;
    JButton loginButton = new JButton("Login");
    JButton signUpButton = new JButton("Sign Up");
    public UserPanel(JPanel parent) {
        this.parentPanel = parent;
        setLayout(new GridLayout(0, 1, 20, 20));
        add(loginButton);
        add(signUpButton);

        loginButton.addActionListener((e) -> {
            LoginPanel loginPanel = new LoginPanel(parentPanel, UserPanel.this);
            parentPanel.removeAll();
            parentPanel.add(loginPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });
        signUpButton.addActionListener((e) -> {
            SignUpPanel signUpPanel = new SignUpPanel(parentPanel, UserPanel.this);
            parentPanel.removeAll();
            parentPanel.add(signUpPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });
    }
}
