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

        Button searchMoviesBtn = new Button("Search Movies & Showtimes");
        Button selectSeatsBtn = new Button("Select Seats");
        Button sellTicketBtn = new Button("Sell Ticket");
        Button processPaymentBtn = new Button("Process Payment");
        Button generateReceiptBtn = new Button("Generate Receipt");
        Button logoutBtn = new Button("Logout");

        for (Button btn : new Button[]{searchMoviesBtn, selectSeatsBtn, sellTicketBtn, processPaymentBtn, generateReceiptBtn, logoutBtn}) {
            btn.setMaxWidth(280);
        }

        searchMoviesBtn.setOnAction(e -> System.out.println("DO: Search Movies"));
        selectSeatsBtn.setOnAction(e -> System.out.println("DO: Select Seats"));
        sellTicketBtn.setOnAction(e -> System.out.println("DO: Sell Ticket"));
        processPaymentBtn.setOnAction(e -> System.out.println("DO: Process Payment"));
        generateReceiptBtn.setOnAction(e -> System.out.println("DO: Generate Receipt"));

        logoutBtn.setOnAction(e -> {
            cashier.logout();
            LoginScreen login = new LoginScreen(stage);
            stage.setScene(login.getScene());
        });

        root.getChildren().addAll(title, role, sep, searchMoviesBtn, selectSeatsBtn, sellTicketBtn, processPaymentBtn, generateReceiptBtn, logoutBtn);

        return new Scene(root, 800, 500);
    }
}