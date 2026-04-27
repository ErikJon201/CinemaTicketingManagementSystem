package views;

import controllers.LoginController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class LoginScreen {

    private Stage stage;

    public LoginScreen(Stage stage) {
        this.stage = stage;
    }

    public Scene getScene() {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));

        Label title = new Label("Cinema Ticketing System");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 24));

        ToggleGroup roleGroup = new ToggleGroup();
        ToggleButton adminBtn = new ToggleButton("Admin");
        ToggleButton cashierBtn = new ToggleButton("Cashier");
        adminBtn.setToggleGroup(roleGroup);
        cashierBtn.setToggleGroup(roleGroup);
        cashierBtn.setSelected(true);

        HBox roleBox = new HBox(8, adminBtn, cashierBtn);
        roleBox.setAlignment(Pos.CENTER);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(280);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(280);

        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 11px;");

        Button loginBtn = new Button("Login");
        loginBtn.setMaxWidth(280);

        LoginController controller = new LoginController(stage);

        loginBtn.setOnAction(e -> {
            String role = adminBtn.isSelected() ? "Admin" : "Cashier";
            controller.handleLogin(
                usernameField.getText().trim(),
                passwordField.getText().trim(),
                role,
                errorLabel
            );
        });

        root.getChildren().addAll(title, roleBox, usernameField, passwordField, errorLabel, loginBtn);

        return new Scene(root, 800, 500);
    }
}