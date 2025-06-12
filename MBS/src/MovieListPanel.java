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

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel heading = new JLabel("Movie List");
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
            Movie[] movies = listAllMovies();
            if (movies != null && movies.length != 0) {
                for (Movie movie : movies) {
                    JButton movieButton = styledButton(movie.getTitle());

                    movieButton.addActionListener(e -> {
                        nextPanel = new AdminMoviePanel(parentPanel, prevPanel, movie.getId(), movie.getTitle(), movie.getGenre(), movie.getDuration(), movie.getLanguage());
                        parentPanel.removeAll();
                        parentPanel.add(nextPanel, BorderLayout.CENTER);
                        parentPanel.revalidate();
                        parentPanel.repaint();
                    });
                    gridPanel.add(movieButton);
                }
                int emptySlots = (3 - (movies.length % 3)) % 3;
                for (int i = 0; i < emptySlots; i++) {
                    gridPanel.add(new JLabel());
                }
            } else {
                JLabel msg = new JLabel("Nothing to see here");
                msg.setFont(new Font("Courier", Font.BOLD, 16));
                msg.setHorizontalAlignment(SwingConstants.CENTER);
                gridPanel.add(new JLabel());
                gridPanel.add(msg);
                gridPanel.add(new JLabel());
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

    public MovieListPanel(JPanel parent, JPanel prePanel, boolean isRemoveMode) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel heading = new JLabel("Movie List");
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
            Movie[] movies = listAllMovies();
            if (movies != null && movies.length != 0) {
                for (Movie movie : movies) {
                    JButton movieButton = styledButton(movie.getTitle());

                    movieButton.addActionListener(e -> {
                        nextPanel = new AdminMoviePanel(parentPanel, prevPanel, movie.getId(), movie.getTitle());
                        parentPanel.removeAll();
                        parentPanel.add(nextPanel, BorderLayout.CENTER);
                        parentPanel.revalidate();
                        parentPanel.repaint();
                    });

                    gridPanel.add(movieButton);
                }
                int emptySlots = (3 - (movies.length % 3)) % 3;
                for (int i = 0; i < emptySlots; i++) {
                    gridPanel.add(new JLabel());
                }
            } else {
                JLabel msg = new JLabel("Nothing to see here");
                msg.setFont(new Font("Courier New", Font.BOLD, 16));
                msg.setHorizontalAlignment(SwingConstants.CENTER);
                gridPanel.add(new JLabel());
                gridPanel.add(msg);
                gridPanel.add(new JLabel());
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
        JButton button = new JButton(text);
        button.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBackground(Color.LIGHT_GRAY);
        button.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        return button;
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

                movies.add(new Movie(id, title, genre, duration, language));
            }
            return movies.toArray(new Movie[0]);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Some Error has occurred, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}