package views;

import models.Admin;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
        // Main Container
        VBox root = new VBox(20);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #f0f2f5;"); // Light grey professional background

        // Header Section
        VBox header = new VBox(5);
        header.setAlignment(Pos.CENTER);

        Label title = new Label("CINEMA ADMINISTRATION");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: #2c3e50;");

          

        Label welcomeLabel = new Label("Logged in as: " + admin.getFullName() + " (Administrator)");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        welcomeLabel.setStyle("-fx-text-fill: #7f8c8d;");

        header.getChildren().addAll(title, welcomeLabel);

        Separator sep = new Separator();
        sep.setPadding(new Insets(10, 0, 10, 0));

        // Buttons Grid (Organizing buttons in a clean layout)
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        // Define Buttons
        Button manageMoviesBtn = createMenuButton("🎬 Manage Movies", "#3498db");
        Button manageShowtimesBtn = createMenuButton("📅 Manage Showtimes", "#3498db");
        Button manageUsersBtn = createMenuButton("👥 Manage Users", "#2ecc71");
        Button salesReportBtn = createMenuButton("📊 Sales Report", "#f1c40f");
        Button logoutBtn = createMenuButton("🚪 Logout", "#e74c3c");

        // Add Buttons to Grid
        
        grid.add(manageMoviesBtn, 0, 0);
        grid.add(manageShowtimesBtn, 1, 0);
        grid.add(manageUsersBtn, 0, 1);
        grid.add(salesReportBtn, 1, 1);
        grid.add(logoutBtn, 0, 2, 2, 1); // Logout spans across two columns

        // --- BUTTON ACTIONS (Linking the screens) ---

        // 1. Manage Movies
        manageMoviesBtn.setOnAction(e -> {
            ManageMoviesScreen screen = new ManageMoviesScreen(stage, admin);
            stage.setScene(screen.getScene());
        });

        // 2. Manage Showtimes
        manageShowtimesBtn.setOnAction(e -> {
            ManageShowtimesScreen screen = new ManageShowtimesScreen(stage, admin);
            stage.setScene(screen.getScene());
        });

        // 3. Manage Users
        manageUsersBtn.setOnAction(e -> {
            ManageUsersScreen screen = new ManageUsersScreen(stage, admin);
            stage.setScene(screen.getScene());
        });

        // 4. View Sales Report
        salesReportBtn.setOnAction(e -> {
            SalesReportScreen screen = new SalesReportScreen(stage, admin);
            stage.setScene(screen.getScene());
        });

        // 5. Logout
        logoutBtn.setOnAction(e -> {
            LoginScreen login = new LoginScreen(stage);
            stage.setScene(login.getScene());
        });

        // Final Assembly
        root.getChildren().addAll(header, sep, grid);

        return new Scene(root, 800, 600);
    }

    /**
     * Helper method to create styled buttons quickly
     */
    private Button createMenuButton(String text, String colorHex) {
        Button btn = new Button(text);
        btn.setPrefSize(250, 60);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        btn.setCursor(javafx.scene.Cursor.HAND);

        // CSS Styling for the button
        btn.setStyle(
                "-fx-background-color: " + colorHex + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 8;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 5);"
        );

        // Hover effect
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: derive(" + colorHex + ", -10%); -fx-text-fill: white; -fx-background-radius: 8;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + colorHex + "; -fx-text-fill: white; -fx-background-radius: 8;"));

        return btn;
    }
}