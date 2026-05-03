package models;
import java.util.ArrayList;
import java.util.List;

public class Showtime {
    private Movie movie;
    private String time;
    private double price;
    private boolean[][] seats; // true = occupied, false = available

    public Showtime(Movie movie, String time, double price, int rows, int cols) {
        this.movie = movie;
        this.time = time;
        this.price = price;
        this.seats = new boolean[rows][cols]; // Default all false (available)
    }

    public Movie getMovie() { return movie; }
    public String getTime() { return time; }
    public double getPrice() { return price; }
    public boolean[][] getSeats() { return seats; }

    public void bookSeat(int row, int col) {
        seats[row][col] = true;
    }
}