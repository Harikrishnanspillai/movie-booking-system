import java.util.List;

public class Ticket {
    private int bookingId, customerId, slotId, numTickets;
    private String bookingTime, movieTitle;
    private double totalCost;
    private List<Seat> seats;
    private List<Snack> snacks;

    public Ticket(int bookingId, int customerId, int slotId, String movieTitle, String bookingTime, int numTickets, double totalCost, List<Seat> seats, List<Snack> snacks) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.slotId = slotId;
        this.movieTitle = movieTitle;
        this.bookingTime = bookingTime;
        this.numTickets = numTickets;
        this.totalCost = totalCost;
        this.seats = seats;
        this.snacks = snacks;
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getSlotId() {
        return slotId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public int getNumTickets() {
        return numTickets;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public List<Snack> getSnacks() {
        return snacks;
    }

    
}
