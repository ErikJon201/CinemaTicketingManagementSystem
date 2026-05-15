package views;

import models.Admin;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;

public class AdminDashboard {

    private Stage stage;
    private Admin admin;

    public AdminDashboard(Stage stage, Admin admin) {
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

        // Nav buttons
        Button manageMoviesBtn   = navButton("🎬   Manage Movies");
        Button manageShowtimesBtn = navButton("📅   Manage Showtimes");
        Button manageUsersBtn    = navButton("👥   Manage Users");
        Button salesReportBtn    = navButton("📊   Sales Report");

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

        // ── Main Content ──────────────────────────────────────────────
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox content = new VBox(24);
        content.setPadding(new Insets(50, 50, 50, 50));
        content.setStyle("-fx-background-color: #0b0f1a;");

        // Greeting
        Label greeting = new Label("Welcome back,");
        greeting.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 14px;");

        Label username = new Label(admin.getFullName());
        username.setStyle(
            "-fx-text-fill: #eaeaea;" +
            "-fx-font-size: 32px;" +
            "-fx-font-weight: bold;"
        );

        Label roleInfo = new Label("Administrator  •  Full system access");
        roleInfo.setStyle("-fx-text-fill: #c9a84c; -fx-font-size: 12px;");

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #2b3250;");
        VBox.setMargin(sep, new Insets(4, 0, 12, 0));

        Label sectionLabel = new Label("MANAGEMENT TOOLS");
        sectionLabel.setStyle(
            "-fx-text-fill: #7a849a;" +
            "-fx-font-size: 10px;" +
            "-fx-font-weight: bold;"
        );

        // Action cards
        HBox row1 = new HBox(20);
        HBox row2 = new HBox(20);

        VBox moviesCard    = actionCard("🎬", "Manage Movies",    "Add, edit, or remove\nmovies from the system");
        VBox showtimesCard = actionCard("📅", "Manage Showtimes", "Schedule and update\nmovie showtimes");
        VBox usersCard     = actionCard("👥", "Manage Users",     "Add or remove cashier\nand admin accounts");
        VBox salesCard     = actionCard("📊", "Sales Report",     "View ticket sales and\nrevenue summaries");

        row1.getChildren().addAll(moviesCard, showtimesCard);
        row2.getChildren().addAll(usersCard, salesCard);

        Label footer = new Label("Authorized personnel only  •  " + admin.getRole());
        footer.setStyle("-fx-text-fill: #2b3250; -fx-font-size: 11px;");
        Region contentSpacer = new Region();
        VBox.setVgrow(contentSpacer, Priority.ALWAYS);

        content.getChildren().addAll(
            greeting, username, roleInfo, sep,
            sectionLabel, row1, row2,
            contentSpacer, footer
        );

        scrollPane.setContent(content);

        // ── Wire up nav buttons ───────────────────────────────────────
        manageMoviesBtn.setOnAction(e ->
            stage.setScene(new ManageMoviesScreen(stage, admin).getScene())
        );
        manageShowtimesBtn.setOnAction(e ->
            stage.setScene(new ManageShowtimesScreen(stage, admin).getScene())
        );
        manageUsersBtn.setOnAction(e ->
            stage.setScene(new ManageUsersScreen(stage, admin).getScene())
        );
        salesReportBtn.setOnAction(e ->
            stage.setScene(new SalesReportScreen(stage, admin).getScene())
        );
        logoutBtn.setOnAction(e ->
            stage.setScene(new LoginScreen(stage).getScene())
        );

        // Wire up cards (mirror nav buttons)
        moviesCard.setOnMouseClicked(e ->
            stage.setScene(new ManageMoviesScreen(stage, admin).getScene())
        );
        showtimesCard.setOnMouseClicked(e ->
            stage.setScene(new ManageShowtimesScreen(stage, admin).getScene())
        );
        usersCard.setOnMouseClicked(e ->
            stage.setScene(new ManageUsersScreen(stage, admin).getScene())
        );
        salesCard.setOnMouseClicked(e ->
            stage.setScene(new SalesReportScreen(stage, admin).getScene())
        );

        root.setLeft(sidebar);
        root.setCenter(scrollPane);
        return new Scene(root, 860, 520);
    }

    // ── Helpers ───────────────────────────────────────────────────────

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

    private VBox actionCard(String icon, String title, String desc) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(28, 28, 28, 28));
        card.setPrefWidth(220);
        card.setStyle(
            "-fx-background-color: #161b2e;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 16, 0, 0, 4);"
        );

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 26px;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-text-fill: #eaeaea;" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;"
        );

        Label descLabel = new Label(desc);
        descLabel.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 12px;");
        descLabel.setWrapText(true);

        Rectangle bar = new Rectangle(40, 3);
        bar.setFill(Color.web("#c9a84c"));
        bar.setArcWidth(2); bar.setArcHeight(2);
        VBox.setMargin(bar, new Insets(6, 0, 0, 0));

        card.getChildren().addAll(iconLabel, titleLabel, descLabel, bar);

        card.setOnMouseEntered(e -> card.setStyle(
            "-fx-background-color: #1e2540;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 24, 0, 0, 8);"
        ));
        card.setOnMouseExited(e -> card.setStyle(
            "-fx-background-color: #161b2e;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 16, 0, 0, 4);"
        ));

        return card;
    }

    private String navStyle() {
        return  "-fx-background-color: transparent;" +
                "-fx-text-fill: #7a849a;" +
                "-fx-font-size: 13px;" +
                "-fx-cursor: hand;";
    }

    private String navHoverStyle() {
        return  "-fx-background-color: #1e2540;" +
                "-fx-text-fill: #c9a84c;" +
                "-fx-font-size: 13px;" +
                "-fx-cursor: hand;";
    }

    private String logoutNavStyle() {
        return  "-fx-background-color: transparent;" +
                "-fx-text-fill: #3d4560;" +
                "-fx-font-size: 13px;" +
                "-fx-cursor: hand;";
    }

    private String logoutNavHoverStyle() {
        return  "-fx-background-color: transparent;" +
                "-fx-text-fill: #e05555;" +
                "-fx-font-size: 13px;" +
                "-fx-cursor: hand;";
    }
}