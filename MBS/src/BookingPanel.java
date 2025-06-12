
import java.awt.*;
import javax.swing.*;

public class BookingPanel extends JPanel {
    private JPanel parentPanel;
    private JPanel prevPanel;
    private JPanel nextPanel;

    public BookingPanel(JPanel parentPanel, JPanel prevPanel, Integer[] seatIds) {
        this.parentPanel = parentPanel;
        this.prevPanel = prevPanel;

        setLayout(new GridLayout(0, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JButton backButton = styledButton("Back");
        JButton noButton = styledButton("No");
        JButton yesButton = styledButton("Yes");

        backButton.addActionListener(e -> {
            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        noButton.addActionListener(e -> {
            nextPanel = new PaymentPanel(parentPanel, new CustomerPanel(UserPanel.getUser(), parentPanel, new UserPanel(parentPanel)), seatIds);
            parentPanel.removeAll();
            parentPanel.add(nextPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        yesButton.addActionListener(e -> {
            nextPanel = new SnackPanel(parentPanel, new CustomerPanel(UserPanel.getUser(), parentPanel, new UserPanel(parentPanel)), seatIds);
            parentPanel.removeAll();
            parentPanel.add(nextPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        JLabel msg = new JLabel("Would you like snacks with that?");
        msg.setFont(new Font("SansSerif", Font.PLAIN, 16));
        msg.setHorizontalAlignment(SwingConstants.CENTER);
        msg.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(backButton);
        buttonPanel.add(noButton);
        buttonPanel.add(yesButton);

        add(msg);
        add(buttonPanel);
    }

    private JButton styledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBackground(Color.LIGHT_GRAY);
        button.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        return button;
    }
}
