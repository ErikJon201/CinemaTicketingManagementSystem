package views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.Admin;
import models.CinemaManager;
import models.Movie;

public class ManageMoviesScreen {

    private Stage stage;
    private Admin admin;
    private TableView<Movie> table;

    public ManageMoviesScreen(Stage stage, Admin admin) {
        this.stage = stage;
        this.admin = admin;
    }

    public Scene getScene() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Manage Movies");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // --- TABLE VIEW ---
        table = new TableView<>();
        table.setItems(CinemaManager.getInstance().getMovies());

        TableColumn<Movie, String> nameCol = new TableColumn<>("Title");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Movie, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));

        TableColumn<Movie, Integer> durCol = new TableColumn<>("Duration (min)");
        durCol.setCellValueFactory(new PropertyValueFactory<>("durationMinutes"));

        table.getColumns().addAll(nameCol, genreCol, durCol);

        // --- FORM FIELDS ---
        TextField txtTitle = new TextField();
        txtTitle.setPromptText("Movie Title");
        TextField txtGenre = new TextField();
        txtGenre.setPromptText("Genre");
        TextField txtDuration = new TextField();
        txtDuration.setPromptText("Duration");

        HBox form = new HBox(10, txtTitle, txtGenre, txtDuration);

        // --- BUTTONS ---
        Button addBtn = new Button("Add Movie");
        Button updateBtn = new Button("Update Selected");
        Button deleteBtn = new Button("Delete Selected");
        Button backBtn = new Button("Back");

        HBox actions = new HBox(10, addBtn, updateBtn, deleteBtn, backBtn);

        // --- LOGIC ---

        // Add
        addBtn.setOnAction(e -> {
            try {
                String title = txtTitle.getText();
                String genre = txtGenre.getText();
                int duration = Integer.parseInt(txtDuration.getText());
                CinemaManager.getInstance().addMovie(new Movie(title, genre, duration));
                txtTitle.clear(); txtGenre.clear(); txtDuration.clear();
            } catch (NumberFormatException ex) {
                showError("Duration must be a number!");
            }
        });

        // Load selection into fields
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtTitle.setText(newSelection.getTitle());
                txtGenre.setText(newSelection.getGenre());
                txtDuration.setText(String.valueOf(newSelection.getDuration()));
            }
        });

        // Update
        updateBtn.setOnAction(e -> {
            Movie selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setTitle(txtTitle.getText());
                selected.setGenre(txtGenre.getText());
                selected.setDuration(Integer.parseInt(txtDuration.getText()));
                CinemaManager.getInstance().getMovies().add(selected);// Update table display
            }
        });

        // Delete
        deleteBtn.setOnAction(e -> {
            Movie selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                CinemaManager.getInstance().deleteMovie(selected);
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
