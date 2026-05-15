package views;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
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

        // ── Root ─────────────────────────────────────────────────────
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0b0f1a;");

        // ── Sidebar ───────────────────────────────────────────────────
        BorderPane sidebar = new BorderPane();
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #161b2e;");

        Rectangle accentBar = new Rectangle(4, 700);
        accentBar.setFill(Color.web("#c9a84c"));

        VBox brandBox = new VBox(4);
        brandBox.setPadding(new Insets(30, 20, 30, 20));
        brandBox.setStyle("-fx-border-color: transparent transparent #2b3250 transparent; -fx-border-width: 1;");
        Label brand = new Label("🎬  CINETICKET");
        brand.setStyle("-fx-text-fill: #c9a84c; -fx-font-size: 13px; -fx-font-weight: bold;");
        Label portalTag = new Label("SALES REPORT");
        portalTag.setStyle("-fx-text-fill: #3d4560; -fx-font-size: 10px; -fx-font-weight: bold;");
        brandBox.getChildren().addAll(brand, portalTag);

        VBox sideTop = new VBox(0);
        sideTop.getChildren().add(
            new HBox(accentBar, brandBox) {{ setAlignment(Pos.CENTER_LEFT); }}
        );

        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setMaxWidth(Double.MAX_VALUE);
        backBtn.setPadding(new Insets(13, 0, 13, 0));
        backBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #3d4560; -fx-font-size: 12px; -fx-cursor: hand;"
        );
        backBtn.setOnMouseEntered(e -> backBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #7a849a; -fx-font-size: 12px; -fx-cursor: hand;"
        ));
        backBtn.setOnMouseExited(e -> backBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #3d4560; -fx-font-size: 12px; -fx-cursor: hand;"
        ));

        VBox sideBottom = new VBox(6);
        sideBottom.setPadding(new Insets(0, 16, 24, 16));
        sideBottom.getChildren().add(backBtn);

        sidebar.setTop(sideTop);
        sidebar.setBottom(sideBottom);

        // ── Main Content ──────────────────────────────────────────────
        VBox content = new VBox(20);
        content.setPadding(new Insets(40, 50, 40, 50));

        Label heading = new Label("Sales Report");
        heading.setStyle(
            "-fx-text-fill: #eaeaea;" +
            "-fx-font-size: 26px;" +
            "-fx-font-weight: bold;"
        );
        Label sub = new Label("Total revenue breakdown by movie");
        sub.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 13px;");

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #2b3250;");

        // ── Sales Data ────────────────────────────────────────────────
        Map<String, Double> salesData = SalesManager.getInstance().getSalesByMovie();

        double grandTotal = salesData.values().stream()
            .mapToDouble(Double::doubleValue).sum();

        // Grand total summary card
        VBox totalCard = new VBox(6);
        totalCard.setPadding(new Insets(20, 24, 20, 24));
        totalCard.setStyle(
            "-fx-background-color: #161b2e;" +
            "-fx-background-radius: 6;" +
            "-fx-border-color: #c9a84c;" +
            "-fx-border-radius: 6;" +
            "-fx-border-width: 1;"
        );
        Label totalTitle = new Label("GRAND TOTAL REVENUE");
        totalTitle.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 10px; -fx-font-weight: bold;");
        Label totalAmount = new Label(String.format("PHP %.2f", grandTotal));
        totalAmount.setStyle(
            "-fx-text-fill: #c9a84c;" +
            "-fx-font-size: 28px;" +
            "-fx-font-weight: bold;"
        );
        totalCard.getChildren().addAll(totalTitle, totalAmount);

        // Per-movie breakdown label
        Label breakdownLabel = new Label("REVENUE BY MOVIE");
        breakdownLabel.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 10px; -fx-font-weight: bold;");

        // Per-movie rows
        VBox movieRows = new VBox(8);

        if (salesData.isEmpty()) {
            Label empty = new Label("No sales recorded yet.");
            empty.setStyle("-fx-text-fill: #3d4560; -fx-font-size: 13px;");
            movieRows.getChildren().add(empty);
        } else {
            for (Map.Entry<String, Double> entry : salesData.entrySet()) {
                HBox row = new HBox();
                row.setPadding(new Insets(14, 20, 14, 20));
                row.setAlignment(Pos.CENTER_LEFT);
                row.setStyle(
                    "-fx-background-color: #0f1422;" +
                    "-fx-background-radius: 4;" +
                    "-fx-border-color: #2b3250;" +
                    "-fx-border-radius: 4;" +
                    "-fx-border-width: 1;"
                );

                Label movieTitle = new Label(entry.getKey());
                movieTitle.setStyle(
                    "-fx-text-fill: #eaeaea;" +
                    "-fx-font-size: 13px;"
                );
                HBox.setHgrow(movieTitle, Priority.ALWAYS);

                Label revenue = new Label(String.format("PHP %.2f", entry.getValue()));
                revenue.setStyle(
                    "-fx-text-fill: #c9a84c;" +
                    "-fx-font-size: 13px;" +
                    "-fx-font-weight: bold;"
                );

                row.getChildren().addAll(movieTitle, revenue);

                // Hover effect
                row.setOnMouseEntered(e -> row.setStyle(
                    "-fx-background-color: #1e2540;" +
                    "-fx-background-radius: 4;" +
                    "-fx-border-color: #2b3250;" +
                    "-fx-border-radius: 4;" +
                    "-fx-border-width: 1;"
                ));
                row.setOnMouseExited(e -> row.setStyle(
                    "-fx-background-color: #0f1422;" +
                    "-fx-background-radius: 4;" +
                    "-fx-border-color: #2b3250;" +
                    "-fx-border-radius: 4;" +
                    "-fx-border-width: 1;"
                ));

                movieRows.getChildren().add(row);
            }
        }

        // Scrollable rows
        ScrollPane scrollPane = new ScrollPane(movieRows);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        backBtn.setOnAction(e -> stage.setScene(new AdminDashboard(stage, admin).getScene()));

        content.getChildren().addAll(
            heading, sub, sep,
            totalCard, breakdownLabel, scrollPane
        );

        root.setLeft(sidebar);
        root.setCenter(content);
        return new Scene(root, 900, 580);
    }
}