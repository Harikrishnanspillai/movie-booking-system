import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class SnackPanel extends JPanel{
    private JPanel parentPanel;
    private JPanel prevPanel;
    private JPanel nextPanel;
    public SnackPanel(JPanel parent, JPanel prePanel, Integer[] seatIds) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;

        setLayout(new BorderLayout());
        JPanel snackGrid = new JPanel(new GridLayout(8, 10, 5, 5));
        JButton backButton = new JButton("Back");
        JButton confirmButton = new JButton("Confirm");
        backButton.setVerticalAlignment(SwingConstants.CENTER);
        backButton.setHorizontalAlignment(SwingConstants.CENTER);
        backButton.addActionListener(e -> {
            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });
        confirmButton.addActionListener((e) ->{
            nextPanel = new PaymentPanel(parentPanel, prevPanel, seatIds, snackIds);
        });

        List<Integer> snackIds = new ArrayList<>();
        try {
            Snack[] snacks = listAllSnacks();
            if (snacks.length != 0){
            for (Snack snack : snacks) {
            JButton snackButton = new JButton(snack.getName());

            snackButton.addActionListener(e -> {
                snackIds.add(snack.getId());
                parentPanel.removeAll();
                parentPanel.add(nextPanel, BorderLayout.CENTER);
                parentPanel.revalidate();
                parentPanel.repaint();
            });

            snackGrid.add(snackButton); 
        }
        add(snackGrid, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(backButton);
        buttonPanel.add(confirmButton);
        add(buttonPanel, BorderLayout.SOUTH);
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
