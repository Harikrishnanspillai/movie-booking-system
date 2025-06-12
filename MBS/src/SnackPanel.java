import java.awt.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;

public class SnackPanel extends JPanel {
    private JPanel parentPanel;
    private JPanel prevPanel;
    private JPanel nextPanel;

    public SnackPanel(JPanel parent, JPanel prePanel, Integer[] seatIds) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JPanel snackGrid = new JPanel(new GridLayout(0, 1, 10, 10));
        snackGrid.setBackground(Color.WHITE);

        JButton backButton = styledButton("← Back");
        JButton confirmButton = styledButton("Confirm");

        backButton.addActionListener(e -> {
            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        Map<Integer, Integer> snackQuantityMap = new HashMap<>();

        try {
            Snack[] snacks = listAllSnacks();
            if (snacks != null && snacks.length != 0) {
                for (Snack snack : snacks) {
                    JPanel snackPanel = new JPanel(new BorderLayout());
                    snackPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    snackPanel.setBackground(new Color(245, 245, 245));

                    JLabel infoLabel = new JLabel(snack.getName() + " - ₹" + snack.getPrice() + " | Stock: " + snack.getQuantity());
                    infoLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                    infoLabel.setFont(new Font("Sans Serif", Font.PLAIN, 14));
                    snackPanel.add(infoLabel, BorderLayout.NORTH);

                    JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    controlPanel.setBackground(new Color(245, 245, 245));

                    JButton minusButton = styledButton("-");
                    JButton plusButton = styledButton("+");
                    JLabel qtyLabel = new JLabel("0");
                    qtyLabel.setFont(new Font("Sans Serif", Font.PLAIN, 14));
                    final int[] qty = {0};

                    plusButton.addActionListener(e -> {
                        if (qty[0] < snack.getQuantity()) {
                            qty[0]++;
                            qtyLabel.setText(String.valueOf(qty[0]));
                            snackQuantityMap.put(snack.getId(), qty[0]);
                        }
                    });

                    minusButton.addActionListener(e -> {
                        if (qty[0] > 0) {
                            qty[0]--;
                            qtyLabel.setText(String.valueOf(qty[0]));
                            if (qty[0] == 0) {
                                snackQuantityMap.remove(snack.getId());
                            } else {
                                snackQuantityMap.put(snack.getId(), qty[0]);
                            }
                        }
                    });

                    controlPanel.add(minusButton);
                    controlPanel.add(new JLabel("Quantity:"));
                    controlPanel.add(qtyLabel);
                    controlPanel.add(plusButton);

                    snackPanel.add(controlPanel, BorderLayout.CENTER);
                    snackGrid.add(snackPanel);
                }

                confirmButton.addActionListener(e -> {
                    List<Integer> snackIdsList = new ArrayList<>();
                    for (Map.Entry<Integer, Integer> entry : snackQuantityMap.entrySet()) {
                        for (int i = 0; i < entry.getValue(); i++) {
                            snackIdsList.add(entry.getKey());
                        }
                    }

                    nextPanel = new PaymentPanel(parentPanel, prevPanel, seatIds, snackIdsList.toArray(new Integer[0]));
                    parentPanel.removeAll();
                    parentPanel.add(nextPanel, BorderLayout.CENTER);
                    parentPanel.revalidate();
                    parentPanel.repaint();
                });

                add(new JScrollPane(snackGrid), BorderLayout.CENTER);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
                buttonPanel.setBackground(Color.WHITE);
                buttonPanel.add(backButton);
                buttonPanel.add(confirmButton);
                add(buttonPanel, BorderLayout.SOUTH);
            } else {
                JLabel msg = new JLabel("Nothing to see here");
                msg.setFont(new Font("Courier", Font.BOLD, 16));
                msg.setHorizontalAlignment(SwingConstants.CENTER);
                add(msg, BorderLayout.CENTER);
                add(backButton, BorderLayout.SOUTH);
            }
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, "Some error has occurred, please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton styledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBackground(Color.LIGHT_GRAY);
        button.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        return button;
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

                snacks.add(new Snack(id, name, price, quantity));
            }
            return snacks.toArray(new Snack[0]);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Some error has occurred, please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}
