public class TimeSlot {
    private int slotId;
    private int screenId;
    private int movieId;
    private String startTime; //YYYY-MM-DD HH:MM:SS
    private String endTime; //YYYY-MM-DD HH:MM:SS
    private double price;

    public TimeSlot(int slotId, int screenId, int movieId, String startTime, String endTime, double price) {
        this.slotId = slotId;
        this.screenId = screenId;
        this.movieId = movieId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
    }

    public int getSlotId() {
        return slotId;
    }

    public int getScreenId() {
        return screenId;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public double getPrice() {
        return price;
    }
}
