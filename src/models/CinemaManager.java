package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CinemaManager {
    private static CinemaManager instance;

    private ObservableList<Movie> movies = FXCollections.observableArrayList();
    private ObservableList<Showtime> showtimes = FXCollections.observableArrayList();

    private CinemaManager() {
        Movie m1 = new Movie("Inception", "Sci-Fi", 148);
        Movie m2 = new Movie("The Dark Knight", "Action", 152);
        Movie m3 = new Movie("Interstellar", "Sci-Fi", 169);
        Movie m4 = new Movie("Avengers Endgame", "Action", 181);
        Movie m5 = new Movie("Inception", "Sci-Fi", 148);
        Movie m6 = new Movie("The Dark Knight", "Action", 152);
        Movie m7 = new Movie("Interstellar", "Sci-Fi", 169);
        Movie m8 = new Movie("Avengers Endgame", "Action", 181);
        Movie m9 = new Movie("Inception", "Sci-Fi", 148);
        Movie m10 = new Movie("The Dark Knight", "Action", 152);
        Movie m11 = new Movie("Inception", "Sci-Fi", 148);
        Movie m12 = new Movie("The Dark Knight", "Action", 152);

        movies.addAll(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12);

        showtimes.add(new Showtime(m1, "Cinema 1", "May 16, 2026", "10:00 AM", 350.0, 5, 8));
        showtimes.add(new Showtime(m2, "Cinema 2", "May 16, 2026", "12:00 PM", 380.0, 5, 8));
        showtimes.add(new Showtime(m3, "Cinema 3", "May 16, 2026", "02:00 PM", 400.0, 5, 8));
        showtimes.add(new Showtime(m4, "Cinema 4", "May 16, 2026", "04:00 PM", 420.0, 5, 8));
        showtimes.add(new Showtime(m5, "Cinema 1", "May 17, 2026", "10:00 AM", 350.0, 5, 8));
        showtimes.add(new Showtime(m6, "Cinema 2", "May 17, 2026", "12:00 PM", 380.0, 5, 8));
        showtimes.add(new Showtime(m7, "Cinema 3", "May 17, 2026", "02:00 PM", 400.0, 5, 8));
        showtimes.add(new Showtime(m8, "Cinema 4", "May 17, 2026", "04:00 PM", 420.0, 5, 8));
        showtimes.add(new Showtime(m9, "Cinema 1", "May 18, 2026", "10:00 AM", 350.0, 5, 8));
        showtimes.add(new Showtime(m10, "Cinema 2", "May 18, 2026", "12:00 PM", 380.0, 5, 8));
        showtimes.add(new Showtime(m11, "Cinema 3", "May 18, 2026", "02:00 PM", 400.0, 5, 8));
        showtimes.add(new Showtime(m12, "Cinema 4", "May 18, 2026", "04:00 PM", 420.0, 5, 8));
    }

    public static CinemaManager getInstance() {
        if (instance == null)
            instance = new CinemaManager();
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