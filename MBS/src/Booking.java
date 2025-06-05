import java.time.LocalDateTime;
import java.util.List;

public class Booking {
    private int bookingId;
    private Customer customer;
    private TimeSlot slot;
    private LocalDateTime bookingTime;
    private double totalCost;
    private List<Seat> seats;
    private List<SnackOrder> snacks;

    public Booking(int bookingId, Customer customer, TimeSlot slot, LocalDateTime bookingTime, double totalCost, List<Seat> seats, List<SnackOrder> snacks) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.slot = slot;
        this.bookingTime = bookingTime;
        this.totalCost = totalCost;
        this.seats = seats;
        this.snacks = snacks;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public TimeSlot getSlot() {
        return slot;
    }

    public void setSlot(TimeSlot slot) {
        this.slot = slot;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public List<SnackOrder> getSnacks() {
        return snacks;
    }

    public void setSnacks(List<SnackOrder> snacks) {
        this.snacks = snacks;
    }
}
