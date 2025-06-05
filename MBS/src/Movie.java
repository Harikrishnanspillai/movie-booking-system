public class Movie {
    private int id;
    private String title, genre, language;
    private int duration;
    private static int count=0;

    public Movie(int id, String title, String genre, String language, int duration) {
        this.id = (count+=1);
        this.title = title;
        this.genre = genre;
        this.language = language;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getGenre() { 
        return genre;
    }
    public String getLanguage() {
        return language;
    }
    public int getDuration() {
        return duration;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
}