package models;

public class Showtime {
    private Movie movie;
    private TheaterRoom room;
    private String date;
    private String time;
    private double price;
    private boolean[][] seats;

  public Showtime(Movie movie, TheaterRoom room, String date, String time, double price) {
    this.movie = movie;
    this.room  = room;
    this.date  = date;
    this.time  = time;
    this.price = price;
    this.seats = new boolean[room.getRows()][room.getCols()];
}

    // ── Getters ──────────────────────────────────────────────────────
    public Movie getMovie() {
        return movie;
    }

    public String getMovieTitle() {
        return movie.getTitle();
    }

    public String getMovieGenre() {
        return movie.getGenre();
    }

    public int getMovieDuration() {
        return movie.getDuration();
    }

   public String getRoomName() {
    return room.getName();
}

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public double getPrice() {
        return price;
    }

    public boolean[][] getSeats() {
        return seats;
    }

    public String getDateTime() {
        return date + " \u2013 " + time;
    }

    public int getAvailableSeats() {
        int count = 0;
        for (boolean[] row : seats)
            for (boolean seat : row)
                if (!seat)
                    count++;
        return count;
    }

    public TheaterRoom getRoom() {
    return room;
}

    // ── Setters ──────────────────────────────────────────────────────
    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void bookSeat(int r, int c) {
        seats[r][c] = true;
    }

    public void setRoom(TheaterRoom room) {
    this.room  = room;
    this.seats = new boolean[room.getRows()][room.getCols()];
}

public void setMovie(Movie movie) {
    this.movie = movie;
}

}