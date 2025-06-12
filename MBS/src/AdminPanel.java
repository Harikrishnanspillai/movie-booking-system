import java.awt.*;
import javax.swing.*;

public class AdminPanel extends JPanel{
    private JPanel parentPanel;
    private JPanel prevPanel;
    private JPanel nextPanel;

    public AdminPanel(User u, JPanel parent, JPanel prePanel){
        this.parentPanel = parent;
        this.prevPanel = prePanel;
        setLayout(new GridLayout(0, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JLabel welcome = new JLabel(String.format("Welcome, %s", u.getName()));
        welcome.setFont(new Font("SansSerif", Font.ITALIC, 18));
        welcome.setHorizontalAlignment(SwingConstants.CENTER);
        welcome.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JButton addMovie = styledButton("Add Movie");
        JButton editMovie = styledButton("Edit Movie");
        JButton removeMovie = styledButton("Remove Movie");

        JButton addSnack = styledButton("Add Snack");
        JButton editSnack = styledButton("Edit Snack");
        JButton removeSnack = styledButton("Remove Snack");

        JButton addTimeSlot = styledButton("Add Time Slot");
        JButton editTimeSlot = styledButton("Edit Time Slot");
        JButton removeTimeSlot = styledButton("Remove Time Slot");

        JButton backButton = styledButton("Logout");

        backButton.addActionListener(e -> {
            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        addMovie.addActionListener((e) -> {
            nextPanel = new AdminMoviePanel(parentPanel, this);
            parentPanel.removeAll();
            parentPanel.add(nextPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        editMovie.addActionListener((e) ->{
            nextPanel = new MovieListPanel(parentPanel, this);
            parentPanel.removeAll();
            parentPanel.add(nextPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        removeMovie.addActionListener((e) ->{
            nextPanel = new MovieListPanel(parentPanel, this, true);
            parentPanel.removeAll();
            parentPanel.add(nextPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        addSnack.addActionListener((e) -> {
            nextPanel = new AdminSnackPanel(parentPanel, this);
            parentPanel.removeAll();
            parentPanel.add(nextPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        editSnack.addActionListener((e) ->{
            nextPanel = new SnackListPanel(parentPanel, this);
            parentPanel.removeAll();
            parentPanel.add(nextPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        removeSnack.addActionListener((e) ->{
            nextPanel = new SnackListPanel(parentPanel, this, true);
            parentPanel.removeAll();
            parentPanel.add(nextPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        addTimeSlot.addActionListener((e) -> {
            nextPanel = new AdminTimeSlotPanel(parentPanel, this);
            parentPanel.removeAll();
            parentPanel.add(nextPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        editTimeSlot.addActionListener((e) ->{
            nextPanel = new TimeSlotListPanel(parentPanel, this);
            parentPanel.removeAll();
            parentPanel.add(nextPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        removeTimeSlot.addActionListener((e) ->{
            nextPanel = new TimeSlotListPanel(parentPanel, this, true);
            parentPanel.removeAll();
            parentPanel.add(nextPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        add(welcome);
        add(addMovie);
        add(editMovie);
        add(removeMovie);
        add(addSnack);
        add(editSnack);
        add(removeSnack);
        add(addTimeSlot);
        add(editTimeSlot);
        add(removeTimeSlot);
        add(backButton);
    }

    private JButton styledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setBackground(Color.LIGHT_GRAY);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return button;
    }
}

