import java.awt.*;
import javax.swing.*;

public class AdminPanel extends JPanel{
    private JPanel parentPanel;
    private JPanel prevPanel;
    private JPanel nextPanel;

    public AdminPanel(User u, JPanel parent, JPanel prePanel){
        this.parentPanel = parent;
        this.prevPanel = prePanel;
        setLayout(new GridLayout(0, 1, 5, 5));
        JLabel welcome = new JLabel(String.format("Welcome, %s", u.getName()));
        welcome.setFont(new Font("Courier New", Font.ITALIC, 18));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcome.setHorizontalAlignment(SwingConstants.CENTER);

        JButton addMovie = new JButton("Add Movie");
        JButton editMovie = new JButton("Edit Movie");
        JButton removeMovie = new JButton("Remove Movie");
        JButton addSnack = new JButton("Add Snack");
        JButton editSnack = new JButton("Edit Snack");
        JButton removeSnack = new JButton("Remove Snack");
        JButton addTimeSlot = new JButton("Add Time Slot");
        JButton editTimeSlot = new JButton("Edit Time Slot");
        JButton removeTimeSlot = new JButton("Remove Time Slot");
        JButton backButton = new JButton("Logout");

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

}