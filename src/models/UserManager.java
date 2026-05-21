package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserManager {
    private static UserManager instance;
    private ObservableList<User>        users        = FXCollections.observableArrayList();
    private ObservableList<DeletedUser> deletedUsers = FXCollections.observableArrayList();
    private int nextId = 3;

    private UserManager() {
        users.add(new Admin(1, "admin", "admin123", "Maria Santos"));
        users.add(new Cashier(2, "cashier1", "cashier123", "Juan dela Cruz"));
    }

    public static UserManager getInstance() {
        if (instance == null) instance = new UserManager();
        return instance;
    }

    public ObservableList<User> getUsers() { return users; }

    public User login(String username, String password, String role) {
        for (User u : users) {
            if (u.getUsername().equals(username)
                    && u.getPassword().equals(password)
                    && u.getRole().equals(role)) {
                return u;
            }
        }
        return null;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public int getNextId() { return nextId++; }

    public void deleteUser(User user, String deletedBy) {
        deletedUsers.add(new DeletedUser(user, deletedBy));
        users.remove(user);
    }

    public void restoreUser(DeletedUser du) {
        deletedUsers.remove(du);
        users.add(du.getUser());
    }

    public ObservableList<DeletedUser> getDeletedUsers() { return deletedUsers; }

    public boolean usernameExists(String username, User exclude) {
        for (User u : users) {
            if (u != exclude && u.getUsername().equalsIgnoreCase(username)) return true;
        }
        return false;
    }
}