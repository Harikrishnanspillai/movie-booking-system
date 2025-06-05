public class BookingSeat {
    private int bookingId;
    private int seatId;

    public BookingSeat(int bookingId, int seatId) {
        this.bookingId = bookingId;
        this.seatId = seatId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }
}
