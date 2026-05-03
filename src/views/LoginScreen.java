package views;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.*;

public class LoginScreen {
    private Stage stage;

    public LoginScreen(Stage stage) { this.stage = stage; }

    public Scene getScene() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));

        Label title = new Label("CINEMA TICKETING SYSTEM");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        ToggleGroup group = new ToggleGroup();
        RadioButton rbAdmin = new RadioButton("Admin");
        RadioButton rbCashier = new RadioButton("Cashier");
        rbAdmin.setToggleGroup(group); rbCashier.setToggleGroup(group);
        rbCashier.setSelected(true);

        TextField userF = new TextField(); userF.setPromptText("Username");
        PasswordField passF = new PasswordField(); passF.setPromptText("Password");
        Button loginBtn = new Button("Login");

        loginBtn.setOnAction(e -> {
            String role = rbAdmin.isSelected() ? "Admin" : "Cashier";
            User u = UserManager.getInstance().login(userF.getText(), passF.getText(), role);
            if (u != null) {
                if (u instanceof Admin) stage.setScene(new AdminDashboard(stage, (Admin)u).getScene());
                else stage.setScene(new CashierDashboard(stage, (Cashier)u).getScene());
            } else {
                new Alert(Alert.AlertType.ERROR, "Invalid Credentials").show();
            }
        });

        root.getChildren().addAll(title, new HBox(10, rbAdmin, rbCashier), userF, passF, loginBtn);
        return new Scene(root, 400, 400);
    }
}