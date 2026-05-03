package views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.*;

public class ManageShowtimesScreen {
    private Stage stage;
    private Admin admin;
    private TableView<Showtime> table;

    public ManageShowtimesScreen(Stage stage, Admin admin) {
        this.stage = stage;
        this.admin = admin;
    }

    public Scene getScene() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Manage Showtimes");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // --- TABLE VIEW ---
        table = new TableView<>();
        table.setItems(CinemaManager.getInstance().getShowtimes());

        TableColumn<Showtime, String> movieCol = new TableColumn<>("Movie");
        movieCol.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));

        TableColumn<Showtime, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<Showtime, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        table.getColumns().addAll(movieCol, timeCol, priceCol);

        // --- FORM ---
        // ComboBox to select a movie from existing movies
        ComboBox<Movie> movieCombo = new ComboBox<>();
        movieCombo.setItems(CinemaManager.getInstance().getMovies());
        movieCombo.setPromptText("Select Movie");

        TextField txtTime = new TextField();
        txtTime.setPromptText("Time (e.g. 1:30 PM)");

        TextField txtPrice = new TextField();
        txtPrice.setPromptText("Price (PHP)");

        HBox form = new HBox(10, movieCombo, txtTime, txtPrice);

        // --- BUTTONS ---
        Button addBtn = new Button("Add Showtime");
        Button deleteBtn = new Button("Delete Selected");
        Button backBtn = new Button("Back");

        HBox actions = new HBox(10, addBtn, deleteBtn, backBtn);

        // --- LOGIC ---

        addBtn.setOnAction(e -> {
            Movie selectedMovie = movieCombo.getValue();
            String time = txtTime.getText();
            String priceStr = txtPrice.getText();

            if (selectedMovie != null && !time.isEmpty() && !priceStr.isEmpty()) {
                try {
                    double price = Double.parseDouble(priceStr);
                    // Create new showtime with a default 5x8 seating grid
                    Showtime newST = new Showtime(selectedMovie, "Room 1", time, price, 5, 8);
                    CinemaManager.getInstance().addShowtime(newST);

                    txtTime.clear();
                    txtPrice.clear();
                } catch (NumberFormatException ex) {
                    showError("Invalid Price!");
                }
            } else {
                showError("Please fill all fields!");
            }
        });

        deleteBtn.setOnAction(e -> {
            Showtime selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                CinemaManager.getInstance().deleteShowtime(selected);
            }
        });

        backBtn.setOnAction(e -> stage.setScene(new AdminDashboard(stage, admin).getScene()));

        root.getChildren().addAll(titleLabel, table, form, actions);
        return new Scene(root, 800, 600);
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.show();
    }
}