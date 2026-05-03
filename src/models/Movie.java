package models;

public class Movie {
    private String title;
    private String genre;
    private int durationMinutes;

    public Movie(String title, String genre, int durationMinutes) {
        this.title = title;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    @Override
    public String toString() { return title; }
}