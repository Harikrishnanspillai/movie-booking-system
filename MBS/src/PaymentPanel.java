import java.awt.*;
import java.sql.*;
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
            int bookingID = bookTickets(UserPanel.getUser().getUserID(), seatIds);
            JOptionPane.showMessageDialog(null, "Booking Successful! Booking ID: " + bookingID, "Booking", JOptionPane.INFORMATION_MESSAGE);

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
    public PaymentPanel(JPanel parent, JPanel prePanel, Integer[] seatIds, Integer[] snackIds) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;
        JPanel ButtonPanel = new JPanel(new FlowLayout());
        JButton confirmButton = new JButton("Confirm");

        setLayout(new GridLayout(0,1,10,10));

        JPanel paymentPanel = new JPanel(new FlowLayout());
        JLabel paymentLabel = new JLabel("Enter payment details(UPI ID/Credit Card number):");
        JTextField paymentField = new JTextField(20);

        paymentPanel.add(paymentLabel);
        paymentPanel.add(paymentField);

        confirmButton.addActionListener(e -> {
            int bookingID = bookTickets(UserPanel.getUser().getUserID(), seatIds, snackIds);
            JOptionPane.showMessageDialog(null, "Booking Successful! Booking ID: " + bookingID, "Booking", JOptionPane.INFORMATION_MESSAGE);

            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();

        });
        add(paymentPanel);
        ButtonPanel.add(confirmButton);
        add(ButtonPanel);
                
    }

    public void bookSeats(int seatId, int bookingId){
        String sql = "UPDATE Seats SET is_booked = 1 WHERE seat_id = ?";
        String bookingsql = "INSERT INTO BookingSeats VALUES (?, ?)";
        try(Connection conn = DBC.Connect();){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, seatId);
            stmt.executeUpdate();
            PreparedStatement book = conn.prepareStatement(bookingsql);
            book.setInt(1, bookingId);
            book.setInt(2, seatId);
            book.executeUpdate();

        }
        catch(Exception err){
            JOptionPane.showMessageDialog(null, "Some Error has occured", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void bookSnacks(int snackId, int bookingId){
        String updateSql = "UPDATE Snacks SET quantity = quantity - 1 WHERE snack_id = ? AND quantity > 0";
        String insertSql = "INSERT INTO BookingSnacks (booking_id, snack_id, quantity) " +
                        "VALUES (?, ?, 1) ON DUPLICATE KEY UPDATE quantity = quantity + 1";

        try (Connection conn = DBC.Connect();
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            updateStmt.setInt(1, snackId);
            int rowsAffected = updateStmt.executeUpdate();

            if (rowsAffected == 0) {
                JOptionPane.showMessageDialog(null, "Snack is out of stock!", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            insertStmt.setInt(1, bookingId);
            insertStmt.setInt(2, snackId);
            insertStmt.executeUpdate();

        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, "Some error has occurred", "SQL Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public int bookTickets(int userId,Integer[] seatIds){
        int slotId = 0;
        double price = 0;
        int num_tickets = seatIds.length;
        String sql = "INSERT INTO Bookings (customer_id, slot_id, num_tickets, total_cost) VALUES (?, ?, ?, ?)";
        String costsql = "SELECT price FROM TimeSlots WHERE slot_id = ?";
        String slotsql = "SELECT slot_id FROM Seats WHERE seat_id = ?";
        try(Connection conn = DBC.Connect()){
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement costStmt = conn.prepareStatement(costsql);
        PreparedStatement slotStmt = conn.prepareStatement(slotsql);

        slotStmt.setInt(1, seatIds[0]);

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
                        for (int i : seatIds){
                            bookSeats(i, bookingId);
                        }
                        return bookingId;
                    }
                }
        }
        catch(Exception err){
            JOptionPane.showMessageDialog(null, "Some Error has occured", "SQLError", JOptionPane.ERROR_MESSAGE);
        }

        return 0;
    }
    public int bookTickets(int userId,Integer[] seatIds, Integer[] snackIds){
        int slotId = 0;
        double ticketPrice = 0;
        int num_tickets=seatIds.length;
        String sql = "INSERT INTO Bookings (customer_id, slot_id, num_tickets, total_cost) VALUES (?, ?, ?, ?)";
        String costsql = "SELECT price FROM TimeSlots WHERE slot_id = ?";
        String slotsql = "SELECT slot_id FROM Seats WHERE seat_id = ?";


        try(Connection conn = DBC.Connect()){
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement costStmt = conn.prepareStatement(costsql);
        PreparedStatement slotStmt = conn.prepareStatement(slotsql);

        slotStmt.setInt(1, seatIds[0]);

        ResultSet rs = slotStmt.executeQuery();

        if(rs.next()){
            slotId = rs.getInt(1);
        }
        costStmt.setInt(1, slotId);
        rs = costStmt.executeQuery();
        if(rs.next()){
            ticketPrice = rs.getDouble(1);
        }

            stmt.setInt(1, userId);
            stmt.setInt(2,slotId);
            stmt.setInt(3, num_tickets);
            stmt.setDouble(4, ticketPrice*num_tickets);

            int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int bookingId = generatedKeys.getInt(1);
                        for (int i : seatIds){
                            bookSeats(i, bookingId);
                        }
                        for (int i : snackIds){
                            bookSnacks(i, bookingId);
                        }
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
