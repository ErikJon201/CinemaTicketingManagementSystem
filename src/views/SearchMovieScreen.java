package views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.*;

public class SearchMovieScreen {
    private Stage stage;
    private Cashier cashier;

    public SearchMovieScreen(Stage stage, Cashier cashier) {
        this.stage = stage;
        this.cashier = cashier;
    }

    public Scene getScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:" + UIHelper.BG + ";");

        String[] navLabels = {"Dashboard", "Now Showing", "Sell Tickets"};
        Runnable[] navActs = {
            () -> stage.setScene(new CashierDashboard(stage, cashier).getScene()),
            null,
            () -> stage.setScene(new MovieSelectionScreen(stage, cashier).getScene())
        };
        root.setLeft(UIHelper.sidebar(cashier, "Now Showing", navLabels, navActs,
                () -> stage.setScene(new LoginScreen(stage).getScene())));

        HBox main = new HBox(20);
        main.setPadding(new Insets(36, 36, 36, 36));
        main.setStyle("-fx-background-color:" + UIHelper.BG + ";");
        HBox.setHgrow(main, Priority.ALWAYS);

        // ── Left: movie list ───────────────────────────────────────────────────
        VBox leftPane = new VBox(14);
        leftPane.setPrefWidth(340);
        leftPane.setMinWidth(320);

        leftPane.getChildren().add(UIHelper.pageHeader("Now Showing",
                "Browse and search available movies."));

        TextField searchF = UIHelper.tf("Search by title or genre...");

        FilteredList<Movie> filtered =
                new FilteredList<>(CinemaManager.getInstance().getMovies(), p -> true);

        searchF.textProperty().addListener((obs, o, n) ->
                filtered.setPredicate(m ->
                        n == null || n.isEmpty() ||
                        m.getTitle().toLowerCase().contains(n.toLowerCase()) ||
                        m.getGenre().toLowerCase().contains(n.toLowerCase())));

        ListView<Movie> movieList = new ListView<>(filtered);
        VBox.setVgrow(movieList, Priority.ALWAYS);
        movieList.setStyle("-fx-background-color:" + UIHelper.CARD +
                ";-fx-border-color:" + UIHelper.BORDER +
                ";-fx-border-radius:10;-fx-background-radius:10;");
        movieList.setFixedCellSize(60);

        movieList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Movie item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setStyle("-fx-background-color:transparent;");
                    return;
                }
                VBox cell = new VBox(3);
                cell.setPadding(new Insets(8, 12, 8, 12));
                cell.getChildren().addAll(
                    UIHelper.lbl(item.getTitle(), UIHelper.TEXT, 13, true),
                    UIHelper.lbl(item.getGenre() + "  •  " + item.getDurationFormatted()
                            + "  •  " + item.getRating(), UIHelper.TEXT2, 11, false));
                setGraphic(cell);
                setStyle("-fx-background-color:transparent;");
            }
        });

        leftPane.getChildren().addAll(searchF, movieList);

        // ── Right: showtime details ────────────────────────────────────────────
        VBox rightPane = new VBox(16);
        HBox.setHgrow(rightPane, Priority.ALWAYS);

        Label detailHeader = UIHelper.lbl("Select a movie to view showtimes",
                UIHelper.TEXT2, 14, false);

        VBox detailCard = UIHelper.card();
        VBox.setVgrow(detailCard, Priority.ALWAYS);
        detailCard.getChildren().add(detailHeader);

        rightPane.getChildren().addAll(
            UIHelper.pageHeader("Showtime Details", "Showtimes for the selected movie."),
            detailCard);

        movieList.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            detailCard.getChildren().clear();

            if (sel == null) {
                detailCard.getChildren().add(
                    UIHelper.lbl("Select a movie to view showtimes.", UIHelper.TEXT2, 13, false));
                return;
            }

            // Movie info
            VBox movieInfo = new VBox(6);
            movieInfo.getChildren().addAll(
                UIHelper.lbl(sel.getTitle(), UIHelper.TEXT, 18, true),
                UIHelper.lbl(
                    sel.getGenre() + "  •  " + sel.getDurationFormatted() +
                    "  •  " + sel.getRating() + "  •  " + sel.getReleaseYear(),
                    UIHelper.TEXT2, 12, false));
            if (!sel.getDescription().isEmpty())
                movieInfo.getChildren().add(
                    UIHelper.lbl(sel.getDescription(), UIHelper.TEXT2, 12, false));

            detailCard.getChildren().addAll(movieInfo, UIHelper.sep());

            // Showtimes for this movie
            boolean found = false;
            for (Showtime st : CinemaManager.getInstance().getShowtimes()) {
                if (st.getMovie() == sel) {
                    found = true;
                    HBox stRow = buildShowtimeRow(st);
                    detailCard.getChildren().add(stRow);
                }
            }
            if (!found) {
                detailCard.getChildren().add(
                    UIHelper.lbl("No showtimes scheduled for this movie.", UIHelper.TEXT2, 13, false));
            }
        });

        main.getChildren().addAll(leftPane, rightPane);

        ScrollPane scroll = new ScrollPane(main);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setStyle("-fx-background:transparent;-fx-background-color:transparent;");
        root.setCenter(scroll);
        return new Scene(root, 1280, 760);
    }

    private HBox buildShowtimeRow(Showtime st) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 12, 10, 12));
        row.setStyle("-fx-background-color:" + UIHelper.CARD2 +
                ";-fx-background-radius:8;");

        VBox info = new VBox(4);
        HBox.setHgrow(info, Priority.ALWAYS);
        int avail = st.getAvailableSeats();
        String aColor = avail == 0 ? "#e74c3c" : avail < 10 ? UIHelper.GOLD : UIHelper.GREEN;

        info.getChildren().addAll(
            UIHelper.lbl(st.getDate() + "  —  " + st.getTime(), UIHelper.TEXT, 13, true),
            UIHelper.lbl(st.getRoomName() + "  •  " + avail + " seats available  •  "
                + String.format("PHP %.0f", st.getPrice()), aColor, 12, false));

        Button sellBtn = UIHelper.primaryBtn("Sell Tickets");
        sellBtn.setPrefHeight(34);

        if (avail == 0) {
            sellBtn.setText("Sold Out");
            sellBtn.setDisable(true);
        } else {
            sellBtn.setOnAction(e ->
                stage.setScene(new SeatSelectionScreen(stage, cashier, st).getScene()));
        }

        row.getChildren().addAll(info, sellBtn);
        return row;
    }
}