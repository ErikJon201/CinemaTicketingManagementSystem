package views;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class CashierDashboard {
    private Stage stage;
    private Cashier cashier;

    public CashierDashboard(Stage stage, Cashier cashier) {
        this.stage = stage;
        this.cashier = cashier;
    }

    public Scene getScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:" + UIHelper.BG + ";");

        // ── Sidebar ────────────────────────────────────────────────────────────
        String[] navLabels = {"Dashboard", "Now Showing", "Sell Tickets"};
        Runnable[] navActs = {
            null,
            () -> stage.setScene(new SearchMovieScreen(stage, cashier).getScene()),
            () -> stage.setScene(new MovieSelectionScreen(stage, cashier).getScene())
        };
        VBox sidebar = UIHelper.sidebar(cashier, "Dashboard", navLabels, navActs,
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
        VBox header = new VBox(6);
        header.getChildren().addAll(
            UIHelper.lbl(greeting() + ", " + cashier.getFullName() + "!",
                         UIHelper.TEXT, 26, true),
            UIHelper.lbl("Cashier  —  Ticket sales portal", UIHelper.TEXT2, 13, false));

        // Stats
        int ticketsToday    = SalesManager.getInstance().getTicketsSoldToday();
        double revenueToday = SalesManager.getInstance().getRevenueToday();

        HBox statsRow = new HBox(16);
        HBox statA = UIHelper.statCard(String.valueOf(ticketsToday),
                "Tickets Sold Today", UIHelper.GREEN);
        HBox statB = UIHelper.statCard(String.format("PHP %.2f", revenueToday),
                "Revenue Today", UIHelper.GOLD);
        HBox.setHgrow(statA, Priority.ALWAYS);
        HBox.setHgrow(statB, Priority.ALWAYS);
        statsRow.getChildren().addAll(statA, statB);

        // Quick actions
        Label actLabel = UIHelper.sectionLbl("Quick Actions");
        VBox.setMargin(actLabel, new Insets(4, 0, 4, 0));
        HBox actions = new HBox(14);
        Button sellBtn  = UIHelper.primaryBtn("Sell Tickets");
        Button browseBtn = UIHelper.btn("Browse Movies", UIHelper.CARD2, UIHelper.TEXT);
        sellBtn.setPrefHeight(42);
        browseBtn.setPrefHeight(42);
        sellBtn.setOnAction(e  -> stage.setScene(new MovieSelectionScreen(stage, cashier).getScene()));
        browseBtn.setOnAction(e -> stage.setScene(new SearchMovieScreen(stage, cashier).getScene()));
        actions.getChildren().addAll(sellBtn, browseBtn);

        // Today's showtimes
        Label showLabel = UIHelper.sectionLbl("Today's Showtimes");
        VBox.setMargin(showLabel, new Insets(6, 0, 4, 0));

        String todayStr = LocalDate.now().format(DateTimeFormatter.ofPattern("MMM d, yyyy"));
        List<Showtime> todayShows = CinemaManager.getInstance().getShowtimes().stream()
                .filter(s -> s.getDate().equals(todayStr))
                .collect(Collectors.toList());

        VBox showtimeCard = UIHelper.card();
        VBox showtimeList = new VBox(6);

        if (todayShows.isEmpty()) {
            showtimeList.getChildren().add(
                UIHelper.lbl("No showtimes scheduled for today.", UIHelper.TEXT2, 13, false));
        } else {
            // Header row
            HBox headerRow = buildRow("MOVIE", "TIME", "ROOM", "AVAILABLE", "PRICE/SEAT", true, null);
            showtimeList.getChildren().addAll(headerRow, UIHelper.sep());
            for (Showtime st : todayShows)
                showtimeList.getChildren().add(
                    buildRow(st.getMovieTitle(), st.getTime(), st.getRoomName(),
                             st.getAvailableSeats() + " seats",
                             String.format("PHP %.0f", st.getPrice()),
                             false, st));
        }

        showtimeCard.getChildren().add(showtimeList);

        content.getChildren().addAll(header, UIHelper.sep(), statsRow,
                actLabel, actions, showLabel, showtimeCard);
        scroll.setContent(content);
        root.setCenter(scroll);

        return new Scene(root, 1280, 760);
    }

    private HBox buildRow(String movie, String time, String room,
                           String avail, String price,
                           boolean isHeader, Showtime showtime) {
        HBox row = new HBox(0);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 8, 10, 8));

        if (!isHeader) {
            row.setStyle("-fx-background-color:transparent;-fx-background-radius:8;");
            row.setOnMouseEntered(e ->
                row.setStyle("-fx-background-color:" + UIHelper.CARD2 + ";-fx-background-radius:8;"));
            row.setOnMouseExited(e ->
                row.setStyle("-fx-background-color:transparent;-fx-background-radius:8;"));
        }

        String c = isHeader ? UIHelper.MUTED : UIHelper.TEXT;
        int  fs = isHeader ? 11 : 13;

        Label mLbl = UIHelper.lbl(movie, c, fs, isHeader);  mLbl.setPrefWidth(260);
        Label tLbl = UIHelper.lbl(time,  c, fs, false);     tLbl.setPrefWidth(100);
        Label rLbl = UIHelper.lbl(room,  c, fs, false);     rLbl.setPrefWidth(140);
        Label aLbl = UIHelper.lbl(avail, c, fs, false);     aLbl.setPrefWidth(110);
        Label pLbl = UIHelper.lbl(price, c, fs, false);     pLbl.setPrefWidth(100);

        if (!isHeader && showtime != null) {
            int seats = showtime.getAvailableSeats();
            String aColor = seats == 0 ? "#e74c3c" : (seats < 10 ? UIHelper.GOLD : UIHelper.GREEN);
            aLbl.setStyle("-fx-text-fill:" + aColor + ";-fx-font-size:13;");
        }

        row.getChildren().addAll(mLbl, tLbl, rLbl, aLbl, pLbl);


        if (!isHeader && showtime != null) {
            Region sp = new Region();
            HBox.setHgrow(sp, Priority.ALWAYS);
            Button btn = UIHelper.primaryBtn("Sell");
            btn.setPrefHeight(30);
            if (showtime.getAvailableSeats() == 0) {
                btn.setText("Sold Out");
                btn.setDisable(true);
            } else {
                final Showtime st = showtime;
                btn.setOnAction(e ->
                    stage.setScene(new SeatSelectionScreen(stage, cashier, st).getScene()));
            }
            row.getChildren().addAll(sp, btn);
        }
        return row;
    }

    private String greeting() {
        int h = LocalTime.now().getHour();
        return h < 12 ? "Good morning" : h < 18 ? "Good afternoon" : "Good evening";
    }
}