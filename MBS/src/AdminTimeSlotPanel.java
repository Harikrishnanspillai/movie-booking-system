import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class AdminTimeSlotPanel extends JPanel {
    private JPanel parentPanel;
    private JPanel prevPanel;
    public AdminTimeSlotPanel(JPanel parent, JPanel prePanel) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;
        setLayout(new GridLayout(5, 2, 10, 10));

        JLabel movieLabel = new JLabel("Movie:");
        JTextField movieField = new JTextField(20);

        JLabel startLabel = new JLabel("Start time (YYYY-MM-DD HH:MM:SS):");
        JTextField startField = new JTextField(20);

        JLabel endLabel = new JLabel("End time (YYYY-MM-DD HH:MM:SS):");
        JTextField endField = new JTextField(20);

        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField(20);

        JButton backButton = new JButton("Back");
        JButton submitButton = new JButton("Submit");


        submitButton.addActionListener((e) ->{
            addTimeSlot( MovieID(movieField.getText()), startField.getText(), endField.getText(), Double.parseDouble(priceField.getText().trim()));
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

        add(movieLabel);
        add(movieField);
        add(startLabel);
        add(startField);
        add(endLabel);
        add(endField);
        add(priceLabel);
        add(priceField);
        add(backButton);
        add(submitButton);
    }
    public AdminTimeSlotPanel(JPanel parent, JPanel prePanel, int slotId, int newMovieId, String newStart, String newEnd, double newPrice) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;
        setLayout(new GridLayout(6, 2, 10, 10));

        JLabel movieLabel = new JLabel("Movie:");
        JTextField movieField = new JTextField(MovieName(newMovieId), 20);

        JLabel startLabel = new JLabel("Start time (YYYY-MM-DD HH:MM:SS):");
        JTextField startField = new JTextField(newStart, 20);

        JLabel endLabel = new JLabel("End time (YYYY-MM-DD HH:MM:SS):");
        JTextField endField = new JTextField(newEnd, 20);

        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField(String.valueOf(newPrice), 20);

        JButton backButton = new JButton("Back");
        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener((e) ->{
            editTimeSlot(slotId, MovieID(movieField.getText().trim()), startField.getText(), endField.getText(), Double.parseDouble(priceField.getText().trim()));
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

        
        add(movieLabel);
        add(movieField);
        add(startLabel);
        add(startField);
        add(endLabel);
        add(endField);
        add(priceLabel);
        add(priceField);

        add(backButton);
        add(submitButton);
    }
    public AdminTimeSlotPanel(JPanel parent, JPanel prePanel ,int slotID, String start, String end) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;
        setLayout(new FlowLayout());

        JLabel ConfirmLabel = new JLabel(("Are you sure want to delete slot from, " + start + " to " + end));
        ConfirmLabel.setFont(new Font("Courier New", Font.BOLD, 18));

        JButton backButton = new JButton("Back");
        JButton submitButton = new JButton("Confirm");

        submitButton.addActionListener((e) ->{
            deleteTimeSlot(slotID);
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

        add(ConfirmLabel);
        add(backButton);
        add(submitButton);
    }

    public void addTimeSlot(int movieId, String start, String end, double price) {
        String sql = "INSERT INTO TimeSlots (movie_id, start_time, end_time, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBC.Connect(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, movieId);
            stmt.setString(2, start);
            stmt.setString(3, end);
            stmt.setDouble(4, price);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int slot_id = generatedKeys.getInt(1);
                        addSeats(slot_id);
                    }
                JOptionPane.showMessageDialog(null, "Time slot added", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Some Error has occured, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void editTimeSlot(int slotId, int newMovieId, String newStart, String newEnd, double newPrice) {
        String sql = "UPDATE TimeSlots SET movie_id = ?, start_time = ?, end_time = ?, price = ? WHERE slot_id = ?";
        try (Connection conn = DBC.Connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newMovieId);
            stmt.setString(2, newStart);
            stmt.setString(3, newEnd);
            stmt.setDouble(4, newPrice);
            stmt.setInt(5, slotId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0){
                JOptionPane.showMessageDialog(null, "Time slot edited", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(null, "Time slot not found", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Some Error has occured, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void deleteTimeSlot(int slotId) {
        String sql = "DELETE FROM TimeSlots WHERE slot_id = ?";
        deleteSeat(slotId);
        try (Connection conn = DBC.Connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, slotId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0){
                JOptionPane.showMessageDialog(null, "Time slot removed", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(null, "Time slot not found", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Some Error has occured, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String MovieName(int movieId){
        String sql = "SELECT title FROM Movies WHERE movie_id = ?";
        try (Connection conn = DBC.Connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("title");
            }
        }catch (Exception err){

        }
        return "";
    }

    public int MovieID(String movieName){
        String sql = "SELECT movie_id FROM Movies WHERE title = ?";
        try (Connection conn = DBC.Connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, movieName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("movie_id");
            }
        }catch (Exception err){

        }
        return 0;
    }

    public void addSeats(int slotId){
        try (Connection conn = DBC.Connect()){
            String[] alph = {"A", "B", "C", "D", "E", "F", "G", "H"};
            for (String i : alph){
                for (int j = 1; j<=10; j++){
                    String seatNo = String.format("%s%d", i, j);
                    String sql = "INSERT INTO SEATS (slot_id, seat_number, is_booked) VALUES(?, ?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)){
                        stmt.setInt(1, slotId);
                        stmt.setString(2, seatNo);
                        stmt.setInt(3, 0);
                        int rowsAffected = stmt.executeUpdate();
                    }
                }
            }
        }
        catch (Exception err){
            JOptionPane.showMessageDialog(null, "Some Error has occured", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteSeat(int slotId){
        String sql = "DELETE FROM Seats WHERE slot_id = ?";
        try (Connection conn = DBC.Connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, slotId);

            int affectedRows = stmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Some Error has occured", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
    }
}
