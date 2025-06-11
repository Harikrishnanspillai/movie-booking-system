import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.util.List;

public class SeatPanel extends JPanel {
    private JPanel parentPanel;
    private JPanel prevPanel;
    private JPanel nextPanel;

    public SeatPanel(JPanel parent, JPanel prePanel, int slotId) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;
        List<Integer> seatIds = new ArrayList<>();

        JPanel seatGrid = new JPanel(new GridLayout(8, 10, 5, 5));
        setLayout(new BorderLayout());
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
        confirmButton.addActionListener((e) -> {
            if (seatIds.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select at least one seat before proceeding.", "Selection Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            nextPanel = new BookingPanel(parentPanel, prevPanel, seatIds.toArray(new Integer[0]));
            parentPanel.removeAll();
            parentPanel.add(nextPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });
            try {
            Seat[] seats = listAllSeats(slotId);
            if (seats.length != 0){
            for (Seat s : seats) {
            JButton seatButton = new JButton(s.getSeatNumber());
            seatButton.setBackground(Color.WHITE);

            if (s.isBooked() == 1) {
                seatButton.setBackground(Color.BLACK);
                seatButton.setForeground(Color.WHITE);
                seatButton.setEnabled(false);  // Optional: prevent clicking
            }
            seatButton.addActionListener((e) ->{
                if ((seatButton.getBackground()).equals(Color.WHITE)) {
                    seatButton.setBackground(Color.GREEN);
                    seatIds.add(s.getSeatId());
                    
                } else if((seatButton.getBackground()).equals(Color.GREEN)){
                    seatButton.setBackground(Color.WHITE);
                    seatIds.remove(s.getSeatId());
                }
            });

            seatGrid.add(seatButton);
        }
        
        add(seatGrid, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(backButton);
        buttonPanel.add(confirmButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading seats", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public Seat[] listAllSeats(int slot_id) {
        String sql = "SELECT * FROM Seats where slot_id = ?";
        List<Seat> seats = new ArrayList<>();
        try (Connection conn = DBC.Connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setInt(1, slot_id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int seatId = rs.getInt("seat_id");
                int slotId = rs.getInt("slot_id");
                String seatNo = rs.getString("seat_number");
                int booked = rs.getInt("is_booked");

                Seat seat = new Seat(seatId, slotId, seatNo, booked);
                seats.add(seat);
            }
            
            return seats.toArray(new Seat[0]);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Some Error has occured", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}
