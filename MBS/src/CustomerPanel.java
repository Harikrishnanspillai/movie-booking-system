
import java.awt.*;
import javax.swing.*;

public class CustomerPanel extends JPanel{
    private JPanel parentPanel;
    private JPanel prevPanel;
    private JPanel nextPanel;

    public CustomerPanel(User u, JPanel parent, JPanel prePanel){
        this.parentPanel = parent;
        this.prevPanel = prePanel;
        setLayout(new GridLayout(0, 1, 5, 5));
        JLabel welcome = new JLabel(String.format("Welcome, %s", u.getName()));
        welcome.setFont(new Font("Courier New", Font.ITALIC, 18));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcome.setHorizontalAlignment(SwingConstants.CENTER);

        JButton bookTicket = new JButton("Book Ticket");
        JButton viewBooked = new JButton("View booked tickets");
        JButton backButton = new JButton("Logout");
        backButton.addActionListener(e -> {
            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        bookTicket.addActionListener((e) ->{
            nextPanel = new CustomerMoviePanel(parentPanel, this);
            parentPanel.removeAll();
            parentPanel.add(nextPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();

        });
        viewBooked.addActionListener(e ->{
            nextPanel = new CustomerTicketsPanel(parentPanel, this);
            parentPanel.removeAll();
            parentPanel.add(nextPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });


        add(welcome);
        add(bookTicket);
        add(viewBooked);
        add(new JLabel());
        add(backButton);

    }
}