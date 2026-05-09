package models;

import java.time.format.DateTimeFormatter;

public class Receipt {
    private Ticket ticket;

    public Receipt(Ticket ticket) {
        this.ticket = ticket;
    }

    // Bug checkpoint:
    public String generate() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM dd, yyyy  hh:mm a");
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("           CINETICKET RECEIPT           \n");
        sb.append("========================================\n");
        sb.append("Ticket #:   ").append(String.format("%04d", ticket.getTicketId())).append("\n");
        sb.append("Date:       ").append(ticket.getPurchaseTime().format(fmt)).append("\n");
        sb.append("Cashier:    ").append(ticket.getCashier().getFullName()).append("\n");
        sb.append("----------------------------------------\n");
        sb.append("Movie:      ").append(ticket.getShowtime().getMovieTitle()).append("\n");
        sb.append("Genre:      ").append(ticket.getShowtime().getMovieGenre()).append("\n");
        sb.append("Duration:   ").append(ticket.getShowtime().getMovieDuration()).append(" min\n");
        sb.append("Showtime:   ").append(ticket.getShowtime().getTime()).append("\n");
        sb.append("Room:       ").append(ticket.getShowtime().getRoomName()).append("\n");
        sb.append("----------------------------------------\n");
        sb.append("Seats:      ").append(String.join(", ", ticket.getSeatLabels())).append("\n");
        sb.append("Price/seat: PHP ").append(String.format("%.2f", ticket.getShowtime().getPrice())).append("\n");
        sb.append("# of seats: ").append(ticket.getSeatLabels().size()).append("\n");
        sb.append("----------------------------------------\n");
        sb.append("TOTAL:      PHP ").append(String.format("%.2f", ticket.getTotalAmount())).append("\n");
        sb.append("========================================\n");
        sb.append("       Thank you for choosing us!       \n");
        sb.append("========================================\n");
        return sb.toString();
    }

    public Ticket getTicket() { return ticket; }
}