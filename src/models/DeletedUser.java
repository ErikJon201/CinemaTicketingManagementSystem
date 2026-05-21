package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DeletedUser {
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("MMM dd, yyyy  hh:mm a");

    private final User user;
    private final String deletedBy;
    private final LocalDateTime deletedAt;

    public DeletedUser(User user, String deletedBy) {
        this.user      = user;
        this.deletedBy = deletedBy;
        this.deletedAt = LocalDateTime.now();
    }

    public User   getUser()                { return user; }
    public String getFullName()            { return user.getFullName(); }
    public String getUsername()            { return user.getUsername(); }
    public String getRole()                { return user.getRole(); }
    public String getDeletedBy()           { return deletedBy; }
    public String getDeletedAtFormatted()  { return deletedAt.format(FMT); }
    public LocalDateTime getDeletedAt()    { return deletedAt; }
}