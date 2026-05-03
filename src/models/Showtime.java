package models;

public class Showtime {
    private Movie movie;
    private String roomName;
    private String time;
    private double price;
    private boolean[][] seats;

    public Showtime(Movie movie, String roomName, String time, double price, int rows, int cols) {
        this.movie = movie;
        this.roomName = roomName;
        this.time = time;
        this.price = price;
        this.seats = new boolean[rows][cols];
    }

    public Movie getMovie() { return movie; }
    public String getMovieTitle() { return movie.getTitle(); }
    public String getRoomName() { return roomName; }
    public String getTime() { return time; }
    public double getPrice() { return price; }
    public boolean[][] getSeats() { return seats; }

    public int getAvailableSeats() {
        int count = 0;
        for (boolean[] row : seats) {
            for (boolean seat : row) {
                if (!seat) count++;
            }
        }
        return count;
    }

    public void bookSeat(int r, int c) { seats[r][c] = true; }
}