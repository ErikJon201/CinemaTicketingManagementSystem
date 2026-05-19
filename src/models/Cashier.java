package models;

public class Cashier extends User {
    public Cashier(int userId, String username, String password, String fullName) {
        super(userId, username, password, fullName, "Cashier");
    }
}
