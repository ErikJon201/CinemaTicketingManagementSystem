package views;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
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
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label header = new Label("Search Movies & Showtimes");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // 1. Search Bar
        TextField searchBar = new TextField();
        searchBar.setPromptText("Type movie title to search...");

        // 2. Movie List (Data from CinemaManager)
        ObservableList<Movie> movieData = FXCollections.observableArrayList(
                CinemaManager.getInstance().getMovies()
        );

        // FilteredList wrap the data so it updates automatically
        FilteredList<Movie> filteredData = new FilteredList<>(movieData, p -> true);

        // Listen to text changes in searchBar
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(movie -> {
                if (newValue == null || newValue.isEmpty()) return true;
                return movie.getTitle().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        ListView<Movie> movieListView = new ListView<>(filteredData);
        movieListView.setPrefHeight(150);

        // 3. Showtime Display Area
        Label showtimeLabel = new Label("Available Showtimes:");
        ListView<String> showtimeListView = new ListView<>();
        showtimeListView.setPrefHeight(150);

        // Logic: When a movie is selected, find its showtimes
        movieListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                showtimeListView.getItems().clear();
                for (Showtime st : CinemaManager.getInstance().getShowtimes()) {
                    if (st.getMovie().getTitle().equals(newVal.getTitle())) {
                        showtimeListView.getItems().add(st.getTime() + " - PHP " + st.getPrice());
                    }
                }
                if (showtimeListView.getItems().isEmpty()) {
                    showtimeListView.getItems().add("No showtimes found for this movie.");
                }
            }
        });

        // 4. Buttons
        Button backBtn = new Button("Back to Dashboard");
        backBtn.setOnAction(e -> stage.setScene(new CashierDashboard(stage, cashier).getScene()));

        // Layout assembly
        root.getChildren().addAll(header, searchBar, new Label("Select Movie:"), movieListView, showtimeLabel, showtimeListView, backBtn);

        return new Scene(root, 800, 500);
    }
}
