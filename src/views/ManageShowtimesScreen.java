package views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
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

        TableColumn<Showtime, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Showtime, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<Showtime, String> roomCol = new TableColumn<>("Room");
        roomCol.setCellValueFactory(new PropertyValueFactory<>("roomName"));

        TableColumn<Showtime, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        table.getColumns().addAll(movieCol, dateCol, timeCol, roomCol, priceCol);

        // --- FORM ---
        ComboBox<Movie> movieCombo = new ComboBox<>();
        movieCombo.setItems(CinemaManager.getInstance().getMovies());
        movieCombo.setPromptText("Select Movie");

        TextField txtRoom = new TextField();
        txtRoom.setPromptText("Room (e.g. Cinema 1)");

        TextField txtDate = new TextField();
        txtDate.setPromptText("Date (e.g. May 12, 2026)");

        TextField txtTime = new TextField();
        txtTime.setPromptText("Time (e.g. 1:30 PM)");

        TextField txtPrice = new TextField();
        txtPrice.setPromptText("Price (PHP)");

        HBox form = new HBox(10, movieCombo, txtRoom, txtDate, txtTime, txtPrice);

        // --- BUTTONS ---
        Button addBtn = new Button("Add Showtime");
        Button updateBtn = new Button("Update Selected");
        Button deleteBtn = new Button("Delete Selected");
        Button backBtn = new Button("Back");

        HBox actions = new HBox(10, addBtn, updateBtn, deleteBtn, backBtn);

        // --- ADD ---
        addBtn.setOnAction(e -> {
            Movie selectedMovie = movieCombo.getValue();
            String room = txtRoom.getText().trim();
            String date = txtDate.getText().trim();
            String time = txtTime.getText().trim();
            String priceStr = txtPrice.getText().trim();

            if (selectedMovie != null && !room.isEmpty() && !date.isEmpty()
                    && !time.isEmpty() && !priceStr.isEmpty()) {
                try {
                    double price = Double.parseDouble(priceStr);

                    Showtime newST = new Showtime(selectedMovie, room, date, time, price, 5, 8);
                    CinemaManager.getInstance().addShowtime(newST);

                    movieCombo.setValue(null);
                    txtRoom.clear();
                    txtDate.clear();
                    txtTime.clear();
                    txtPrice.clear();

                } catch (NumberFormatException ex) {
                    showError("Invalid Price!");
                }
            } else {
                showError("Please fill all fields!");
            }
        });

        // --- LOAD SELECTED ---
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                movieCombo.setValue(newVal.getMovie());
                txtRoom.setText(newVal.getRoomName());
                txtDate.setText(newVal.getDate());
                txtTime.setText(newVal.getTime());
                txtPrice.setText(String.valueOf(newVal.getPrice()));
            }
        });

    
        updateBtn.setOnAction(e -> {
            Showtime selected = table.getSelectionModel().getSelectedItem();

            if (selected == null) {
                showError("Please select a showtime to update.");
                return;
            }

            String room = txtRoom.getText().trim();
            String date = txtDate.getText().trim();
            String time = txtTime.getText().trim();
            String priceStr = txtPrice.getText().trim();

            if (room.isEmpty() || date.isEmpty() || time.isEmpty() || priceStr.isEmpty()) {
                showError("Please fill all fields!");
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);

                selected.setRoomName(room);
                selected.setDate(date);
                selected.setTime(time);
                selected.setPrice(price);

                table.refresh();

            } catch (NumberFormatException ex) {
                showError("Invalid Price!");
            }
        });

        // --- DELETE ---
        deleteBtn.setOnAction(e -> {
            Showtime selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                CinemaManager.getInstance().deleteShowtime(selected);
            }
        });

        // --- BACK ---
        backBtn.setOnAction(e ->
                stage.setScene(new AdminDashboard(stage, admin).getScene())
        );

        root.getChildren().addAll(titleLabel, table, form, actions);
        return new Scene(root, 800, 600);
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.show();
    }
}