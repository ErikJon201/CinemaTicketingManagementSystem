package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserManager {
    private static UserManager instance;
    private ObservableList<User> users = FXCollections.observableArrayList();
    private int nextId = 1;

    private UserManager() {
        users.add(new Admin(nextId++, "admin", "admin123", "System Administrator"));
        users.add(new Cashier(nextId++, "cashier1", "cash123", "John Doe"));
    }

    public static UserManager getInstance() {
        if (instance == null) instance = new UserManager();
        return instance;
    }

    public ObservableList<User> getUsers() { return users; }

    public int generateId() { return nextId++; }

    public boolean usernameExists(String username) {
        for (User u : users)
            if (u.getUsername().equalsIgnoreCase(username)) return true;
        return false;
    }

    public User login(String username, String password, String role) {
        for (User u : users)
            if (u.getUsername().equals(username) &&
                u.getPassword().equals(password) &&
                u.getRole().equals(role))
                return u;
        return null;
    }
}