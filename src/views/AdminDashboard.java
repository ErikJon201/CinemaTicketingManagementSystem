package views;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.*;

import java.time.LocalTime;

public class AdminDashboard {
    private Stage stage;
    private Admin admin;

    public AdminDashboard(Stage stage, Admin admin) {
        this.stage = stage;
        this.admin = admin;
    }

    public Scene getScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:" + UIHelper.BG + ";");

        // ── Sidebar ────────────────────────────────────────────────────────────
        String[] navLabels = {"Dashboard", "Movies", "Showtimes", "Theater Rooms", "Staff", "Sales Report"};
        Runnable[] navActions = {
            null,
            () -> stage.setScene(new ManageMoviesScreen(stage, admin).getScene()),
            () -> stage.setScene(new ManageShowtimesScreen(stage, admin).getScene()),
            () -> stage.setScene(new ManageRoomsScreen(stage, admin).getScene()),
            () -> stage.setScene(new ManageUsersScreen(stage, admin).getScene()),
            () -> stage.setScene(new SalesReportScreen(stage, admin).getScene())
        };
        VBox sidebar = UIHelper.sidebar(admin, "Dashboard", navLabels, navActions,
                () -> stage.setScene(new LoginScreen(stage).getScene()));
        root.setLeft(sidebar);

        // ── Content ────────────────────────────────────────────────────────────
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background:transparent;-fx-background-color:transparent;");

        VBox content = new VBox(28);
        content.setPadding(new Insets(44, 48, 44, 48));
        content.setStyle("-fx-background-color:" + UIHelper.BG + ";");

        // Greeting
        String timeGreet = greeting();
        VBox header = new VBox(6);
        header.getChildren().addAll(
            UIHelper.lbl(timeGreet + ", " + admin.getFullName() + "!", UIHelper.TEXT, 26, true),
            UIHelper.lbl("Administrator  —  Full system access", UIHelper.TEXT2, 13, false));

        UIHelper.sep();

        // Stats row
        int movieCount    = CinemaManager.getInstance().getMovies().size();
        int showtimeCount = CinemaManager.getInstance().getShowtimes().size();
        double todayRev   = SalesManager.getInstance().getRevenueToday();
        int userCount     = UserManager.getInstance().getUsers().size();

        HBox statsRow = new HBox(16);
        statsRow.getChildren().addAll(
            UIHelper.statCard(String.valueOf(movieCount),    "Movies",          UIHelper.BLUE),
            UIHelper.statCard(String.valueOf(showtimeCount), "Showtimes",       UIHelper.PURPLE),
            UIHelper.statCard("PHP " + String.format("%.0f", todayRev), "Revenue Today", UIHelper.GREEN),
            UIHelper.statCard(String.valueOf(userCount),     "Staff Accounts",  UIHelper.GOLD));
        for (javafx.scene.Node n : statsRow.getChildren())
            HBox.setHgrow(n, Priority.ALWAYS);

        // Quick nav section
        Label secLabel = UIHelper.sectionLbl("Management");
        VBox.setMargin(secLabel, new Insets(8, 0, 4, 0));

        HBox cards1 = new HBox(16);
        HBox cards2 = new HBox(16);

        VBox c1 = actionCard("Movies",        "Add, edit, and remove movies from the catalog.",
                UIHelper.BLUE,   () -> stage.setScene(new ManageMoviesScreen(stage, admin).getScene()));
        VBox c2 = actionCard("Showtimes",     "Schedule and manage movie showtimes and pricing.",
                UIHelper.PURPLE, () -> stage.setScene(new ManageShowtimesScreen(stage, admin).getScene()));
        VBox c3 = actionCard("Theater Rooms", "Configure rooms, seating capacity, and types.",
                UIHelper.GOLD,   () -> stage.setScene(new ManageRoomsScreen(stage, admin).getScene()));
        VBox c4 = actionCard("Staff",         "Manage admin and cashier user accounts.",
                UIHelper.ORANGE, () -> stage.setScene(new ManageUsersScreen(stage, admin).getScene()));
        VBox c5 = actionCard("Sales Report",  "View revenue analytics and transaction history.",
                UIHelper.GREEN,  () -> stage.setScene(new SalesReportScreen(stage, admin).getScene()));

        for (VBox c : new VBox[]{c1, c2, c3, c4, c5})
            HBox.setHgrow(c, Priority.ALWAYS);

        cards1.getChildren().addAll(c1, c2, c3);
        cards2.getChildren().addAll(c4, c5);

        content.getChildren().addAll(header, UIHelper.sep(), statsRow, secLabel, cards1, cards2);
        scroll.setContent(content);
        root.setCenter(scroll);

        return new Scene(root, 1280, 760);
    }

    private VBox actionCard(String title, String desc, String accent, Runnable action) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(22, 22, 22, 22));
        card.setStyle(cardStyle(false));

        Region bar = new Region();
        bar.setPrefHeight(3);
        bar.setStyle("-fx-background-color:" + accent + ";-fx-background-radius:3;");

        card.getChildren().addAll(
            UIHelper.lbl(title, UIHelper.TEXT,  15, true),
            UIHelper.lbl(desc,  UIHelper.TEXT2, 12, false),
            bar);

        card.getChildren().get(1).getClass(); // ensure it's a Label
        ((Label) card.getChildren().get(1)).setWrapText(true);

        card.setOnMouseEntered(e -> card.setStyle(cardStyle(true)));
        card.setOnMouseExited(e  -> card.setStyle(cardStyle(false)));
        card.setOnMouseClicked(e -> action.run());
        card.setStyle(cardStyle(false) + "-fx-cursor:hand;");
        card.setOnMouseEntered(e -> card.setStyle(cardStyle(true) + "-fx-cursor:hand;"));
        card.setOnMouseExited(e  -> card.setStyle(cardStyle(false) + "-fx-cursor:hand;"));
        return card;
    }

    private String cardStyle(boolean hover) {
        return hover
            ? "-fx-background-color:" + UIHelper.CARD2 + ";-fx-background-radius:12;" +
              "-fx-border-color:" + UIHelper.BORDER + ";-fx-border-radius:12;-fx-border-width:1;"
            : "-fx-background-color:" + UIHelper.CARD + ";-fx-background-radius:12;" +
              "-fx-border-color:" + UIHelper.BORDER + ";-fx-border-radius:12;-fx-border-width:1;";
    }

    private String greeting() {
        int hour = LocalTime.now().getHour();
        if (hour < 12) return "Good morning";
        if (hour < 18) return "Good afternoon";
        return "Good evening";
    }
}