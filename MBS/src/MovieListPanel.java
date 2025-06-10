
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
public class MovieListPanel extends JPanel{
    private JPanel parentPanel;
    private JPanel prevPanel;
    private JPanel nextPanel;

    public MovieListPanel(JPanel parent, JPanel prePanel) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;

        setLayout(new GridLayout(3, 3, 10, 10));

        try {
            Movie[] movies = listAllMovies();
            for (Movie movie : movies) {
            JButton movieButton = new JButton(movie.getTitle());

            movieButton.addActionListener(e -> {
                nextPanel = new AdminMoviePanel(parent, prePanel, movie.getId(), movie.getTitle(), movie.getGenre(), movie.getDuration(), movie.getLanguage());
                parentPanel.removeAll();
                parentPanel.add(nextPanel, BorderLayout.CENTER);
                parentPanel.revalidate();
                parentPanel.repaint();
            });

            add(movieButton);
        }
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, "Some Error has occured, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }

    }

    public MovieListPanel(JPanel parent, JPanel prePanel, boolean b) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;

        setLayout(new GridLayout(3, 3, 10, 10));

        try {
            Movie[] movies = listAllMovies();
            for (Movie movie : movies) {
            JButton movieButton = new JButton(movie.getTitle());

            movieButton.addActionListener(e -> {
                nextPanel = new AdminMoviePanel(parent, prePanel, movie.getId(), movie.getTitle());
                parentPanel.removeAll();
                parentPanel.add(nextPanel, BorderLayout.CENTER);
                parentPanel.revalidate();
                parentPanel.repaint();
            });

            add(movieButton);
        }
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, "Some Error has occured, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }

    }
    
    public Movie[] listAllMovies() {
        String sql = "SELECT * FROM Movies";
        List<Movie> movies = new ArrayList<>();
        try (Connection conn = DBC.Connect(); 
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("movie_id");
                String title = rs.getString("title");
                String genre = rs.getString("genre");
                int duration = rs.getInt("duration");
                String language = rs.getString("lang");

                Movie movie = new Movie(id, title, genre, duration, language);
                movies.add(movie);
            }
            
            return movies.toArray(new Movie[0]);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Some Error has occured, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}
