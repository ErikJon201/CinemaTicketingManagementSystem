package models;

public class Movie {
    private static int nextId = 1;

    private int movieId;
    private String title;
    private String genre;
    private int duration;
    private String rating;
    private String description;
    private int releaseYear;

    public Movie(String title, String genre, int duration,
                 String rating, String description, int releaseYear) {
        this.movieId = nextId++;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.description = description;
        this.releaseYear = releaseYear;
    }

    // Convenience constructor for quick creation (no description/rating)
    public Movie(String title, String genre, int duration) {
        this(title, genre, duration, "PG-13", "", 2025);
    }

    public int getMovieId()        { return movieId; }
    public String getTitle()       { return title; }
    public String getGenre()       { return genre; }
    public int getDuration()       { return duration; }
    public String getRating()      { return rating; }
    public String getDescription() { return description; }
    public int getReleaseYear()    { return releaseYear; }

    public void setTitle(String t)       { this.title = t; }
    public void setGenre(String g)       { this.genre = g; }
    public void setDuration(int d)       { this.duration = d; }
    public void setRating(String r)      { this.rating = r; }
    public void setDescription(String d) { this.description = d; }
    public void setReleaseYear(int y)    { this.releaseYear = y; }

    public String getDurationFormatted() {
        return (duration / 60) + "h " + (duration % 60) + "m";
    }

    @Override
    public String toString() { return title; }
}