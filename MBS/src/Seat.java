public class Seat {
    private int seatId;
    private int screenId;
    private String seatNumber;

    public Seat(int seatId, int screenId, String seatNumber) {
        this.seatId = seatId;
        this.screenId = screenId;
        this.seatNumber = seatNumber;
    }
    public int getSeatId() {
        return seatId;
    }
    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }
    public int getScreenId() {
        return screenId;
    }
    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }
    public String getSeatNumber() {
        return seatNumber;
    }
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
}