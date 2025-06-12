import java.awt.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;

public class PaymentPanel extends JPanel {
    private JPanel parentPanel;
    private JPanel prevPanel;
    private JPanel nextPanel;

    public PaymentPanel(JPanel parent, JPanel prePanel, Integer[] seatIds) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;
        setLayout(new GridLayout(0, 1, 20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JPanel paymentPanel = new JPanel(new FlowLayout());
        paymentPanel.setBackground(Color.WHITE);

        JPanel msgPanel = new JPanel(new FlowLayout());
        
        JLabel msg = new JLabel("<html>Number of tickets:" + seatIds.length + "<br>Cost: "+ seatPrice(seatIds[0])*seatIds.length+ "</html>");
        JLabel paymentLabel = new JLabel("Enter payment details (UPI ID/Credit Card number):");
        paymentLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JTextField paymentField = new JTextField(20);
        msg.setFont(new Font("SansSerif", Font.PLAIN, 14));
        msg.setHorizontalAlignment(SwingConstants.CENTER);
        msg.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        msgPanel.add(msg);
        paymentPanel.add(paymentLabel);
        paymentPanel.add(paymentField);

        JButton backButton = styledButton("Cancel");
        JButton confirmButton = styledButton("Confirm");

        confirmButton.addActionListener(e -> {
            int bookingID = bookTickets(UserPanel.getUser().getUserID(), seatIds, paymentField.getText());
            JOptionPane.showMessageDialog(null, "Booking Successful! Booking ID: " + bookingID, "Booking", JOptionPane.INFORMATION_MESSAGE);

            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });
        backButton.addActionListener(e -> {
            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        add(msgPanel);
        add(paymentPanel);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(backButton);
        buttonPanel.add(confirmButton);
        add(buttonPanel);
    }

    public PaymentPanel(JPanel parent, JPanel prePanel, Integer[] seatIds, Integer[] snackIds) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;
        
        setLayout(new GridLayout(0, 1, 20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JPanel paymentPanel = new JPanel(new FlowLayout());
        paymentPanel.setBackground(Color.WHITE);

        JPanel msgPanel = new JPanel(new FlowLayout());
        
        JLabel msg = new JLabel("<html>Number of tickets:" + seatIds.length + "<br>Cost: "+ (seatPrice(seatIds[0])*seatIds.length+getSnackPrice(countOccurrences(snackIds)))+ "</html>");
        JLabel paymentLabel = new JLabel("Enter payment details (UPI ID/Credit Card number):");
        paymentLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JTextField paymentField = new JTextField(20);
        msg.setFont(new Font("SansSerif", Font.PLAIN, 14));
        msg.setHorizontalAlignment(SwingConstants.CENTER);
        msg.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        msgPanel.add(msg);
        paymentPanel.add(paymentLabel);
        paymentPanel.add(paymentField);

        JButton backButton = styledButton("Cancel");
        JButton confirmButton = styledButton("Confirm");

        confirmButton.addActionListener(e -> {
            int bookingID = bookTickets(UserPanel.getUser().getUserID(), seatIds, snackIds, paymentField.getText());
            JOptionPane.showMessageDialog(null, "Booking Successful! Booking ID: " + bookingID, "Booking", JOptionPane.INFORMATION_MESSAGE);

            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });
        backButton.addActionListener(e -> {
            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        add(msgPanel);
        add(paymentPanel);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(backButton);
        buttonPanel.add(confirmButton);
        add(buttonPanel);
    }

    private JButton styledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBackground(Color.LIGHT_GRAY);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return button;
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
    public int bookTickets(int userId,Integer[] seatIds, String upi){
        int slotId = 0;
        double price = 0;
        int num_tickets = seatIds.length;
        String sql = "INSERT INTO Bookings (customer_id, slot_id, num_tickets, total_cost, payment_method) VALUES (?, ?, ?, ?, ?)";
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
            stmt.setString(5, upi);

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
    public int bookTickets(int userId,Integer[] seatIds, Integer[] snackIds, String upi){
        int slotId = 0;
        double ticketPrice = 0;
        int num_tickets=seatIds.length;
        String sql = "INSERT INTO Bookings (customer_id, slot_id, num_tickets, total_cost, payment_method) VALUES (?, ?, ?, ?, ?)";
        String costsql = "SELECT price FROM TimeSlots WHERE slot_id = ?";
        String slotsql = "SELECT slot_id FROM Seats WHERE seat_id = ?";

        double snackCost = getSnackPrice(countOccurrences(snackIds));
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
            stmt.setDouble(4, ticketPrice*num_tickets+snackCost);
            stmt.setString(5, upi);

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
    
    public int[][] countOccurrences(Integer[] arr) {
        List<int[]> resultList = new ArrayList<>();

        for (int i = 0; i < arr.length; i++) {
            int current = arr[i];
            System.out.println(current);
            boolean found = false;

            for (int j = 0; j < resultList.size(); j++) {
                if (resultList.get(j)[0] == current) {
                    resultList.get(j)[1]++;
                    found = true;
                    break;
                }
            }
            if (!found) {
                resultList.add(new int[]{current, 1});
            }
        }
        int[][] result = new int[resultList.size()][2];
        for (int i = 0; i < resultList.size(); i++) {
            result[i] = resultList.get(i);
        }
        
        for (int[] pair : result) {
            System.out.println(pair[0] + " " + pair[1]);}
        return result;
    }

    public double getSnackPrice(int[][] SnackData){
        String sql = "SELECT price FROM Snacks WHERE snack_id = ?";
        double total = 0;
        try(Connection conn = DBC.Connect(); PreparedStatement stmt = conn.prepareStatement(sql)){
            for (int[] i : SnackData){
                stmt.setInt(1, i[0]);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()){
                    double cost = rs.getDouble(1);
                    System.out.println(cost);
                    total += (cost*i[1]);
                }
            }
        }
        catch (SQLException err){
            JOptionPane.showMessageDialog(null, "Some Error has occured, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
        return total;
    }
    public double seatPrice(int seatId){
        int slotId = 0;
        double price = 0;
        String slotsql = "SELECT slot_id FROM Seats WHERE seat_id = ?";
        String costsql = "SELECT price FROM TimeSlots WHERE slot_id = ?";
        try(Connection conn = DBC.Connect()){
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
        }
        catch (SQLException err){
        }
        return price;
    }
}
