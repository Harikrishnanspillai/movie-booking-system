import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class AdminMoviePanel extends JPanel {
    private JPanel parentPanel;
    private JPanel prevPanel;
    public AdminMoviePanel(JPanel parent, JPanel prePanel) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;
        setLayout(new GridLayout(5, 2, 10, 10));

        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField(20);

        JLabel genreLabel = new JLabel("Genre:");
        JTextField genreField = new JTextField(20);

        JLabel durationLabel = new JLabel("Duration (min):");
        JTextField durationField = new JTextField(20);

        JLabel languageLabel = new JLabel("Language:");
        JTextField languageField = new JTextField(20);

        JButton backButton = new JButton("Back");
        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener((e) ->{
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

        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField(newTitle, 20);

        JLabel genreLabel = new JLabel("Genre:");
        JTextField genreField = new JTextField(newGenre, 20);

        JLabel durationLabel = new JLabel("Duration (min):");
        JTextField durationField = new JTextField(String.valueOf(newDuration), 20);

        JLabel languageLabel = new JLabel("Language:");
        JTextField languageField = new JTextField(newLanguage, 20);

        JButton backButton = new JButton("Back");
        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener((e) ->{
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
    public AdminMoviePanel(JPanel parent, JPanel prePanel ,int movieID, String movieTitle) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;
        setLayout(new FlowLayout());

        JLabel ConfirmLabel = new JLabel(("Are you sure want to delete, " + movieTitle));
        ConfirmLabel.setFont(new Font("Courier New", Font.BOLD, 18));

        JButton backButton = new JButton("Back");
        JButton submitButton = new JButton("Confirm");

        submitButton.addActionListener((e) ->{
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

        add(ConfirmLabel);
        add(backButton);
        add(submitButton);
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
        String sql = "DELETE FROM Movies WHERE movie_id = ?";
        try (Connection conn = DBC.Connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
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
}
