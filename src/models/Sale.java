package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Sale {
    private static int nextId = 1;
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("MMM dd, yyyy  hh:mm a");

    private int saleId;
    private String movieTitle;
    private double amount;
    private String cashierName;
    private List<String> seatLabels;
    private int quantity;
    private LocalDateTime dateTime;

    public Sale(String movieTitle, double amount,
                String cashierName, List<String> seatLabels) {
        this.saleId = nextId++;
        this.movieTitle = movieTitle;
        this.amount = amount;
        this.cashierName = cashierName;
        this.seatLabels = seatLabels;
        this.quantity = seatLabels != null ? seatLabels.size() : 1;
        this.dateTime = LocalDateTime.now();
    }

    public int getSaleId()             { return saleId; }
    public String getMovieTitle()      { return movieTitle; }
    public double getAmount()          { return amount; }
    public String getCashierName()     { return cashierName; }
    public List<String> getSeatLabels(){ return seatLabels; }
    public int getQuantity()           { return quantity; }
    public LocalDateTime getDateTime() { return dateTime; }

    public String getSeatsDisplay() {
        if (seatLabels == null || seatLabels.isEmpty()) return "-";
        return String.join(", ", seatLabels);
    }

    public String getDateTimeFormatted() {
        return dateTime.format(FMT);
    }

    public boolean isToday() {
        return dateTime.toLocalDate().equals(LocalDateTime.now().toLocalDate());
    }
}