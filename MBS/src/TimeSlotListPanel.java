import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class TimeSlotListPanel extends JPanel{
    private JPanel parentPanel;
    private JPanel prevPanel;
    private JPanel nextPanel;

    public TimeSlotListPanel(JPanel parent, JPanel prePanel) {
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
            TimeSlot[] timeSlots = listAllSlots();
            if (timeSlots.length != 0){
                for (TimeSlot ts : timeSlots) {
                JButton movieButton = new JButton(("From " + ts.getStartTime() + " to " + ts.getEndTime()));

                movieButton.addActionListener(e -> {
                    nextPanel = new AdminTimeSlotPanel(parent, prePanel, ts.getSlotId(), ts.getMovieId(), ts.getStartTime(), ts.getEndTime(), ts.getPrice());
                    parentPanel.removeAll();
                    parentPanel.add(nextPanel, BorderLayout.CENTER);
                    parentPanel.revalidate();
                    parentPanel.repaint();
                });

                add(movieButton, BorderLayout.CENTER);
                }
                add(backButton, BorderLayout.SOUTH);
            }
            else{
                JLabel msg = new JLabel("Nothing to see here");
                msg.setFont(new Font("Courier New", Font.BOLD, 20));
                msg.setHorizontalAlignment(SwingConstants.CENTER);
                msg.setVerticalAlignment(SwingConstants.CENTER);
                add(msg);
                add(backButton, BorderLayout.SOUTH);
            }

        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, "Some Error has occured, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }

    }

    public TimeSlotListPanel(JPanel parent, JPanel prePanel, boolean b) {
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
            TimeSlot[] timeSlots = listAllSlots();
            if (timeSlots.length != 0){
            for (TimeSlot ts : timeSlots) {
            JButton movieButton = new JButton(("From " + ts.getStartTime() + " to " + ts.getEndTime()));

            movieButton.addActionListener(e -> {
                nextPanel = new AdminTimeSlotPanel(parent, prePanel, ts.getSlotId(), ts.getStartTime(), ts.getEndTime());
                parentPanel.removeAll();
                parentPanel.add(nextPanel, BorderLayout.CENTER);
                parentPanel.revalidate();
                parentPanel.repaint();
            });

            add(movieButton, BorderLayout.CENTER);
        }   add(backButton, BorderLayout.SOUTH);
    }
    else{
                JLabel msg = new JLabel("Nothing to see here");
                msg.setFont(new Font("Courier New", Font.BOLD, 20));
                msg.setHorizontalAlignment(SwingConstants.CENTER);
                msg.setVerticalAlignment(SwingConstants.CENTER);
                add(msg, BorderLayout.CENTER);
                add(backButton, BorderLayout.SOUTH);
            }
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, "Some Error has occured, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }

    }
    
    public TimeSlot[] listAllSlots() {
        String sql = "SELECT * FROM TimeSlots";
        List<TimeSlot> timeSlots = new ArrayList<>();
        try (Connection conn = DBC.Connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int slotId = rs.getInt("slot_id");
                int movieID = rs.getInt("movie_id");
                String start = rs.getString("start_time");
                String end = rs.getString("end_time");
                double price = rs.getDouble("price");

                TimeSlot ts = new TimeSlot(slotId, movieID, start, end, price);
                timeSlots.add(ts);
            }
            
            return timeSlots.toArray(new TimeSlot[0]);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Some Error has occured, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
        return null;
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
}