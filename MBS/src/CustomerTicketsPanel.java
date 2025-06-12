import java.awt.*;
import javax.swing.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class CustomerTicketsPanel extends JPanel {
    private JPanel parentPanel;
    private JPanel prevPanel;
    private JPanel nextPanel;

    public CustomerTicketsPanel(JPanel parent, JPanel prePanel) {
        this.parentPanel = parent;
        this.prevPanel = prePanel;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JButton backButton = styledButton("← Back");
        JPanel ticketGrid = new JPanel(new GridLayout(3, 3, 10, 10));
        ticketGrid.setOpaque(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);

        backButton.addActionListener(e -> {
            parentPanel.removeAll();
            parentPanel.add(prevPanel, BorderLayout.CENTER);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        try {
            Ticket[] tickets = listAllTickets(UserPanel.getUser().getUserID());
            if (tickets.length != 0) {
                for (Ticket ticket : tickets) {
                    String[] seats = getSeatNos(ticket.getSeats());
                    String[] snacks = getSnackNames(ticket.getSnacks());

                    JButton ticketButton = new JButton("<html>Booking Id: " + ticket.getBookingId() +
                            "<br>Booking time: " + ticket.getBookingTime() +
                            "<br>Movie Name: " + ticket.getMovieTitle() +
                            "<br>Seats: " + arrToStr(seats) +
                            "<br>Snacks: " + arrToStr(snacks) +
                            "<br>Price: ₹" + ticket.getTotalCost() + "</html>");
                    ticketButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
                    ticketButton.setEnabled(false);
                    ticketButton.setOpaque(true);
                    ticketButton.setBackground(Color.BLUE);
                    ticketButton.setForeground(Color.BLACK);
                    ticketButton.setHorizontalAlignment(SwingConstants.LEFT);
                    ticketGrid.add(ticketButton);
                }
            } else {
                JLabel msg = new JLabel("Nothing to see here");
                msg.setFont(new Font("Courier", Font.BOLD, 16));
                msg.setHorizontalAlignment(SwingConstants.CENTER);
                ticketGrid.add(new JLabel());
                ticketGrid.add(msg);
                ticketGrid.add(new JLabel());
            }

            add(ticketGrid, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Some Error has occurred, Please try again", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton styledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBackground(Color.LIGHT_GRAY);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return button;
    }

    public Ticket[] listAllTickets(int customerId) {
        String sql = "SELECT b.*, m.title AS movie_title " +
                "FROM Bookings b " +
                "JOIN TimeSlots t ON b.slot_id = t.slot_id " +
                "JOIN Movies m ON t.movie_id = m.movie_id " +
                "WHERE b.customer_id = ?";
        List<Ticket> tickets = new ArrayList<>();

        try (Connection conn = DBC.Connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int bookingId = rs.getInt("booking_id");
                int slotId = rs.getInt("slot_id");
                String bookingTime = rs.getString("booking_time");
                int numTickets = rs.getInt("num_tickets");
                double totalCost = rs.getDouble("total_cost");
                String movieTitle = rs.getString("movie_title");

                List<Seat> seatList = getSeatsForBooking(conn, bookingId);
                List<Snack> snackList = getSnacksForBooking(conn, bookingId);

                Ticket ticket = new Ticket(bookingId, customerId, slotId, movieTitle, bookingTime, numTickets, totalCost, seatList, snackList);
                tickets.add(ticket);
            }
            return tickets.toArray(new Ticket[0]);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Some Error has occurred", "SQLError", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    private List<Seat> getSeatsForBooking(Connection conn, int bookingId) throws SQLException {
        String sql = "SELECT s.* FROM Seats s JOIN BookingSeats bs ON s.seat_id = bs.seat_id WHERE bs.booking_id = ?";
        List<Seat> seats = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int seatId = rs.getInt("seat_id");
                int slotId = rs.getInt("slot_id");
                String seatNo = rs.getString("seat_number");
                int isBooked = rs.getInt("is_booked");

                seats.add(new Seat(seatId, slotId, seatNo, isBooked));
            }
        }
        return seats;
    }

    private List<Snack> getSnacksForBooking(Connection conn, int bookingId) throws SQLException {
        String sql = "SELECT s.snack_id, s.name, s.price, bs.quantity " +
                "FROM BookingSnacks bs " +
                "JOIN Snacks s ON bs.snack_id = s.snack_id " +
                "WHERE bs.booking_id = ?";
        List<Snack> snacks = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int snackId = rs.getInt("snack_id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");

                snacks.add(new Snack(snackId, name, price, quantity));
            }
        }
        return snacks;
    }

    public String[] getSeatNos(List<Seat> seats) {
        List<String> seatNos = new ArrayList<>();
        for (Seat seat : seats) {
            seatNos.add(seat.getSeatNumber());
        }
        return seatNos.toArray(new String[0]);
    }

    public String[] getSnackNames(List<Snack> snacks) {
        List<String> snackNames = new ArrayList<>();
        for (Snack snack : snacks) {
            if (!snackNames.contains(snack.getName())) {
                snackNames.add(snack.getName());
            }
        }
        return snackNames.toArray(new String[0]);
    }

    public String arrToStr(String[] arr) {
        StringBuilder r = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            r.append(arr[i]);
            if (i < arr.length -1) {
                r.append(", ");
            }
        }
        return r.toString();
    }
}
