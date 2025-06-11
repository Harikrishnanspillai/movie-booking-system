import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class SnackListPanel extends JPanel {
    private JPanel parentPanel;
    private JPanel prevPanel;
    private JPanel nextPanel;

    public SnackListPanel(JPanel parent, JPanel prePanel) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel heading = new JLabel("Snack List");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 20));
        heading.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        gridPanel.setOpaque(false);

        JButton backButton = styledButton("← Back");
        backButton.addActionListener(e -> {
            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        try {
            Snack[] snacks = listAllSnacks();
            if (snacks != null && snacks.length != 0) {
                for (Snack snack : snacks) {
                    JButton snackButton = styledButton(snack.getName());
                    snackButton.addActionListener(e -> {
                        nextPanel = new AdminSnackPanel(parent, prePanel, snack.getId(), snack.getName(), snack.getPrice(), snack.getQuantity());
                        parentPanel.removeAll();
                        parentPanel.add(nextPanel, BorderLayout.CENTER);
                        parentPanel.revalidate();
                        parentPanel.repaint();
                    });
                    gridPanel.add(snackButton);
                }

                int emptySlots = (3 - (snacks.length % 3)) % 3;
                for (int i = 0; i < emptySlots; i++) {
                    gridPanel.add(new JLabel());
                }

            } else {
                JLabel msg = new JLabel("Nothing to see here");
                msg.setFont(new Font("Courier New", Font.BOLD, 20));
                msg.setHorizontalAlignment(SwingConstants.CENTER);
                gridPanel.add(new JLabel());
                gridPanel.add(msg);
                gridPanel.add(new JLabel());
            }

        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, "Some Error has occured, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(backButton);

        add(heading, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public SnackListPanel(JPanel parent, JPanel prePanel, boolean b) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel heading = new JLabel("Snack List");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 20));
        heading.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        gridPanel.setOpaque(false);

        JButton backButton = styledButton("← Back");
        backButton.addActionListener(e -> {
            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        try {
            Snack[] snacks = listAllSnacks();
            if (snacks != null && snacks.length != 0) {
                for (Snack snack : snacks) {
                    JButton snackButton = styledButton(snack.getName());
                    snackButton.addActionListener(e -> {
                        nextPanel = new AdminSnackPanel(parent, prePanel, snack.getId(), snack.getName());
                        parentPanel.removeAll();
                        parentPanel.add(nextPanel, BorderLayout.CENTER);
                        parentPanel.revalidate();
                        parentPanel.repaint();
                    });
                    gridPanel.add(snackButton);
                }

                int emptySlots = (3 - (snacks.length % 3)) % 3;
                for (int i = 0; i < emptySlots; i++) {
                    gridPanel.add(new JLabel());
                }

            } else {
                JLabel msg = new JLabel("Nothing to see here");
                msg.setFont(new Font("Courier New", Font.BOLD, 20));
                msg.setHorizontalAlignment(SwingConstants.CENTER);
                gridPanel.add(new JLabel());
                gridPanel.add(msg);
                gridPanel.add(new JLabel());
            }

        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, "Some Error has occured, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(backButton);

        add(heading, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton styledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setBackground(new Color(220, 230, 245));
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
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
