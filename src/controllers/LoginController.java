package controllers;

import models.Admin;
import models.Cashier;
import models.UserManager;
import views.AdminDashboard;
import views.CashierDashboard;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class LoginController {

    private Stage stage;

    public LoginController(Stage stage) {
        this.stage = stage;
    }

    public void handleLogin(String username, String password, String role, Label errorLabel) {
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        if (role.equals("Cashier")) {
            Cashier cashier = UserManager.getInstance().loginCashier(username, password);
            if (cashier != null) {
                CashierDashboard dashboard = new CashierDashboard(stage, cashier);
                stage.setScene(dashboard.getScene());
            } else {
                errorLabel.setText("Invalid username or password.");
            }
        } else if (role.equals("Admin")) {
            Admin admin = UserManager.getInstance().loginAdmin(username, password);
            if (admin != null) {
                AdminDashboard dashboard = new AdminDashboard(stage, admin);
                stage.setScene(dashboard.getScene());
            } else {
                errorLabel.setText("Invalid username or password.");
            }
        }
    }
}