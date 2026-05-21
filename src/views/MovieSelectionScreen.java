package views;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.*;

import java.util.List;
import java.util.stream.Collectors;

public class MovieSelectionScreen {
    private Stage stage;
    private Cashier cashier;

    public MovieSelectionScreen(Stage stage, Cashier cashier) {
        this.stage = stage;
        this.cashier = cashier;
    }

    public Scene getScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:" + UIHelper.BG + ";");

        String[] navLabels = {"Dashboard", "Now Showing", "Sell Tickets"};
        Runnable[] navActs = {
            () -> stage.setScene(new CashierDashboard(stage, cashier).getScene()),
            () -> stage.setScene(new SearchMovieScreen(stage, cashier).getScene()),
            null
        };
        root.setLeft(UIHelper.sidebar(cashier, "Sell Tickets", navLabels, navActs,
                () -> stage.setScene(new LoginScreen(stage).getScene())));

        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background:transparent;-fx-background-color:transparent;");

        VBox content = new VBox(24);
        content.setPadding(new Insets(44, 48, 44, 48));
        content.setStyle("-fx-background-color:" + UIHelper.BG + ";");

        content.getChildren().add(UIHelper.pageHeader("Select Showtime",
                "Choose a showtime to begin selling tickets."));

        // Genre filter
        Label filterLabel = UIHelper.sectionLbl("Filter by Genre");
        VBox.setMargin(filterLabel, new Insets(4, 0, 4, 0));

        HBox filterRow = new HBox(8);
        filterRow.setAlignment(Pos.CENTER_LEFT);
        String[] genres = {"All", "Action", "Sci-Fi", "Thriller", "Horror",
                           "Animation", "Romance", "Comedy"};
        String[] selectedGenre = {"All"};

        VBox showtimeGrid = new VBox(10);

        Runnable refreshGrid = () -> {
            showtimeGrid.getChildren().clear();
            List<Showtime> shows = CinemaManager.getInstance().getShowtimes().stream()
                .filter(s -> selectedGenre[0].equals("All") ||
                             s.getMovieGenre().equalsIgnoreCase(selectedGenre[0]))
                .collect(Collectors.toList());

            if (shows.isEmpty()) {
                showtimeGrid.getChildren().add(
                    UIHelper.lbl("No showtimes match the selected genre.", UIHelper.TEXT2, 13, false));
                return;
            }

            // Header
            HBox hdr = buildHeader();
            showtimeGrid.getChildren().addAll(hdr, UIHelper.sep());

            for (Showtime st : shows) {
                showtimeGrid.getChildren().add(buildShowtimeRow(st));
            }
        };

        for (String genre : genres) {
            Button chip = buildChip(genre, genre.equals("All"));
            chip.setOnAction(e -> {
                selectedGenre[0] = genre;
                filterRow.getChildren().forEach(n -> {
                    if (n instanceof Button b) {
                        boolean active = b.getText().equals(genre);
                        b.setStyle(chipStyle(active));
                    }
                });
                refreshGrid.run();
            });
            filterRow.getChildren().add(chip);
        }

        refreshGrid.run();

        VBox gridCard = UIHelper.card();
        gridCard.getChildren().add(showtimeGrid);

        content.getChildren().addAll(filterLabel, filterRow, gridCard);
        scroll.setContent(content);
        root.setCenter(scroll);
        return new Scene(root, 1280, 760);
    }

    private HBox buildHeader() {
        HBox row = new HBox(0);
        row.setPadding(new Insets(6, 8, 6, 8));
        Label m = UIHelper.lbl("MOVIE", UIHelper.MUTED, 11, true); m.setPrefWidth(260);
        Label g = UIHelper.lbl("GENRE", UIHelper.MUTED, 11, true); g.setPrefWidth(100);
        Label d = UIHelper.lbl("DATE", UIHelper.MUTED, 11, true);  d.setPrefWidth(130);
        Label t = UIHelper.lbl("TIME", UIHelper.MUTED, 11, true);  t.setPrefWidth(100);
        Label r = UIHelper.lbl("ROOM", UIHelper.MUTED, 11, true);  r.setPrefWidth(130);
        Label a = UIHelper.lbl("AVAILABLE", UIHelper.MUTED, 11, true); a.setPrefWidth(100);
        Label p = UIHelper.lbl("PRICE", UIHelper.MUTED, 11, true); p.setPrefWidth(90);
        row.getChildren().addAll(m, g, d, t, r, a, p);
        return row;
    }

    private HBox buildShowtimeRow(Showtime st) {
        HBox row = new HBox(0);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(11, 8, 11, 8));
        row.setStyle("-fx-background-color:transparent;-fx-background-radius:8;");
        row.setOnMouseEntered(e ->
            row.setStyle("-fx-background-color:" + UIHelper.CARD2 + ";-fx-background-radius:8;"));
        row.setOnMouseExited(e ->
            row.setStyle("-fx-background-color:transparent;-fx-background-radius:8;"));

        Label m = UIHelper.lbl(st.getMovieTitle(), UIHelper.TEXT, 13, true);  m.setPrefWidth(260);
        Label g = UIHelper.lbl(st.getMovieGenre(), UIHelper.TEXT2, 12, false); g.setPrefWidth(100);
        Label d = UIHelper.lbl(st.getDate(),       UIHelper.TEXT2, 12, false); d.setPrefWidth(130);
        Label t = UIHelper.lbl(st.getTime(),       UIHelper.TEXT,  13, false); t.setPrefWidth(100);
        Label r = UIHelper.lbl(st.getRoomName(),   UIHelper.TEXT2, 12, false); r.setPrefWidth(130);

        int avail = st.getAvailableSeats();
        String aColor = avail == 0 ? "#e74c3c" : avail < 10 ? UIHelper.GOLD : UIHelper.GREEN;
        Label a = UIHelper.lbl(avail + " seats", aColor, 12, false); a.setPrefWidth(100);

        Label p = UIHelper.lbl(String.format("PHP %.0f", st.getPrice()), UIHelper.TEXT, 13, false);
        p.setPrefWidth(90);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button buyBtn = UIHelper.primaryBtn("Select");
        buyBtn.setPrefHeight(32);

        if (avail == 0) {
            buyBtn.setText("Sold Out");
            buyBtn.setDisable(true);
        } else {
            buyBtn.setOnAction(e ->
                stage.setScene(new SeatSelectionScreen(stage, cashier, st).getScene()));
        }

        row.getChildren().addAll(m, g, d, t, r, a, p, spacer, buyBtn);
        return row;
    }

    private Button buildChip(String label, boolean active) {
        Button b = new Button(label);
        b.setStyle(chipStyle(active));
        return b;
    }

    private String chipStyle(boolean active) {
        return active
            ? "-fx-background-color:" + UIHelper.RED + ";-fx-text-fill:#fff;" +
              "-fx-font-size:12;-fx-font-weight:bold;-fx-background-radius:20;" +
              "-fx-cursor:hand;-fx-padding:6 14;"
            : "-fx-background-color:" + UIHelper.CARD2 + ";-fx-text-fill:" + UIHelper.TEXT2 +
              ";-fx-font-size:12;-fx-background-radius:20;-fx-cursor:hand;-fx-padding:6 14;" +
              "-fx-border-color:" + UIHelper.BORDER + ";-fx-border-radius:20;-fx-border-width:1;";
    }
}