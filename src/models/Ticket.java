package models;

import java.time.LocalDateTime;
import java.util.List;

public class Ticket {
    private static int counter = 1;
    private int ticketId;
    private Showtime showtime;
    private List<String> seatLabels;
    private double totalAmount;
    private LocalDateTime purchaseTime;
    private Cashier cashier;

    public Ticket(Showtime showtime, List<String> seatLabels, double totalAmount, Cashier cashier) {
        this.ticketId     = counter++;
        this.showtime     = showtime;
        this.seatLabels   = seatLabels;
        this.totalAmount  = totalAmount;
        this.purchaseTime = LocalDateTime.now();
        this.cashier      = cashier;
    }

    public int getTicketId()          { return ticketId; }
    public Showtime getShowtime()     { return showtime; }
    public List<String> getSeatLabels() { return seatLabels; }
    public double getTotalAmount()    { return totalAmount; }
    public LocalDateTime getPurchaseTime() { return purchaseTime; }
    public Cashier getCashier()       { return cashier; }
}
