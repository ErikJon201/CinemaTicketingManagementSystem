package models;

import java.time.LocalDateTime;

public class Sale {
    private String movieTitle;
    private double amount;
    private LocalDateTime dateTime;

    public Sale(String movieTitle, double amount) {
        this.movieTitle = movieTitle;
        this.amount = amount;
        this.dateTime = LocalDateTime.now();
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}