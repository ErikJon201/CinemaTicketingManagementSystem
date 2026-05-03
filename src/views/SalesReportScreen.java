package views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.*;
import java.util.Map;

public class SalesReportScreen {
    private Stage stage;
    private Admin admin;

    public SalesReportScreen(Stage stage, Admin admin) {
        this.stage = stage;
        this.admin = admin;
    }

    public Scene getScene() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));

        Label title = new Label("Sales Report (Total Revenue by Movie)");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextArea reportArea = new TextArea();
        reportArea.setEditable(false);

        Map<String, Double> salesData = SalesManager.getInstance().getSalesByMovie();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-30s | %-10s\n", "Movie Title", "Revenue"));
        sb.append("----------------------------------------------------------\n");

        double grandTotal = 0;
        for (Map.Entry<String, Double> entry : salesData.entrySet()) {
            sb.append(String.format("%-30s | PHP %.2f\n", entry.getKey(), entry.getValue()));
            grandTotal += entry.getValue();
        }
        sb.append("----------------------------------------------------------\n");
        sb.append(String.format("GRAND TOTAL: PHP %.2f", grandTotal));

        reportArea.setText(sb.toString());

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> stage.setScene(new AdminDashboard(stage, admin).getScene()));

        root.getChildren().addAll(title, reportArea, backBtn);
        return new Scene(root, 600, 400);
    }
}