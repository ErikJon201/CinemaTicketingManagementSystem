package controllers;

import models.*;
import views.*;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class LoginController {
    private Stage stage;

    public LoginController(Stage stage) {
        this.stage = stage;
    }

    public void handleLogin(String username, String password, String role, Label errorLabel) {
        User user = UserManager.getInstance().login(username, password, role);

        if (user == null) {
            errorLabel.setText("Invalid credentials for " + role);
            return;
        }

        if (user instanceof Admin) {
            stage.setScene(new AdminDashboard(stage, (Admin) user).getScene());
        } else if (user instanceof Cashier) {
            stage.setScene(new CashierDashboard(stage, (Cashier) user).getScene());
        }
    }
}