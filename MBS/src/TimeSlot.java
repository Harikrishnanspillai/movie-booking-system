import java.time.LocalDateTime;

public class TimeSlot {
    private int slotId;
    private Movie movie;
    private LocalDateTime startTime;
    private int screenId;

    public TimeSlot(int slotId, Movie movie, LocalDateTime startTime, int screenId) {
        this.slotId = slotId;
        this.movie = movie;
        this.startTime = startTime;
        this.screenId = screenId;
    }
    public int getSlotId() {
        return slotId;
    }
    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }
    public Movie getMovie() {
        return movie;
    }
    public void setMovie(Movie movie) {
        this.movie = movie;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public int getScreenId() {
        return screenId;
    }
    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }
}