import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.security.auth.login.LoginContext;

import javax.swing.*;


public class PaymentPanel extends JPanel{
    private JPanel parentPanel;
    private JPanel prevPanel;
    private JPanel nextPanel;

    public PaymentPanel(JPanel parent, JPanel prePanel, Integer[] seatIds) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;
        JPanel ButtonPanel = new JPanel(new FlowLayout());
        JButton confirmButton = new JButton("Confirm");

        confirmButton.addActionListener(e -> {
            for(int i : seatIds){
                bookSeats(i);
            }
            int bookingID = bookTickets(LoginPanel.u.getUserID(), seatIds[0], seatIds.length);
            JOptionPane.showMessageDialog(null, "Booking Succes", "Booking", JOptionPane.INFORMATION_MESSAGE);
            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();

        });

        setLayout(new GridLayout(0,1,10,10));

        add(new JLabel("Enter payment details"));
        ButtonPanel.add(confirmButton);
        add(ButtonPanel);
                
    }

    public void bookSeats(int seatId){
        String sql = "UPDATE Seats SET is_booked = 1 WHERE seat_id = ?";
        try(Connection conn = DBC.Connect();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, seatId);
        }
        catch(Exception err){
            JOptionPane.showMessageDialog(null, "Some Error has occured", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
    }
    public int bookTickets(int userId, int seatId,int num_tickets){
        int slotId = 0;
        double price = 0;
        String sql = "INSERT INTO Bookings (customer_id, slot_id, num_tickets, total_cost) VALUES (?, ?, ?, ?)";
        String costsql = "SELECT price FROM TimeSlots WHERE slot_id = ?";
        String slotsql = "SELECT slot_id FROM Seats WHERE seat_id = ?";
        try(Connection conn = DBC.Connect()){
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement costStmt = conn.prepareStatement(costsql);
        PreparedStatement slotStmt = conn.prepareStatement(slotsql);

        slotStmt.setInt(1, seatId);

        ResultSet rs = slotStmt.executeQuery();

        if(rs.next()){
            slotId = rs.getInt(1);
        }
        costStmt.setInt(1, slotId);
        rs = costStmt.executeQuery();
        if(rs.next()){
            price = rs.getDouble(1);
        }

            stmt.setInt(1, userId);
            stmt.setInt(2,slotId);
            stmt.setInt(3, num_tickets);
            stmt.setDouble(4, price*num_tickets);

            int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int bookingId = generatedKeys.getInt(1);

                        return bookingId;
                    }
                }
        }
        catch(Exception err){
            JOptionPane.showMessageDialog(null, "Some Error has occured", "SQLError", JOptionPane.ERROR_MESSAGE);
        }

        return 0;
    }
}
