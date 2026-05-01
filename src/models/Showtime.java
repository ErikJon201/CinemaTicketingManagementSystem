package models;

import java.util.Date;

public class Showtime {
    private String showtimeID;
    private Date date;
    private String time; // e.g. "10:00 AM", "2:30 PM"
    private double ticketPrice;

    //Constructors
    public Showtime() {
    }

    public Showtime(String showtimeID, Date date, String time, double ticketPrice) {
        this.showtimeID = showtimeID;
        this.date = date;
        this.time = time;
        this.ticketPrice = ticketPrice;
    }

    //Getters 
    public String getShowtimeID() {
        return showtimeID;
    }

    public Date getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    //Setters 
    public void setShowtimeID(String showtimeID) {
        this.showtimeID = showtimeID;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s %s | P%.2f",
                showtimeID, date, time, ticketPrice);
    }
}