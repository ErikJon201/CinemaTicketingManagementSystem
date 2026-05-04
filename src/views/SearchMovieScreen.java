package views;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
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
        Label portalTag = new Label("SEARCH MOVIES");
        portalTag.setStyle("-fx-text-fill: #3d4560; -fx-font-size: 10px; -fx-font-weight: bold;");
        brandBox.getChildren().addAll(brand, portalTag);

        VBox sideTop = new VBox(0);
        sideTop.getChildren().addAll(
            new HBox(accentBar, brandBox) {{ setAlignment(Pos.CENTER_LEFT); }}
        );

        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setMaxWidth(Double.MAX_VALUE);
        backBtn.setPadding(new Insets(13, 0, 13, 0));
        backBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #3d4560;" +
            "-fx-font-size: 12px;" +
            "-fx-cursor: hand;"
        );
        backBtn.setOnMouseEntered(e -> backBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #7a849a;" +
            "-fx-font-size: 12px; -fx-cursor: hand;"
        ));
        backBtn.setOnMouseExited(e -> backBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #3d4560;" +
            "-fx-font-size: 12px; -fx-cursor: hand;"
        ));

        VBox sideBottom = new VBox(6);
        sideBottom.setPadding(new Insets(0, 16, 24, 16));
        sideBottom.getChildren().add(backBtn);

        sidebar.setTop(sideTop);
        sidebar.setBottom(sideBottom);

        // ── Main Content ──────────────────────────────────────────────
        VBox content = new VBox(20);
        content.setPadding(new Insets(40, 50, 40, 50));

        Label heading = new Label("Search Movies");
        heading.setStyle(
            "-fx-text-fill: #eaeaea;" +
            "-fx-font-size: 26px;" +
            "-fx-font-weight: bold;"
        );
        Label sub = new Label("Find a movie and view its available showtimes");
        sub.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 13px;");

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #2b3250;");

        // ── Search Bar ────────────────────────────────────────────────
        TextField searchBar = new TextField();
        searchBar.setPromptText("🔍  Type movie title to search...");
        searchBar.setStyle(
            "-fx-background-color: #0f1422;" +
            "-fx-text-fill: #eaeaea;" +
            "-fx-prompt-text-fill: #3d4560;" +
            "-fx-border-color: #2b3250;" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-border-width: 1;" +
            "-fx-padding: 10 14;" +
            "-fx-font-size: 13px;"
        );

        // ── Movie List ────────────────────────────────────────────────
        ObservableList<Movie> movieData = FXCollections.observableArrayList(
            CinemaManager.getInstance().getMovies()
        );
        FilteredList<Movie> filteredData = new FilteredList<>(movieData, p -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(movie -> {
                if (newValue == null || newValue.isEmpty()) return true;
                return movie.getTitle().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        Label selectLabel = sectionLabel("SELECT MOVIE");

        ListView<Movie> movieListView = new ListView<>(filteredData);
        movieListView.setPrefHeight(160);
        movieListView.setStyle(listStyle());
        movieListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Movie item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    setText("🎬  " + item.getTitle());
                    setStyle(
                        "-fx-background-color: transparent;" +
                        "-fx-text-fill: #eaeaea;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 8 12;"
                    );
                }
            }
        });

        // ── Showtime Panel ────────────────────────────────────────────
        Label showtimeLabel = sectionLabel("AVAILABLE SHOWTIMES");

        ListView<String> showtimeListView = new ListView<>();
        showtimeListView.setPrefHeight(160);
        showtimeListView.setStyle(listStyle());
        showtimeListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    setText(item);
                    boolean isGold = item.startsWith("PHP") || item.contains("PHP");
                    setStyle(
                        "-fx-background-color: transparent;" +
                        "-fx-text-fill: " + (isGold ? "#c9a84c" : "#7a849a") + ";" +
                        "-fx-font-size: 12px;" +
                        "-fx-padding: 6 12;"
                    );
                }
            }
        });

        // ── Logic (unchanged) ─────────────────────────────────────────
        movieListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                showtimeListView.getItems().clear();
                for (Showtime st : CinemaManager.getInstance().getShowtimes()) {
                    if (st.getMovie().getTitle().equals(newVal.getTitle())) {
                        showtimeListView.getItems().add("Title: " + st.getMovieTitle());
                        showtimeListView.getItems().add("Genre: " + st.getMovieGenre());
                        showtimeListView.getItems().add("Duration: " + st.getMovieDuration() + " minutes");
                        showtimeListView.getItems().add(st.getTime() + " - PHP " + st.getPrice());
                    }
                }
                if (showtimeListView.getItems().isEmpty()) {
                    showtimeListView.getItems().add("No showtimes found for this movie.");
                }
            }
        });

        backBtn.setOnAction(e -> stage.setScene(new CashierDashboard(stage, cashier).getScene()));

        // Two-column layout for lists
        HBox listsRow = new HBox(20);
        VBox leftCol  = new VBox(8, selectLabel, movieListView);
        VBox rightCol = new VBox(8, showtimeLabel, showtimeListView);
        HBox.setHgrow(leftCol, Priority.ALWAYS);
        HBox.setHgrow(rightCol, Priority.ALWAYS);
        listsRow.getChildren().addAll(leftCol, rightCol);

        content.getChildren().addAll(heading, sub, sep, searchBar, listsRow);

        root.setLeft(sidebar);
        root.setCenter(content);
        return new Scene(root, 900, 560);
    }

    // ── Helpers ───────────────────────────────────────────────────────

    private Label sectionLabel(String text) {
        Label lbl = new Label(text);
        lbl.setStyle(
            "-fx-text-fill: #7a849a;" +
            "-fx-font-size: 10px;" +
            "-fx-font-weight: bold;"
        );
        return lbl;
    }

    private String listStyle() {
        return  "-fx-background-color: #0f1422;" +
                "-fx-border-color: #2b3250;" +
                "-fx-border-radius: 4;" +
                "-fx-background-radius: 4;" +
                "-fx-border-width: 1;";
    }
}