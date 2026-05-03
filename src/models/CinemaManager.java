package models;
import java.util.ArrayList;
import java.util.List;

public class CinemaManager {
    private static CinemaManager instance;
    private List<Movie> movies = new ArrayList<>();
    private List<Showtime> showtimes = new ArrayList<>();

    private CinemaManager() {
        // Sample Data
        Movie m1 = new Movie("Inception", "Sci-Fi", 148);
        Movie m2 = new Movie("The Lion King", "Animation", 118);
        movies.add(m1);
        movies.add(m2);

        showtimes.add(new Showtime(m1, "10:00 AM", 250.0, 5, 8));
        showtimes.add(new Showtime(m1, "02:00 PM", 250.0, 5, 8));
        showtimes.add(new Showtime(m2, "11:00 AM", 200.0, 5, 8));
    }

    public static CinemaManager getInstance() {
        if (instance == null) instance = new CinemaManager();
        return instance;
    }

    public List<Movie> getMovies() { return movies; }
    public List<Showtime> getShowtimes() { return showtimes; }
}
