package views;

import controllers.SalesController;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import models.*;
import java.util.Map;

public class SalesReportScreen {
    private Stage stage;
    private Admin admin;
    private SalesController controller = new SalesController();

    public SalesReportScreen(Stage stage, Admin admin) {
        this.stage = stage;
        this.admin = admin;
    }

    public Scene getScene() {

        // ── Root ─────────────────────────────────────────────────────
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0b0f1a;");

        // ── Sidebar ──────────────────────────────────────────────────
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(240);
        sidebar.setStyle("-fx-background-color: #161b2e;");

        Rectangle accentBar = new Rectangle(4, 600);
        accentBar.setFill(Color.web("#c9a84c"));

        VBox brandBox = new VBox(4);
        brandBox.setPadding(new Insets(30, 20, 30, 20));
        brandBox.setStyle("-fx-border-color: transparent transparent #2b3250 transparent; -fx-border-width: 1;");

        Label brand = new Label("🎬  CINETICKET");
        brand.setStyle("-fx-text-fill: #c9a84c; -fx-font-size: 13px; -fx-font-weight: bold;");
        Label roleTag = new Label("ADMIN PORTAL");
        roleTag.setStyle("-fx-text-fill: #3d4560; -fx-font-size: 10px; -fx-font-weight: bold;");
        brandBox.getChildren().addAll(brand, roleTag);

        Button manageMoviesBtn    = navButton("🎥   Manage Movies");
        Button manageShowtimesBtn = navButton("🕒   Manage Showtimes");
        Button manageUsersBtn     = navButton("👥   Manage Users");
        Button salesReportBtn     = navButton("📊   Sales Report");
        salesReportBtn.setStyle(navActiveStyle()); // highlight current screen

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button logoutBtn = navButton("⎋   Logout");
        logoutBtn.setStyle(logoutNavStyle());
        logoutBtn.setOnMouseEntered(e -> logoutBtn.setStyle(logoutNavHoverStyle()));
        logoutBtn.setOnMouseExited(e  -> logoutBtn.setStyle(logoutNavStyle()));
        VBox.setMargin(logoutBtn, new Insets(0, 0, 20, 0));

        sidebar.getChildren().addAll(
            new HBox(accentBar, brandBox) {{ setAlignment(Pos.CENTER_LEFT); }},
            manageMoviesBtn, manageShowtimesBtn, manageUsersBtn, salesReportBtn,
            spacer, logoutBtn
        );

        // ── Main Content ─────────────────────────────────────────────
        VBox content = new VBox(24);
        content.setPadding(new Insets(50, 50, 50, 50));
        content.setStyle("-fx-background-color: #0b0f1a;");

        // Header
        Label greeting = new Label("Sales Report");
        greeting.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 14px;");
        Label subtitle = new Label("Revenue by Movie");
        subtitle.setStyle("-fx-text-fill: #eaeaea; -fx-font-size: 32px; -fx-font-weight: bold;");

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #2b3250;");
        VBox.setMargin(sep, new Insets(4, 0, 12, 0));

        // ── Summary Cards ─────────────────────────────────────────────
        Label summaryLabel = new Label("SUMMARY");
        summaryLabel.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 10px; -fx-font-weight: bold;");

        HBox summaryCards = new HBox(16);
        summaryCards.getChildren().addAll(
            summaryCard("💰", "Grand Total",
                String.format("PHP %.2f", controller.getGrandTotal())),
            summaryCard("🎟", "Tickets Sold",
                String.valueOf(controller.getTotalTicketsSold())),
            summaryCard("📈", "Avg. Ticket Price",
                String.format("PHP %.2f", controller.getAverageTicketPrice())),
            summaryCard("🏆", "Top Movie",
                controller.getTopMovie())
        );

        // ── Table ─────────────────────────────────────────────────────
        Label tableLabel = new Label("REVENUE BY MOVIE");
        tableLabel.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 10px; -fx-font-weight: bold;");

        TableView<Map.Entry<String, Double>> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(220);

        TableColumn<Map.Entry<String, Double>, String> movieCol = new TableColumn<>("Movie Title");
        movieCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getKey()));
        movieCol.setCellFactory(col -> styledCell());

        TableColumn<Map.Entry<String, Double>, String> revenueCol = new TableColumn<>("Revenue");
        revenueCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                String.format("PHP %.2f", data.getValue().getValue())));
        revenueCol.setCellFactory(col -> styledCell());
        revenueCol.setMaxWidth(160);
        revenueCol.setMinWidth(160);

        table.getColumns().addAll(movieCol, revenueCol);

        // Populate table
        javafx.collections.ObservableList<Map.Entry<String, Double>> tableData =
            javafx.collections.FXCollections.observableArrayList(
                controller.getSalesByMovieSorted().entrySet()
            );
        table.setItems(tableData);

        // ── Footer ────────────────────────────────────────────────────
        Region contentSpacer = new Region();
        VBox.setVgrow(contentSpacer, Priority.ALWAYS);
        Label footer = new Label("Authorized personnel only  •  " + admin.getRole());
        footer.setStyle("-fx-text-fill: #2b3250; -fx-font-size: 11px;");

        content.getChildren().addAll(
            greeting, subtitle, sep,
            summaryLabel, summaryCards,
            tableLabel, table,
            contentSpacer, footer
        );

        // ── Wire Sidebar ──────────────────────────────────────────────
        manageMoviesBtn.setOnAction(e ->
            stage.setScene(new ManageMoviesScreen(stage, admin).getScene()));
        manageShowtimesBtn.setOnAction(e ->
            stage.setScene(new ManageShowtimesScreen(stage, admin).getScene()));
        manageUsersBtn.setOnAction(e ->
            stage.setScene(new ManageUsersScreen(stage, admin).getScene()));
        salesReportBtn.setOnAction(e ->
            stage.setScene(new SalesReportScreen(stage, admin).getScene()));
        logoutBtn.setOnAction(e ->
            stage.setScene(new LoginScreen(stage).getScene()));

        root.setLeft(sidebar);
        root.setCenter(content);

        Scene scene = new Scene(root, 1000, 620);
        scene.getStylesheets().add("data:text/css," +
            ".table-view { -fx-background-color: %23161b2e; }" +
            ".table-view .column-header { -fx-background-color: %231e2540; }" +
            ".table-view .column-header-background { -fx-background-color: %231e2540; }" +
            ".table-view .table-row-cell { -fx-background-color: %23161b2e; -fx-border-color: transparent transparent %232b3250 transparent; }" +
            ".table-view .table-row-cell:selected { -fx-background-color: %231e2540; }" +
            ".table-view .table-row-cell:hover { -fx-background-color: %231a2035; }" +
            ".table-view .table-cell { -fx-text-fill: %23eaeaea; }" +
            ".column-header .label { -fx-text-fill: %237a849a; -fx-font-weight: bold; }"
        );
        return scene;
    }

    // ── Helpers ───────────────────────────────────────────────────────

    private VBox summaryCard(String icon, String title, String value) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(20, 24, 20, 24));
        card.setPrefWidth(200);
        card.setStyle(
            "-fx-background-color: #161b2e;" +
            "-fx-background-radius: 6;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 16, 0, 0, 4);"
        );

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 22px;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 11px; -fx-font-weight: bold;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-text-fill: #eaeaea; -fx-font-size: 16px; -fx-font-weight: bold;");
        valueLabel.setWrapText(true);

        Rectangle bar = new Rectangle(40, 3);
        bar.setFill(Color.web("#c9a84c"));
        bar.setArcWidth(2);
        bar.setArcHeight(2);
        VBox.setMargin(bar, new Insets(4, 0, 0, 0));

        card.getChildren().addAll(iconLabel, titleLabel, valueLabel, bar);
        return card;
    }

    private <T> TableCell<Map.Entry<String, Double>, T> styledCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
                setStyle("-fx-text-fill: #eaeaea; -fx-alignment: CENTER-LEFT;");
            }
        };
    }

    private Button navButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(14, 24, 14, 24));
        btn.setStyle(navStyle());
        btn.setOnMouseEntered(e -> btn.setStyle(navHoverStyle()));
        btn.setOnMouseExited(e  -> btn.setStyle(navStyle()));
        return btn;
    }

    private String navStyle() {
        return "-fx-background-color: transparent;" +
               "-fx-text-fill: #7a849a;" +
               "-fx-font-size: 13px;" +
               "-fx-cursor: hand;";
    }

    private String navHoverStyle() {
        return "-fx-background-color: #1e2540;" +
               "-fx-text-fill: #c9a84c;" +
               "-fx-font-size: 13px;" +
               "-fx-cursor: hand;";
    }

    private String navActiveStyle() {
        return "-fx-background-color: #1e2540;" +
               "-fx-text-fill: #c9a84c;" +
               "-fx-font-size: 13px;" +
               "-fx-cursor: hand;" +
               "-fx-border-color: transparent transparent transparent #c9a84c;" +
               "-fx-border-width: 3;";
    }

    private String logoutNavStyle() {
        return "-fx-background-color: transparent;" +
               "-fx-text-fill: #3d4560;" +
               "-fx-font-size: 13px;" +
               "-fx-cursor: hand;";
    }

    private String logoutNavHoverStyle() {
        return "-fx-background-color: transparent;" +
               "-fx-text-fill: #e05555;" +
               "-fx-font-size: 13px;" +
               "-fx-cursor: hand;";
    }
}