
import java.awt.*;
import javax.swing.*;
public class BookingPanel extends JPanel{
    private JPanel parentPanel;
    private JPanel prevPanel;
    private JPanel nextPanel;

    public BookingPanel(JPanel parentPanel, JPanel prevPanel, Integer[] seatIds) {
        this.parentPanel = parentPanel;
        this.prevPanel = prevPanel;

        setLayout(new GridLayout(0,1,10,10));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });
        JButton no = new JButton("No");
        no.addActionListener(e -> {
            nextPanel = new PaymentPanel(parentPanel, new CustomerPanel(UserPanel.getUser(), parentPanel, new UserPanel(parentPanel)), seatIds);
            parentPanel.removeAll();
            parentPanel.add(nextPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });
        JButton yes = new JButton("Yes");
        yes.addActionListener(e -> {
            nextPanel = new SnackPanel(parentPanel, new CustomerPanel(UserPanel.getUser(), parentPanel, new UserPanel(parentPanel)), seatIds);
            parentPanel.removeAll();
            parentPanel.add(nextPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        JLabel msg = new JLabel("Would u like snacks with that?");
        

        add(msg);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(backButton);
        buttonPanel.add(no);
        buttonPanel.add(yes);
        add(buttonPanel);
    }
}
