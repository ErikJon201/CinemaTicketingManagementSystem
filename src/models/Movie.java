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

    // Getters and Setters
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    @Override
    public String toString() { return title + " (" + genre + ")"; }
}