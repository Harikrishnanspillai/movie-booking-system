import java.awt.*;
import javax.swing.*;

public class CustomerPanel extends JPanel {
    private JPanel parentPanel;
    private JPanel prevPanel;
    private JPanel nextPanel;

    public CustomerPanel(User u, JPanel parent, JPanel prePanel) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;
        setLayout(new GridLayout(0, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JLabel welcome = new JLabel(String.format("Welcome, %s", u.getName()));
        welcome.setFont(new Font("Sans Serif", Font.ITALIC, 18));
        welcome.setHorizontalAlignment(SwingConstants.CENTER);
        welcome.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JButton bookTicket = styledButton("Book Ticket");
        JButton viewBooked = styledButton("View booked tickets");
        JButton backButton = styledButton("Logout");

        backButton.addActionListener(e -> {
            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        bookTicket.addActionListener(e -> {
            nextPanel = new CustomerMoviePanel(parentPanel, this);
            parentPanel.removeAll();
            parentPanel.add(nextPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        viewBooked.addActionListener(e -> {
            nextPanel = new CustomerTicketsPanel(parentPanel, this);
            parentPanel.removeAll();
            parentPanel.add(nextPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        add(welcome);
        add(bookTicket);
        add(viewBooked);
        add(Box.createVerticalStrut(10));
        add(backButton);
    }

    private JButton styledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setBackground(Color.LIGHT_GRAY);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return button;
    }
}
