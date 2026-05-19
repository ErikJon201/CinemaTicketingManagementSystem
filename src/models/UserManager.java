package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserManager {
    private static UserManager instance;
    private ObservableList<User> users = FXCollections.observableArrayList();

    private UserManager() {
        users = FXCollections.observableArrayList();

        users.add(new Admin(1, "1", "1", "Admin User"));
        users.add(new Cashier(2, "1", "1", "Cashier User"));
    }

    public static UserManager getInstance() {
        if (instance == null)
            instance = new UserManager();
        return instance;
    }

    public ObservableList<User> getUsers() {
        return users;
    }

    public User login(String username, String password, String role) {
        for (User u : users) {
            if (u.getUsername().equals(username) &&
                    u.getPassword().equals(password) &&
                    u.getRole().equals(role)) {
                return u;
            }
        }
        return null;
    }
}