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

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel heading = new JLabel("Timeslot List");
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
            if (timeSlots.length != 0){
                for (TimeSlot ts : timeSlots) {
                    JButton timeSlotButton = styledButton("From " + ts.getStartTime() + " to " + ts.getEndTime());
                    timeSlotButton.addActionListener(e -> {
                        nextPanel = new AdminTimeSlotPanel(parentPanel, prevPanel, ts.getSlotId(), ts.getMovieId(), ts.getStartTime(), ts.getEndTime(), ts.getPrice());
                        parentPanel.removeAll();
                        parentPanel.add(nextPanel, BorderLayout.CENTER);
                        parentPanel.revalidate();
                        parentPanel.repaint();
                    });

                    gridPanel.add(timeSlotButton);
                }
                int emptySlots = (3 - (timeSlots.length % 3)) % 3;
                for (int i = 0; i < emptySlots; i++) {
                    gridPanel.add(new JLabel());
                }
            }
            else{
                JLabel msg = new JLabel("Nothing to see here");
                msg.setFont(new Font("Courier new", Font.BOLD, 16));
                msg.setHorizontalAlignment(SwingConstants.CENTER);
                gridPanel.add(new JLabel());
                gridPanel.add(msg);
                gridPanel.add(new JLabel());
            }

        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, "Some Error has occurred, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(backButton);

        add(heading, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public TimeSlotListPanel(JPanel parent, JPanel prePanel, boolean isRemoveMode) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel heading = new JLabel("Timeslot List");
        heading.setFont(new Font("San Serif", Font.BOLD, 20));
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
            if (timeSlots.length != 0){
                for (TimeSlot ts : timeSlots) {
                    JButton timeSlotButton = styledButton("From " + ts.getStartTime() + " to " + ts.getEndTime());

                    timeSlotButton.addActionListener(e -> {
                        nextPanel = new AdminTimeSlotPanel(parentPanel, prevPanel, ts.getSlotId(), ts.getStartTime(), ts.getEndTime());
                        parentPanel.removeAll();
                        parentPanel.add(nextPanel, BorderLayout.CENTER);
                        parentPanel.revalidate();
                        parentPanel.repaint();
                    });

                    gridPanel.add(timeSlotButton);
                }
                int emptySlots = (3 - (timeSlots.length % 3)) % 3;
                for (int i = 0; i < emptySlots; i++) {
                    gridPanel.add(new JLabel());
                }
            } else{
                JLabel msg = new JLabel("Nothing to see here");
                msg.setFont(new Font("Courier New", Font.BOLD, 16));
                msg.setHorizontalAlignment(SwingConstants.CENTER);
                gridPanel.add(new JLabel());
                gridPanel.add(msg);
                gridPanel.add(new JLabel());
            }
        } catch (Exception err) {
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
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBackground(Color.LIGHT_GRAY);
        button.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
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

                timeSlots.add(new TimeSlot(slotId, movieID, start, end, price));
            }
            return timeSlots.toArray(new TimeSlot[0]);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Some Error has occurred, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
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