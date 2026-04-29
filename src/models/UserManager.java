package models;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static UserManager instance;
    private List<Cashier> cashiers = new ArrayList<>();
    private Admin admin;

    private UserManager() {
        admin = new Admin(0, "admin", "admin123");
        cashiers.add(new Cashier(1, "cashier", "cash123"));
    }

    public static UserManager getInstance() {
        if (instance == null) instance = new UserManager();
        return instance;
    }

    public Cashier loginCashier(String username, String password) {
        for (Cashier c : cashiers) {
            if (c.getUsername().equals(username) && c.getPassword().equals(password)) {
                return c;
            }
        }
        return null;
    }

    public Admin loginAdmin(String username, String password) {
        if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
            return admin;
        }
        return null;
    }

    public boolean register(String username, String password) {
        for (Cashier c : cashiers) {
            if (c.getUsername().equals(username)) return false;
        }
        int id = cashiers.size() + 1;
        cashiers.add(new Cashier(id, username, password));
        return true;
    }

    public List<Cashier> getAllCashiers() {
        return cashiers;
    }
}