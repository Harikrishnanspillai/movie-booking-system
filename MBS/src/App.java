import java.awt.*;
import javax.swing.*;


public class App {
    public static void main(String[] args) {
        JFrame frame = new JFrame("MBS");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel title = new JLabel("Movie Ticket Booking System");
        title.setFont(new Font("Papyrus", Font.BOLD | Font.ITALIC, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        JPanel mainPanel = new JPanel(new BorderLayout());
        UserPanel userPanel = new UserPanel(mainPanel);
        mainPanel.add(userPanel, BorderLayout.CENTER);
        frame.add(title, BorderLayout.NORTH);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setVisible(true);
    }
}
