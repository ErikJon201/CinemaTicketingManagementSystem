package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CinemaManager {
    private static CinemaManager instance;

    private ObservableList<Movie> movies = FXCollections.observableArrayList();
    private ObservableList<Showtime> showtimes = FXCollections.observableArrayList();
    private ObservableList<TheaterRoom> rooms = FXCollections.observableArrayList(); // ← NEW

    private CinemaManager() {

        // ── Default rooms ────────────────────────────────────────────
        TheaterRoom r1 = new TheaterRoom("Cinema 1", 5, 8);
        TheaterRoom r2 = new TheaterRoom("Cinema 2", 5, 8);
        TheaterRoom r3 = new TheaterRoom("Cinema 3", 6, 9);
        TheaterRoom r4 = new TheaterRoom("Cinema 4", 6, 9);
        rooms.addAll(r1, r2, r3, r4);

        // ── Movies ───────────────────────────────────────────────────
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

        
        showtimes.add(new Showtime(m1, r1, "May 16, 2026", "10:00 AM", 350.0));
        showtimes.add(new Showtime(m2, r2, "May 16, 2026", "12:00 PM", 380.0));
        showtimes.add(new Showtime(m3, r3, "May 16, 2026", "02:00 PM", 400.0));
        showtimes.add(new Showtime(m4, r4, "May 16, 2026", "04:00 PM", 420.0));
        showtimes.add(new Showtime(m5, r1, "May 17, 2026", "10:00 AM", 350.0));
        showtimes.add(new Showtime(m6, r2, "May 17, 2026", "12:00 PM", 380.0));
        showtimes.add(new Showtime(m7, r3, "May 17, 2026", "02:00 PM", 400.0));
        showtimes.add(new Showtime(m8, r4, "May 17, 2026", "04:00 PM", 420.0));
        showtimes.add(new Showtime(m9, r1, "May 18, 2026", "10:00 AM", 350.0));
        showtimes.add(new Showtime(m10, r2, "May 18, 2026", "12:00 PM", 380.0));
        showtimes.add(new Showtime(m11, r3, "May 18, 2026", "02:00 PM", 400.0));
        showtimes.add(new Showtime(m12, r4, "May 18, 2026", "04:00 PM", 420.0));
    }

    public static CinemaManager getInstance() {
        if (instance == null)
            instance = new CinemaManager();
        return instance;
    }

    // ── Movies ────────────────────────────────────────────────────────
    public ObservableList<Movie> getMovies() {
        return movies;
    }

    public void addMovie(Movie movie) {
        movies.add(movie);
    }

    public void deleteMovie(Movie movie) {
        movies.remove(movie);
    }

    // ── Showtimes ─────────────────────────────────────────────────────
    public ObservableList<Showtime> getShowtimes() {
        return showtimes;
    }

    public void addShowtime(Showtime showtime) {
        showtimes.add(showtime);
    }

    public void deleteShowtime(Showtime showtime) {
        showtimes.remove(showtime);
    }

    // ── Rooms ─────────────────────────────────────────────────────────
    public ObservableList<TheaterRoom> getRooms() {
        return rooms;
    } 

    public void addRoom(TheaterRoom room) {
        rooms.add(room);
    } 

    public void deleteRoom(TheaterRoom room) {
        rooms.remove(room);
    }
}