package models;
public class Admin extends User {
    public Admin(int userId, String username, String password, String fullName) {
        super(userId, username, password, fullName, "Admin");
    }
}