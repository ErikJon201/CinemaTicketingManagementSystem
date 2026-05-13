package controllers;

import models.*;
import javafx.scene.control.Label;

public class UserController {

    public void handleAddUser(String fullName, String username, String password, String role, Label statusLabel) {
        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || role == null) {
            showError(statusLabel, "All fields are required.");
            return;
        }
        if (UserManager.getInstance().usernameExists(username)) {
            showError(statusLabel, "Username already exists.");
            return;
        }

        int newId = UserManager.getInstance().generateId();
        User newUser = role.equals("Admin")
            ? new Admin(newId, username, password, fullName)
            : new Cashier(newId, username, password, fullName);

        UserManager.getInstance().getUsers().add(newUser);
        showSuccess(statusLabel, "User added successfully.");
    }

    public void handleEditUser(User selected, String fullName, String username,
                                String password, Label statusLabel, Runnable onSuccess) {
        if (selected == null) {
            showError(statusLabel, "Select a user to edit.");
            return;
        }
        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showError(statusLabel, "All fields are required.");
            return;
        }
        if (!username.equals(selected.getUsername()) &&
                UserManager.getInstance().usernameExists(username)) {
            showError(statusLabel, "Username already taken.");
            return;
        }

        selected.setFullName(fullName);
        selected.setUsername(username);
        selected.setPassword(password);
        showSuccess(statusLabel, "User updated successfully.");
        onSuccess.run();
    }

    public void handleDeleteUser(User selected, Admin currentAdmin,
                                  Label statusLabel, Runnable onSuccess) {
        if (selected == null) {
            showError(statusLabel, "Select a user to delete.");
            return;
        }
        if (selected.getUserId() == currentAdmin.getUserId()) {
            showError(statusLabel, "You cannot delete yourself.");
            return;
        }

        UserManager.getInstance().getUsers().remove(selected);
        showSuccess(statusLabel, "User deleted successfully.");
        onSuccess.run();
    }

    // Helpers
    private void showError(Label label, String message) {
        label.setStyle("-fx-text-fill: #e05555; -fx-font-size: 12px;");
        label.setText("⚠  " + message);
    }

    private void showSuccess(Label label, String message) {
        label.setStyle("-fx-text-fill: #4caf82; -fx-font-size: 12px;");
        label.setText("✓  " + message);
    }
}