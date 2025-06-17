import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class AdminMoviePanel extends JPanel {
    private JPanel parentPanel;
    private JPanel prevPanel;
    private JPanel nextPanel;
    public AdminMoviePanel(JPanel parent, JPanel prePanel) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;
        setLayout(new GridLayout(5, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField(20);

        JLabel genreLabel = new JLabel("Genre:");
        JTextField genreField = new JTextField(20);

        JLabel durationLabel = new JLabel("Duration (min):");
        JTextField durationField = new JTextField(20);

        JLabel languageLabel = new JLabel("Language:");
        JTextField languageField = new JTextField(20);

        JButton backButton = styledButton("Back");
        JButton submitButton = styledButton("Submit");

        submitButton.addActionListener((e) -> {
            addMovie(titleField.getText(), genreField.getText(), Integer.parseInt(durationField.getText().trim()), languageField.getText());
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

        add(titleLabel);
        add(titleField);
        add(genreLabel);
        add(genreField);
        add(durationLabel);
        add(durationField);
        add(languageLabel);
        add(languageField);
        add(backButton);
        add(submitButton);
    }

    public AdminMoviePanel(JPanel parent, JPanel prePanel, int MovieID, String newTitle, String newGenre, int newDuration, String newLanguage) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;
        setLayout(new GridLayout(5, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField(newTitle, 20);

        JLabel genreLabel = new JLabel("Genre:");
        JTextField genreField = new JTextField(newGenre, 20);

        JLabel durationLabel = new JLabel("Duration (min):");
        JTextField durationField = new JTextField(String.valueOf(newDuration), 20);

        JLabel languageLabel = new JLabel("Language:");
        JTextField languageField = new JTextField(newLanguage, 20);

        JButton backButton = styledButton("Back");
        JButton submitButton = styledButton("Submit");

        submitButton.addActionListener((e) -> {
            editMovie(MovieID, titleField.getText(), genreField.getText(), Integer.parseInt(durationField.getText().trim()), languageField.getText());
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

        add(titleLabel);
        add(titleField);
        add(genreLabel);
        add(genreField);
        add(durationLabel);
        add(durationField);
        add(languageLabel);
        add(languageField);
        add(backButton);
        add(submitButton);
    }
    public AdminMoviePanel(JPanel parent, JPanel prePanel, int movieID, String movieTitle) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel confirmLabel = new JLabel("Are you sure you want to delete, " + movieTitle + "?");
        confirmLabel.setFont(new Font("Courier", Font.BOLD, 16));
        
        JButton backButton = styledButton("Back");
        JButton confirmButton = styledButton("Confirm");
        
        confirmButton.addActionListener((e) -> {
            removeMovie(movieID);
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
        
        add(confirmLabel);
        add(Box.createRigidArea(new Dimension(20,0))); // spacing
        add(backButton);
        add(confirmButton);
    }

    public JButton styledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBackground(Color.LIGHT_GRAY);
        button.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        return button;
    }


    public void addMovie(String title, String genre, int duration, String language) {
        String sql = "INSERT INTO Movies (title, genre, duration, lang) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBC.Connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, genre);
            stmt.setInt(3, duration);
            stmt.setString(4, language);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Movie added", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Some Error has occured, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void editMovie(int MovieID, String newTitle, String newGenre, int newDuration, String newLanguage) {
        String sql = "UPDATE Movies SET title = ?, genre = ?, duration = ?, lang = ? WHERE movie_id = ?";
        try (Connection conn = DBC.Connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newTitle);
            stmt.setString(2, newGenre);
            stmt.setInt(3, newDuration);
            stmt.setString(4, newLanguage);
            stmt.setInt(5, MovieID);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Movie edited", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(null, "Movie not found", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Some Error has occured, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void removeMovie(int movieId){
        String msql = "DELETE FROM Movies WHERE movie_id = ?";
        deleteTimeSlot(movieId);
        try (Connection conn = DBC.Connect(); PreparedStatement stmt = conn.prepareStatement(msql)) {
            stmt.setInt(1, movieId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0){
                JOptionPane.showMessageDialog(null, "Movie removed", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(null, "Movie not found", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Some Error has occured, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
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
    public void deleteTimeSlot(int movieId) {
        String sql = "DELETE FROM TimeSlots WHERE movie_id = ?";
        try (Connection conn = DBC.Connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movieId);
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
    public void addSeats(int slotId){
        try (Connection conn = DBC.Connect()){
            String[] alph = {"A", "B", "C", "D", "E"};
            for (String i : alph){
                for (int j = 1; j<=10; j++){
                    String seatNo = String.format("%s%d", i, j);
                    String sql = "INSERT INTO SEATS (slot_id, seat_id, is_booked) VALUES(?, ? ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)){
                        stmt.setInt(1, slotId);
                        stmt.setString(2, seatNo);
                        stmt.setBoolean(3, false);
                        int rowsAffected = stmt.executeUpdate();
                    }
                }
            }
        }
        catch (Exception err){
            JOptionPane.showMessageDialog(null, "Some Error has occured, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
    }
}