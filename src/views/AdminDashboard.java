package views;

import models.Admin;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class AdminDashboard {

    private Stage stage;
    private Admin admin;

    public AdminDashboard(Stage stage, Admin admin) {
        this.stage = stage;
        this.admin = admin;
    }

    public Scene getScene() {
        VBox root = new VBox(12);
        root.setPadding(new Insets(40));

        Label title = new Label("Welcome, " + admin.getUsername() + "!");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 24));

        Label role = new Label("Role: " + admin.getRole());
        role.setStyle("-fx-text-fill: gray; -fx-font-size: 12px;");

        Separator sep = new Separator();

        Button manageMoviesBtn = new Button("Manage Movies");
        Button manageShowtimesBtn = new Button("Manage Showtimes");
        Button manageUsersBtn = new Button("Manage Users");
        Button salesReportBtn = new Button("View Sales Report");
        Button logoutBtn = new Button("Logout");

        for (Button btn : new Button[]{manageMoviesBtn, manageShowtimesBtn, manageUsersBtn, salesReportBtn, logoutBtn}) {
            btn.setMaxWidth(280);
        }

        manageMoviesBtn.setOnAction(e -> {
            ManageMoviesScreen manageScreen = new ManageMoviesScreen(stage, admin);
            stage.setScene(manageScreen.getScene());
        });
        manageShowtimesBtn.setOnAction(e -> {
            ManageShowtimesScreen showtimeScreen = new ManageShowtimesScreen(stage, admin);
            stage.setScene(showtimeScreen.getScene());
        });
        manageUsersBtn.setOnAction(e -> System.out.println("DO: Manage Users"));
        salesReportBtn.setOnAction(e -> System.out.println("DO: Sales Report"));

        logoutBtn.setOnAction(e -> {
            admin.logout();
            LoginScreen login = new LoginScreen(stage);
            stage.setScene(login.getScene());
        });

        root.getChildren().addAll(
            title, role, sep,
            manageMoviesBtn, manageShowtimesBtn, manageUsersBtn, salesReportBtn, logoutBtn
        );

        return new Scene(root, 800, 500);
    }
}