package models;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static UserManager instance;
    private List<Cashier> cashiers = new ArrayList<>();

    private UserManager() {
        cashiers.add(new Cashier(1, "cashier1", "cash123"));
    }

    public static UserManager getInstance() {
        if (instance == null) instance = new UserManager();
        return instance;
    }

    public Cashier login(String username, String password) {
        for (Cashier c : cashiers) {
            if (c.getUsername().equals(username) && c.getPassword().equals(password)) {
                return c;
            }
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