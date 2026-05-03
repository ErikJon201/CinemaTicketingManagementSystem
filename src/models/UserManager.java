package models;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserManager {
    private static UserManager instance;
    private ObservableList<User> users = FXCollections.observableArrayList();

    private UserManager() {
        users.add(new Admin(1, "admin", "admin123", "System Administrator"));
        users.add(new Cashier(2, "cashier", "cash123", "John Doe"));
    }

    public static UserManager getInstance() {
        if (instance == null) instance = new UserManager();
        return instance;
    }

    public ObservableList<User> getUsers() { return users; }

    public User login(String username, String password, String role) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password) && u.getRole().equals(role)) {
                return u;
            }
        }
        return null;
    }
}