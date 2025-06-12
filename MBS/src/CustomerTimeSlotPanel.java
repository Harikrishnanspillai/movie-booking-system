
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import javax.swing.*;

public class CustomerTimeSlotPanel extends JPanel{
    private JPanel parentPanel;
    private JPanel prevPanel;
    private JPanel nextPanel;
    

    public CustomerTimeSlotPanel(JPanel parent, JPanel prePanel, int newMovieId) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;

        setLayout(new GridLayout(3, 3, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel heading = new JLabel("Timeslots");
        heading.setFont(new Font("Sans Serif", Font.BOLD, 20));
        heading.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        gridPanel.setOpaque(false);

        JButton backButton = styledButton("← Back");
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
            if (timeSlots.length != 0) {
                for (TimeSlot ts : timeSlots) {
                    JButton slotButton = styledButton("From " + ts.getStartTime() + "<br>To " + ts.getEndTime() + "<br>Price: ₹" + ts.getPrice());

                    slotButton.addActionListener(e -> {
                        nextPanel = new SeatPanel(parentPanel, prevPanel, ts.getSlotId());
                        parentPanel.removeAll();
                        parentPanel.add(nextPanel, BorderLayout.CENTER);
                        parentPanel.revalidate();
                        parentPanel.repaint();
                    });

                    gridPanel.add(slotButton);
                }
                int emptySlots = (3 - (timeSlots.length % 3)) % 3;
                for (int i = 0; i < emptySlots; i++) {
                    gridPanel.add(new JLabel());
                }
                
            } else {
                JLabel msg = new JLabel("Nothing to see here");
                msg.setFont(new Font("Courier", Font.BOLD, 16));
                msg.setHorizontalAlignment(SwingConstants.CENTER);
                add(msg);
                add(backButton);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Some Error has occurred, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(backButton);

        add(heading, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton styledButton(String text) {
        JButton button = new JButton("<html>" + text + "</html>");
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBackground(Color.LIGHT_GRAY);
        button.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
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
            JOptionPane.showMessageDialog(null, "Some Error has occurred, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public TimeSlot[] findSlots(int movieId) {
        String sql = "SELECT * FROM TimeSlots where movie_id = ?";
        List<TimeSlot> timeSlots = new ArrayList<>();
        try (Connection conn = DBC.Connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql);) {
                
                stmt.setInt(1, movieId);
                ResultSet rs = stmt.executeQuery();

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
