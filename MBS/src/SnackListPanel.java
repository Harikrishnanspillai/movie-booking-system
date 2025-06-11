import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
public class SnackListPanel extends JPanel{
    private JPanel parentPanel;
    private JPanel prevPanel;
    private JPanel nextPanel;

    public SnackListPanel(JPanel parent, JPanel prePanel) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;

        setLayout(new GridLayout(3, 3, 10, 10));
        JButton backButton = new JButton("Back");
        backButton.setVerticalAlignment(SwingConstants.CENTER);
        backButton.setHorizontalAlignment(SwingConstants.CENTER);
        backButton.addActionListener(e -> {
            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        try {
            Snack[] snacks = listAllSnacks();
            if (snacks.length != 0){
            for (Snack snack : snacks) {
            JButton movieButton = new JButton(snack.getName());

            movieButton.addActionListener(e -> {
                nextPanel = new AdminSnackPanel(parent, prePanel, snack.getId(), snack.getName(), snack.getPrice(), snack.getQuantity());
                parentPanel.removeAll();
                parentPanel.add(nextPanel, BorderLayout.CENTER);
                parentPanel.revalidate();
                parentPanel.repaint();
            });

            add(movieButton); 
        }
        for (int i = 0; i>(snacks.length%3); i++){
                    add(new JLabel());
                }
                add(backButton);
    }
    else{
                JLabel msg = new JLabel("Nothing to see here");
                msg.setFont(new Font("Courier New", Font.BOLD, 20));
                msg.setHorizontalAlignment(SwingConstants.CENTER);
                msg.setVerticalAlignment(SwingConstants.CENTER);
                add(new JLabel());
                add(msg);
                add(new JLabel());
                add(backButton);
            }
        
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, "Some Error has occured, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }

    }

    public SnackListPanel(JPanel parent, JPanel prePanel, boolean b) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;

        setLayout(new GridLayout(3, 3, 10, 10));
        JButton backButton = new JButton("Back");
        backButton.setVerticalAlignment(SwingConstants.CENTER);
        backButton.setHorizontalAlignment(SwingConstants.CENTER);
        backButton.addActionListener(e -> {
            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        try {
            Snack[] snacks = listAllSnacks();
            if (snacks.length != 0)
            {for (Snack snack : snacks) {
            JButton movieButton = new JButton(snack.getName());

            movieButton.addActionListener(e -> {
                nextPanel = new AdminSnackPanel(parent, prePanel, snack.getId(), snack.getName());
                parentPanel.removeAll();
                parentPanel.add(nextPanel, BorderLayout.CENTER);
                parentPanel.revalidate();
                parentPanel.repaint();
            });

            add(movieButton);}
            for (int i = 0; i>(snacks.length%3); i++){
                    add(new JLabel());
                }
                add(backButton);
    }
    else{
                JLabel msg = new JLabel("Nothing to see here");
                msg.setFont(new Font("Courier New", Font.BOLD, 20));
                msg.setHorizontalAlignment(SwingConstants.CENTER);
                msg.setVerticalAlignment(SwingConstants.CENTER);
                add(new JLabel());
                add(msg);
                add(new JLabel());
                add(backButton);
            }
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, "Some Error has occured, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }

    }
    
    public Snack[] listAllSnacks() {
        String sql = "SELECT * FROM Snacks";
        List<Snack> snacks = new ArrayList<>();
        try (Connection conn = DBC.Connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("snack_id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");

                Snack snack = new Snack(id, name, price, quantity);
                snacks.add(snack);
            }
            
            return snacks.toArray(new Snack[0]);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Some Error has occured, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}
