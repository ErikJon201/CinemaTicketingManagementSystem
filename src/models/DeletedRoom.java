package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DeletedRoom {
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("MMM dd, yyyy  hh:mm a");

    private final TheaterRoom room;
    private final String deletedBy;
    private final LocalDateTime deletedAt;

    public DeletedRoom(TheaterRoom room, String deletedBy) {
        this.room      = room;
        this.deletedBy = deletedBy;
        this.deletedAt = LocalDateTime.now();
    }

    public TheaterRoom getRoom()            { return room; }
    public String      getName()            { return room.getName(); }
    public String      getType()            { return room.getType(); }
    public int         getRows()            { return room.getRows(); }
    public int         getCols()            { return room.getCols(); }
    public int         getCapacity()        { return room.getCapacity(); }
    public String      getDeletedBy()       { return deletedBy; }
    public String      getDeletedAtFormatted(){ return deletedAt.format(FMT); }
    public LocalDateTime getDeletedAt()     { return deletedAt; }
}