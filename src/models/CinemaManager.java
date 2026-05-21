package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CinemaManager {
    private static CinemaManager instance;

    private ObservableList<Movie>           movies           = FXCollections.observableArrayList();
    private ObservableList<Showtime>        showtimes        = FXCollections.observableArrayList();
    private ObservableList<TheaterRoom>     rooms            = FXCollections.observableArrayList();
    private ObservableList<DeletedMovie>    deletedMovies    = FXCollections.observableArrayList();
    private ObservableList<DeletedShowtime> deletedShowtimes = FXCollections.observableArrayList();
    private ObservableList<DeletedRoom>     deletedRooms     = FXCollections.observableArrayList();

    private CinemaManager() {

        // ── Rooms ─────────────────────────────────────────────────────────────
        TheaterRoom r1 = new TheaterRoom("Cinema 1", TheaterRoom.STANDARD, 6, 10);
        TheaterRoom r2 = new TheaterRoom("Cinema 2", TheaterRoom.STANDARD, 6, 10);
        TheaterRoom r3 = new TheaterRoom("Cinema 3", TheaterRoom.PREMIUM,  7, 12);
        TheaterRoom r4 = new TheaterRoom("Cinema 4", TheaterRoom.IMAX,     8, 14);
        rooms.addAll(r1, r2, r3, r4);

        // ── Movies ────────────────────────────────────────────────────────────
        Movie m1 = new Movie("Dune: Part Three",
                "Sci-Fi", 155, "PG-13",
                "Paul Atreides leads the final chapter of his legendary journey across the sands of Arrakis.", 2025);

        Movie m2 = new Movie("Avengers: Secret Wars",
                "Action", 172, "PG-13",
                "Earth's mightiest heroes face their greatest threat yet as realities collide across the multiverse.", 2025);

        Movie m3 = new Movie("The Midnight Crisis",
                "Thriller", 118, "R",
                "A detective unravels a web of conspiracies that puts her own life on the line.", 2025);

        Movie m4 = new Movie("Neon Horizons",
                "Sci-Fi", 132, "PG-13",
                "In a cyberpunk future, a programmer discovers she can rewrite the laws of reality.", 2025);

        Movie m5 = new Movie("A Quiet Place IV",
                "Horror", 97, "PG-13",
                "Survival becomes even more desperate as the creatures evolve and the safe zones disappear.", 2025);

        Movie m6 = new Movie("Inside Out 3",
                "Animation", 105, "PG",
                "Riley navigates college life as her emotions face new challenges they were never built for.", 2025);

        Movie m7 = new Movie("Heart of the City",
                "Romance", 112, "PG-13",
                "Two strangers meet by chance in a city that never sleeps and change each other's lives forever.", 2024);

        Movie m8 = new Movie("Crimson Tide II",
                "Action", 140, "R",
                "A rogue submarine commander must be stopped before a tactical nuclear strike changes history.", 2025);

        movies.addAll(m1, m2, m3, m4, m5, m6, m7, m8);

        // ── Showtimes (May 21–23, 2026) ───────────────────────────────────────
        showtimes.add(new Showtime(m1, r4, "May 21, 2026", "10:00 AM", 450.0));
        showtimes.add(new Showtime(m2, r3, "May 21, 2026", "01:00 PM", 420.0));
        showtimes.add(new Showtime(m3, r1, "May 21, 2026", "03:30 PM", 350.0));
        showtimes.add(new Showtime(m4, r2, "May 21, 2026", "06:00 PM", 370.0));
        showtimes.add(new Showtime(m5, r1, "May 21, 2026", "08:30 PM", 340.0));
        showtimes.add(new Showtime(m6, r2, "May 21, 2026", "11:00 AM", 300.0));

        showtimes.add(new Showtime(m7, r1, "May 22, 2026", "10:30 AM", 320.0));
        showtimes.add(new Showtime(m8, r2, "May 22, 2026", "01:30 PM", 380.0));
        showtimes.add(new Showtime(m1, r3, "May 22, 2026", "04:00 PM", 430.0));
        showtimes.add(new Showtime(m2, r4, "May 22, 2026", "07:00 PM", 450.0));

        showtimes.add(new Showtime(m3, r1, "May 23, 2026", "11:00 AM", 350.0));
        showtimes.add(new Showtime(m4, r3, "May 23, 2026", "02:00 PM", 390.0));
        showtimes.add(new Showtime(m5, r2, "May 23, 2026", "05:00 PM", 340.0));
        showtimes.add(new Showtime(m6, r4, "May 23, 2026", "07:30 PM", 330.0));
    }

    public static CinemaManager getInstance() {
        if (instance == null) instance = new CinemaManager();
        return instance;
    }

    // ── Movies ─────────────────────────────────────────────────────────────────
    public ObservableList<Movie>       getMovies()    { return movies; }
    public void addMovie(Movie m) { movies.add(m); }

    public void deleteMovie(Movie m, String deletedBy) {
        deletedMovies.add(new DeletedMovie(m, deletedBy));
        movies.remove(m);
    }

    public void restoreMovie(DeletedMovie dm) {
        deletedMovies.remove(dm);
        movies.add(dm.getMovie());
    }

    public ObservableList<DeletedMovie> getDeletedMovies() { return deletedMovies; }

    // ── Showtimes ──────────────────────────────────────────────────────────────
    public ObservableList<Showtime> getShowtimes() { return showtimes; }
    public void addShowtime(Showtime s) { showtimes.add(s); }

    public void deleteShowtime(Showtime s, String deletedBy) {
        deletedShowtimes.add(new DeletedShowtime(s, deletedBy));
        showtimes.remove(s);
    }

    public void restoreShowtime(DeletedShowtime ds) {
        deletedShowtimes.remove(ds);
        showtimes.add(ds.getShowtime());
    }

    public ObservableList<DeletedShowtime> getDeletedShowtimes() { return deletedShowtimes; }

    // ── Rooms ──────────────────────────────────────────────────────────────────
    public ObservableList<TheaterRoom> getRooms() { return rooms; }
    public void addRoom(TheaterRoom r) { rooms.add(r); }

    public void deleteRoom(TheaterRoom r, String deletedBy) {
        deletedRooms.add(new DeletedRoom(r, deletedBy));
        rooms.remove(r);
    }

    public void restoreRoom(DeletedRoom dr) {
        deletedRooms.remove(dr);
        rooms.add(dr.getRoom());
    }

    public ObservableList<DeletedRoom> getDeletedRooms() { return deletedRooms; }
}