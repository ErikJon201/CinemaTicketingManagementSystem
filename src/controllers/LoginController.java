package controllers;

import models.Cashier;
import models.UserManager;
import views.CashierDashboard;
import views.LoginScreen;
import javafx.stage.Stage;

public class LoginController {

    private Stage stage;

    public LoginController(Stage stage) {
        this.stage = stage;
    }

    public void handleLogin(String username, String password, String role, javafx.scene.control.Label errorLabel) {
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        if (role.equals("Cashier")) {
            Cashier cashier = UserManager.getInstance().login(username, password);
            if (cashier != null) {
                CashierDashboard dashboard = new CashierDashboard(stage, cashier);
                stage.setScene(dashboard.getScene());
            } else {
                errorLabel.setText("Invalid username or password.");
            }
        } else if (role.equals("Admin")) {
            // DO: admin login logic
            errorLabel.setText("Admin login not yet available.");
        }
    }
}