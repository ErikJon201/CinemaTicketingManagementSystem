package models;

public class Movie {
    private String movieID;
    private String title;
    private String genre;
    private int duration;
    private String rating;

    //Constructors
    public Movie() {
    }

    public Movie(String movieID, String title, String genre,
            int duration, String rating) {
        this.movieID = movieID;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
    }

    //Getters 
    public String getMovieID() {
        return movieID;
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

    public String getRating() {
        return rating;
    }

    //Setters 
    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s | %s | %d mins",
                movieID, title, genre, rating, duration);
    }
}
