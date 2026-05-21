package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DeletedMovie {
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("MMM dd, yyyy  hh:mm a");

    private final Movie movie;
    private final String deletedBy;
    private final LocalDateTime deletedAt;

    public DeletedMovie(Movie movie, String deletedBy) {
        this.movie     = movie;
        this.deletedBy = deletedBy;
        this.deletedAt = LocalDateTime.now();
    }

    public Movie  getMovie()             { return movie; }
    public String getTitle()             { return movie.getTitle(); }
    public String getGenre()             { return movie.getGenre(); }
    public String getRating()            { return movie.getRating(); }
    public String getDurationFormatted() { return movie.getDurationFormatted(); }
    public int    getReleaseYear()       { return movie.getReleaseYear(); }
    public String getDeletedBy()         { return deletedBy; }
    public String getDeletedAtFormatted(){ return deletedAt.format(FMT); }
    public LocalDateTime getDeletedAt()  { return deletedAt; }
}