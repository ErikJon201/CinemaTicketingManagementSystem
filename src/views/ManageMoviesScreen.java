package views;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.*;

public class ManageMoviesScreen {
    private Stage stage;
    private Admin admin;

    public ManageMoviesScreen(Stage stage, Admin admin) {
        this.stage = stage;
        this.admin = admin;
    }

    public Scene getScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:" + UIHelper.BG + ";");

        // ── Sidebar ────────────────────────────────────────────────────────────
        String[] nav = {"Dashboard", "Movies", "Showtimes", "Theater Rooms", "Staff", "Sales Report"};
        Runnable[] acts = {
            () -> stage.setScene(new AdminDashboard(stage, admin).getScene()),
            null,
            () -> stage.setScene(new ManageShowtimesScreen(stage, admin).getScene()),
            () -> stage.setScene(new ManageRoomsScreen(stage, admin).getScene()),
            () -> stage.setScene(new ManageUsersScreen(stage, admin).getScene()),
            () -> stage.setScene(new SalesReportScreen(stage, admin).getScene())
        };
        root.setLeft(UIHelper.sidebar(admin, "Movies", nav, acts,
                () -> stage.setScene(new LoginScreen(stage).getScene())));

        // ── Main content ───────────────────────────────────────────────────────
        HBox main = new HBox(20);
        main.setPadding(new Insets(36, 36, 36, 36));
        main.setStyle("-fx-background-color:" + UIHelper.BG + ";");

        // ── Left: table ────────────────────────────────────────────────────────
        VBox leftPane = new VBox(16);
        HBox.setHgrow(leftPane, Priority.ALWAYS);

        // Header row with title and History button
        HBox headerRow = new HBox(12);
        headerRow.setAlignment(Pos.CENTER_LEFT);
        VBox headerText = UIHelper.pageHeader("Movie Catalog",
                "Add, edit, or remove movies from the system.");
        HBox.setHgrow(headerText, Priority.ALWAYS);
        Button historyBtn = UIHelper.outlineBtn("Deleted History", UIHelper.GOLD);
        historyBtn.setOnAction(e -> showHistoryDialog());
        headerRow.getChildren().addAll(headerText, historyBtn);

        // Search bar
        TextField searchF = UIHelper.tf("Search movies...");
        searchF.setMaxWidth(320);

        FilteredList<Movie> filtered =
                new FilteredList<>(CinemaManager.getInstance().getMovies(), p -> true);
        searchF.textProperty().addListener((obs, o, n) ->
                filtered.setPredicate(m -> n == null || n.isEmpty() ||
                        m.getTitle().toLowerCase().contains(n.toLowerCase()) ||
                        m.getGenre().toLowerCase().contains(n.toLowerCase())));

        TableView<Movie> table = UIHelper.table();
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<Movie, Integer> idCol = UIHelper.col("#", 44);
        idCol.setCellValueFactory(new PropertyValueFactory<>("movieId"));

        TableColumn<Movie, String> titleCol = UIHelper.col("Title", 180);
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Movie, String> genreCol = UIHelper.col("Genre", 100);
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));

        TableColumn<Movie, String> ratingCol = UIHelper.col("Rating", 70);
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));

        TableColumn<Movie, String> durCol = UIHelper.col("Duration", 90);
        durCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDurationFormatted()));

        TableColumn<Movie, Integer> yearCol = UIHelper.col("Year", 60);
        yearCol.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));

        table.getColumns().addAll(idCol, titleCol, genreCol, ratingCol, durCol, yearCol);
        table.setItems(filtered);

        leftPane.getChildren().addAll(headerRow, searchF, table);

        // ── Right: form ────────────────────────────────────────────────────────
        VBox formCard = UIHelper.card();
        formCard.setPrefWidth(320);
        formCard.setMaxWidth(320);

        Label formTitle = UIHelper.lbl("Movie Details", UIHelper.TEXT, 15, true);

        TextField titleF  = UIHelper.tf("e.g. Avengers: Endgame");
        TextField genreF  = UIHelper.tf("e.g. Action");
        TextField durF    = UIHelper.tf("Duration in minutes (e.g. 150)");
        TextField yearF   = UIHelper.tf("e.g. 2025");
        ComboBox<String> ratingCb = UIHelper.cb();
        ratingCb.getItems().addAll("G", "PG", "PG-13", "R", "NC-17");
        ratingCb.setPromptText("Select rating");
        TextArea descA = UIHelper.ta("Brief movie description...", 3);

        Label errorLbl = UIHelper.errorLbl();

        Button addBtn    = UIHelper.primaryBtn("Add Movie");
        Button updateBtn = UIHelper.outlineBtn("Update Selected", UIHelper.GOLD);
        Button deleteBtn = UIHelper.outlineBtn("Delete Selected", "#e74c3c");
        Button clearBtn  = UIHelper.ghostBtn("Clear Form");

        for (Button b : new Button[]{addBtn, updateBtn, deleteBtn, clearBtn})
            b.setMaxWidth(Double.MAX_VALUE);

        formCard.getChildren().addAll(
            formTitle, UIHelper.sep(),
            UIHelper.formRow("Title",          titleF),
            UIHelper.formRow("Genre",          genreF),
            UIHelper.formRow("Rating",         ratingCb),
            UIHelper.formRow("Duration (min)", durF),
            UIHelper.formRow("Release Year",   yearF),
            UIHelper.formRow("Description",    descA),
            errorLbl, UIHelper.sep(),
            new VBox(8, addBtn, updateBtn, deleteBtn, clearBtn));

        // ── Logic ──────────────────────────────────────────────────────────────

        Runnable clearForm = () -> {
            titleF.clear(); genreF.clear(); durF.clear();
            yearF.clear(); descA.clear();
            ratingCb.getSelectionModel().clearSelection();
            table.getSelectionModel().clearSelection();
            UIHelper.clearError(errorLbl);
        };

        clearBtn.setOnAction(e -> clearForm.run());

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                titleF.setText(sel.getTitle());
                genreF.setText(sel.getGenre());
                ratingCb.setValue(sel.getRating());
                durF.setText(String.valueOf(sel.getDuration()));
                yearF.setText(String.valueOf(sel.getReleaseYear()));
                descA.setText(sel.getDescription());
                UIHelper.clearError(errorLbl);
            }
        });

        addBtn.setOnAction(e -> {
            if (!validateForm(titleF, genreF, ratingCb, durF, yearF, errorLbl)) return;
            Movie m = new Movie(
                titleF.getText().trim(),
                genreF.getText().trim(),
                Integer.parseInt(durF.getText().trim()),
                ratingCb.getValue(),
                descA.getText().trim(),
                Integer.parseInt(yearF.getText().trim()));
            CinemaManager.getInstance().addMovie(m);
            clearForm.run();
        });

        updateBtn.setOnAction(e -> {
            Movie sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { UIHelper.showError(errorLbl, "Select a movie to update."); return; }
            if (!validateForm(titleF, genreF, ratingCb, durF, yearF, errorLbl)) return;
            sel.setTitle(titleF.getText().trim());
            sel.setGenre(genreF.getText().trim());
            sel.setRating(ratingCb.getValue());
            sel.setDuration(Integer.parseInt(durF.getText().trim()));
            sel.setReleaseYear(Integer.parseInt(yearF.getText().trim()));
            sel.setDescription(descA.getText().trim());
            table.refresh();
            clearForm.run();
        });

        deleteBtn.setOnAction(e -> {
            Movie sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { UIHelper.showError(errorLbl, "Select a movie to delete."); return; }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete \"" + sel.getTitle() + "\"?", ButtonType.YES, ButtonType.NO);
            confirm.setHeaderText(null);
            confirm.showAndWait().ifPresent(bt -> {
                if (bt == ButtonType.YES) {
                    CinemaManager.getInstance().deleteMovie(sel, admin.getFullName());
                    clearForm.run();
                }
            });
        });

        main.getChildren().addAll(leftPane, formCard);

        ScrollPane scroll = new ScrollPane(main);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setStyle("-fx-background:transparent;-fx-background-color:transparent;");
        root.setCenter(scroll);

        return new Scene(root, 1280, 760);
    }

    // ── Deleted Movies History Dialog ──────────────────────────────────────────

    private void showHistoryDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Deleted Movies History");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        VBox content = new VBox(16);
        content.setPadding(new Insets(20, 24, 8, 24));
        content.setPrefWidth(860);
        content.setStyle("-fx-background-color:" + UIHelper.BG + ";");

        content.getChildren().addAll(
            UIHelper.lbl("Deleted Movies History", UIHelper.TEXT, 18, true),
            UIHelper.lbl("Movies deleted from the catalog. You can restore any entry.",
                    UIHelper.TEXT2, 13, false),
            UIHelper.sep());

        TableView<DeletedMovie> histTable = UIHelper.table();
        histTable.setPrefHeight(400);
        histTable.setItems(CinemaManager.getInstance().getDeletedMovies());

        TableColumn<DeletedMovie, String> tCol = UIHelper.col("Title", 180);
        tCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<DeletedMovie, String> gCol = UIHelper.col("Genre", 90);
        gCol.setCellValueFactory(new PropertyValueFactory<>("genre"));

        TableColumn<DeletedMovie, String> rCol = UIHelper.col("Rating", 70);
        rCol.setCellValueFactory(new PropertyValueFactory<>("rating"));

        TableColumn<DeletedMovie, String> dCol = UIHelper.col("Duration", 80);
        dCol.setCellValueFactory(new PropertyValueFactory<>("durationFormatted"));

        TableColumn<DeletedMovie, Integer> yCol = UIHelper.col("Year", 60);
        yCol.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));

        TableColumn<DeletedMovie, String> byCol = UIHelper.col("Deleted By", 130);
        byCol.setCellValueFactory(new PropertyValueFactory<>("deletedBy"));

        TableColumn<DeletedMovie, String> atCol = UIHelper.col("Deleted At", 180);
        atCol.setCellValueFactory(new PropertyValueFactory<>("deletedAtFormatted"));

        TableColumn<DeletedMovie, Void> actionCol = new TableColumn<>("Action");
        actionCol.setMinWidth(100);
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button restoreBtn = UIHelper.outlineBtn("Restore", UIHelper.GREEN);
            {
                restoreBtn.setPrefHeight(28);
                restoreBtn.setOnAction(e -> {
                    DeletedMovie dm = getTableView().getItems().get(getIndex());
                    CinemaManager.getInstance().restoreMovie(dm);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : restoreBtn);
            }
        });

        histTable.getColumns().addAll(tCol, gCol, rCol, dCol, yCol, byCol, atCol, actionCol);
        histTable.setPlaceholder(UIHelper.lbl("No movies have been deleted yet.", UIHelper.TEXT2, 13, false));

        content.getChildren().add(histTable);
        VBox.setVgrow(histTable, Priority.ALWAYS);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setStyle("-fx-background-color:" + UIHelper.BG + ";");
        dialog.showAndWait();
    }

    private boolean validateForm(TextField titleF, TextField genreF,
                                  ComboBox<String> ratingCb, TextField durF,
                                  TextField yearF, Label err) {
        if (titleF.getText().trim().isEmpty()) {
            UIHelper.showError(err, "Title is required."); return false; }
        if (genreF.getText().trim().isEmpty()) {
            UIHelper.showError(err, "Genre is required."); return false; }
        if (ratingCb.getValue() == null) {
            UIHelper.showError(err, "Rating is required."); return false; }
        try { Integer.parseInt(durF.getText().trim()); }
        catch (NumberFormatException ex) {
            UIHelper.showError(err, "Duration must be a whole number."); return false; }
        try { Integer.parseInt(yearF.getText().trim()); }
        catch (NumberFormatException ex) {
            UIHelper.showError(err, "Release year must be a whole number."); return false; }
        UIHelper.clearError(err);
        return true;
    }
}