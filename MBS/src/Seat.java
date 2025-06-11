public class Seat {
    private int seatId;
    private int slotId;
    private String seatNumber;
    private int isBooked;

    public Seat(int seatId, int slotId, String seatNumber, int isBooked) {
        this.seatId = seatId;
        this.slotId = slotId;
        this.seatNumber = seatNumber;
        this.isBooked = isBooked;
    }

    public int getSeatId() {
        return seatId;
    }

    public int getSlotId() {
        return slotId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public int isBooked() {
        return isBooked;
    }

    public void setBooked(int isBooked) {
        this.isBooked = isBooked;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }
}
