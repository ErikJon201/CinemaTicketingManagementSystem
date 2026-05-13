package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CinemaManager {
    private static CinemaManager instance;

    private ObservableList<Movie> movies = FXCollections.observableArrayList();
    private ObservableList<Showtime> showtimes = FXCollections.observableArrayList();

    private CinemaManager() {
        // FIX: removed the duplicate m5–m12 entries — only 4 unique movies needed.
        Movie m1 = new Movie("Inception",        "Sci-Fi", 148);
        Movie m2 = new Movie("The Dark Knight",  "Action", 152);
        Movie m3 = new Movie("Interstellar",     "Sci-Fi", 169);
        Movie m4 = new Movie("Avengers Endgame", "Action", 181);

        movies.addAll(m1, m2, m3, m4);

        showtimes.add(new Showtime(m1, "Cinema 1", "May 12, 2026", "10:00 AM", 350.0, 5, 8));
        showtimes.add(new Showtime(m2, "Cinema 2", "May 12, 2026", "12:00 PM", 380.0, 5, 8));
        showtimes.add(new Showtime(m3, "Cinema 3", "May 12, 2026", "02:00 PM", 400.0, 5, 8));
        showtimes.add(new Showtime(m4, "Cinema 4", "May 12, 2026", "04:00 PM", 420.0, 5, 8));
        showtimes.add(new Showtime(m1, "Cinema 1", "May 13, 2026", "10:00 AM", 350.0, 5, 8));
        showtimes.add(new Showtime(m3, "Cinema 3", "May 13, 2026", "02:00 PM", 400.0, 5, 8));
        showtimes.add(new Showtime(m4, "Cinema 4", "May 13, 2026", "04:00 PM", 420.0, 5, 8));
        showtimes.add(new Showtime(m1, "Cinema 1", "May 14, 2026", "10:00 AM", 350.0, 5, 8));
        showtimes.add(new Showtime(m2, "Cinema 2", "May 14, 2026", "12:00 PM", 380.0, 5, 8));
        showtimes.add(new Showtime(m3, "Cinema 3", "May 14, 2026", "02:00 PM", 400.0, 5, 8));
        showtimes.add(new Showtime(m4, "Cinema 4", "May 14, 2026", "04:00 PM", 420.0, 5, 8));
    }

    public static CinemaManager getInstance() {
        if (instance == null) instance = new CinemaManager();
        return instance;
    }

    public ObservableList<Movie> getMovies()     { return movies; }
    public ObservableList<Showtime> getShowtimes() { return showtimes; }

    public void addShowtime(Showtime showtime)  { showtimes.add(showtime); }
    public void deleteShowtime(Showtime showtime) { showtimes.remove(showtime); }
    public void addMovie(Movie movie)            { movies.add(movie); }
    public void deleteMovie(Movie movie)         { movies.remove(movie); }
}