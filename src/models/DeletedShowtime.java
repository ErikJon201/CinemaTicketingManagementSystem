package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DeletedShowtime {
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("MMM dd, yyyy  hh:mm a");

    private final Showtime showtime;
    private final String deletedBy;
    private final LocalDateTime deletedAt;

    public DeletedShowtime(Showtime showtime, String deletedBy) {
        this.showtime  = showtime;
        this.deletedBy = deletedBy;
        this.deletedAt = LocalDateTime.now();
    }

    public Showtime getShowtime()          { return showtime; }
    public String   getMovieTitle()        { return showtime.getMovieTitle(); }
    public String   getRoomName()          { return showtime.getRoomName(); }
    public String   getDate()              { return showtime.getDate(); }
    public String   getTime()              { return showtime.getTime(); }
    public double   getPrice()             { return showtime.getPrice(); }
    public String   getDeletedBy()         { return deletedBy; }
    public String   getDeletedAtFormatted(){ return deletedAt.format(FMT); }
    public LocalDateTime getDeletedAt()    { return deletedAt; }
}