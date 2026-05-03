package models;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CinemaManager {
    private static CinemaManager instance;
    // Use ObservableList for automatic UI updates
    private ObservableList<Movie> movies = FXCollections.observableArrayList();
    private ObservableList<Showtime> showtimes = FXCollections.observableArrayList();

    private CinemaManager() {
        movies.add(new Movie("Inception", "Sci-Fi", 148));
        movies.add(new Movie("The Lion King", "Animation", 118));
    }

    public static CinemaManager getInstance() {
        if (instance == null) instance = new CinemaManager();
        return instance;
    }

    public ObservableList<Movie> getMovies() { return movies; }

    public void addMovie(Movie movie) {
        movies.add(movie);
    }

    public void deleteMovie(Movie movie) {
        movies.remove(movie);
    }

    public ObservableList<Showtime> getShowtimes() {
        return showtimes;
    }

    public void addShowtime(Showtime st) {
        showtimes.add(st);
    }

    public void deleteShowtime(Showtime st) {
        showtimes.remove(st);
    }

    // Update is handled by modifying the object directly or replacing it
    public void updateMovie(int index, Movie updatedMovie) {
        movies.set(index, updatedMovie);
    }
}