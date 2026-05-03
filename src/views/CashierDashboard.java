package views;

import models.Cashier;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class CashierDashboard {

    private Stage stage;
    private Cashier cashier;

    public CashierDashboard(Stage stage, Cashier cashier) {
        this.stage = stage;
        this.cashier = cashier;
    }

    public Scene getScene() {
        VBox root = new VBox(12);
        root.setPadding(new Insets(40));

        Label title = new Label("Welcome, " + cashier.getUsername() + "!");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 24));

        Label role = new Label("Role: " + cashier.getRole());
        role.setStyle("-fx-text-fill: gray; -fx-font-size: 12px;");

        Separator sep = new Separator();

        Button searchMoviesBtn = new Button("Search Movies");
        Button sellTicketBtn = new Button("Buy Ticket");
        Button logoutBtn = new Button("Logout");

        for (Button btn : new Button[]{searchMoviesBtn, sellTicketBtn, logoutBtn}) {
            btn.setMaxWidth(280);
        }

        searchMoviesBtn.setOnAction(e -> {
            SearchMovieScreen searchScreen = new SearchMovieScreen(stage, cashier);
            stage.setScene(searchScreen.getScene());
        });
        sellTicketBtn.setOnAction(e -> {
            MovieSelectionScreen movieSelection = new MovieSelectionScreen(stage, cashier);
            stage.setScene(movieSelection.getScene());
        });

        logoutBtn.setOnAction(e -> {
            LoginScreen login = new LoginScreen(stage);
            stage.setScene(login.getScene());
        });

        root.getChildren().addAll(title, role, sep, searchMoviesBtn, sellTicketBtn, logoutBtn);

        return new Scene(root, 800, 500);
    }
}