package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CinemaManager {
    private static CinemaManager instance;

    private ObservableList<Movie> movies = FXCollections.observableArrayList();
    private ObservableList<Showtime> showtimes = FXCollections.observableArrayList();

    private CinemaManager() {
        Movie m1 = new Movie("Inception", "Sci-Fi", 148);
        movies.add(m1);

        showtimes.add(new Showtime(m1, "Cinema 1", "10:00 AM", 350.0, 5, 8));
    }

    public static CinemaManager getInstance() {
        if (instance == null) instance = new CinemaManager();
        return instance;
    }

    public ObservableList<Movie> getMovies() {
        return movies;
    }

    public ObservableList<Showtime> getShowtimes() {
        return showtimes;
    }

    public void addShowtime(Showtime showtime) {
        showtimes.add(showtime);
    }

    public void deleteShowtime(Showtime showtime) {
        showtimes.remove(showtime);
    }

    public void addMovie(Movie movie) {
        movies.add(movie);
    }

    public void deleteMovie(Movie movie) {
        movies.remove(movie);
    }
}