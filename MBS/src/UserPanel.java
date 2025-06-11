import java.awt.*;
import javax.swing.*;

public class UserPanel extends JPanel{
    JPanel parentPanel;
    static User u;
    JButton loginButton = new JButton("Login");
    JButton signUpButton = new JButton("Sign Up");
    public UserPanel(JPanel parent) {
        this.parentPanel = parent;
        setLayout(new FlowLayout());
        loginButton.setMaximumSize(new Dimension(70, 20));
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

    public static void setUser(User user){
        u = user;
    }

    public static User getUser(){
        return u;
    }
    public static void clearUser(){
        u = null;
    }
}