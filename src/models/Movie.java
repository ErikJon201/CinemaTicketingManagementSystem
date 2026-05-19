package models;

public class Movie {
    private String title;
    private String genre;
    private int duration; // in minutes

    public Movie(String title, String genre, int duration) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setTitle(String t) {
        title = t;
    }

    public void setGenre(String g) {
        genre = g;
    }

    public void setDuration(int d) {
        duration = d;
    }

    @Override
    public String toString() {
        return title;
    }
}