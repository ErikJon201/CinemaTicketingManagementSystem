package models;

public class Showtime {
    private Movie movie;
    private String time;
    private double price;
    private boolean[][] seats;

    public Showtime(Movie movie, String time, double price, int rows, int cols) {
        this.movie = movie;
        this.time = time;
        this.price = price;
        this.seats = new boolean[rows][cols];
    }

    // ADD THIS METHOD HERE:
    public void bookSeat(int row, int col) {
        // Logic: Set the seat at the specific row and column to true (booked)
        if (row >= 0 && row < seats.length && col >= 0 && col < seats[0].length) {
            seats[row][col] = true;
        }
    }

    // Getters and other methods...
    public Movie getMovie() { return movie; }
    public String getTime() { return time; }
    public double getPrice() { return price; }
    public boolean[][] getSeats() { return seats; }
    public String getMovieTitle() { return movie.getTitle(); }

    @Override
    public String toString() {
        return movie.getTitle() + " @ " + time;
    }
}