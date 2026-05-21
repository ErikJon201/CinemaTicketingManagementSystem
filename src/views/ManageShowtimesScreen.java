package views;

import javafx.beans.property.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.*;
import javafx.collections.transformation.FilteredList;

public class ManageShowtimesScreen {
    private Stage stage;
    private Admin admin;

    public ManageShowtimesScreen(Stage stage, Admin admin) {
        this.stage = stage;
        this.admin = admin;
    }

    public Scene getScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:" + UIHelper.BG + ";");

        String[] nav = {"Dashboard", "Movies", "Showtimes", "Theater Rooms", "Staff", "Sales Report"};
        Runnable[] acts = {
            () -> stage.setScene(new AdminDashboard(stage, admin).getScene()),
            () -> stage.setScene(new ManageMoviesScreen(stage, admin).getScene()),
            null,
            () -> stage.setScene(new ManageRoomsScreen(stage, admin).getScene()),
            () -> stage.setScene(new ManageUsersScreen(stage, admin).getScene()),
            () -> stage.setScene(new SalesReportScreen(stage, admin).getScene())
        };
        root.setLeft(UIHelper.sidebar(admin, "Showtimes", nav, acts,
                () -> stage.setScene(new LoginScreen(stage).getScene())));

        // ── Main ──────────────────────────────────────────────────────────────
        HBox main = new HBox(20);
        main.setPadding(new Insets(36, 36, 36, 36));
        main.setStyle("-fx-background-color:" + UIHelper.BG + ";");

        // ── Left: table ────────────────────────────────────────────────────────
        VBox leftPane = new VBox(16);
        HBox.setHgrow(leftPane, Priority.ALWAYS);

        HBox headerRow = new HBox(12);
        headerRow.setAlignment(Pos.CENTER_LEFT);
        VBox headerText = UIHelper.pageHeader("Showtime Schedule",
                "Manage movie showtimes, rooms, dates, and pricing.");
        HBox.setHgrow(headerText, Priority.ALWAYS);
        Button historyBtn = UIHelper.outlineBtn("Deleted History", UIHelper.GOLD);
        historyBtn.setOnAction(e -> showHistoryDialog());
        headerRow.getChildren().addAll(headerText, historyBtn);
        leftPane.getChildren().add(headerRow);

        TableView<Showtime> table = UIHelper.table();
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<Showtime, String> movieCol = UIHelper.col("Movie", 180);
        movieCol.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));

        TableColumn<Showtime, String> roomCol = UIHelper.col("Room", 130);
        roomCol.setCellValueFactory(new PropertyValueFactory<>("roomName"));

        TableColumn<Showtime, String> dateCol = UIHelper.col("Date", 130);
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Showtime, String> timeCol = UIHelper.col("Time", 90);
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<Showtime, Double> priceCol = UIHelper.col("Price", 90);
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Showtime, Integer> seatsCol = UIHelper.col("Available", 80);
        seatsCol.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));

        TableColumn<Showtime, String> statusCol = UIHelper.col("Status", 90);
        statusCol.setCellValueFactory(data -> {
            int avail = data.getValue().getAvailableSeats();
            String s = avail == 0 ? "Sold Out" : avail < 10 ? "Almost Full" : "Available";
            return new SimpleStringProperty(s);
        });

        table.getColumns().addAll(movieCol, roomCol, dateCol, timeCol, priceCol, seatsCol, statusCol);
        table.setItems(CinemaManager.getInstance().getShowtimes());
        leftPane.getChildren().add(table);

        // ── Right: form ────────────────────────────────────────────────────────
        VBox formCard = UIHelper.card();
        formCard.setPrefWidth(320);
        formCard.setMaxWidth(320);

        Label formTitle = UIHelper.lbl("Showtime Details", UIHelper.TEXT, 15, true);

        ComboBox<Movie> movieCb = UIHelper.cb();
        movieCb.setItems(CinemaManager.getInstance().getMovies());
        movieCb.setPromptText("Select movie");

        ComboBox<TheaterRoom> roomCb = UIHelper.cb();
        roomCb.setItems(CinemaManager.getInstance().getRooms());
        roomCb.setPromptText("Select room");

        TextField dateF  = UIHelper.tf("e.g. May 21, 2026");
        TextField timeF  = UIHelper.tf("e.g. 10:00 AM");
        TextField priceF = UIHelper.tf("Price in PHP (e.g. 380)");

        Label errorLbl = UIHelper.errorLbl();

        Button addBtn    = UIHelper.primaryBtn("Add Showtime");
        Button updateBtn = UIHelper.outlineBtn("Update Selected", UIHelper.GOLD);
        Button deleteBtn = UIHelper.outlineBtn("Delete Selected", "#e74c3c");
        Button clearBtn  = UIHelper.ghostBtn("Clear Form");

        for (Button b : new Button[]{addBtn, updateBtn, deleteBtn, clearBtn})
            b.setMaxWidth(Double.MAX_VALUE);

        formCard.getChildren().addAll(
            formTitle, UIHelper.sep(),
            UIHelper.formRow("Movie",  movieCb),
            UIHelper.formRow("Room",   roomCb),
            UIHelper.formRow("Date",   dateF),
            UIHelper.formRow("Time",   timeF),
            UIHelper.formRow("Price (PHP)", priceF),
            errorLbl, UIHelper.sep(),
            new VBox(8, addBtn, updateBtn, deleteBtn, clearBtn));

        // ── Logic ──────────────────────────────────────────────────────────────

        Runnable clear = () -> {
            movieCb.getSelectionModel().clearSelection();
            roomCb.getSelectionModel().clearSelection();
            dateF.clear(); timeF.clear(); priceF.clear();
            table.getSelectionModel().clearSelection();
            UIHelper.clearError(errorLbl);
        };

        clearBtn.setOnAction(e -> clear.run());

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                movieCb.setValue(sel.getMovie());
                roomCb.setValue(sel.getRoom());
                dateF.setText(sel.getDate());
                timeF.setText(sel.getTime());
                priceF.setText(String.valueOf(sel.getPrice()));
            }
        });

        addBtn.setOnAction(e -> {
            if (!validateForm(movieCb, roomCb, dateF, timeF, priceF, errorLbl)) return;
            Showtime st = new Showtime(
                movieCb.getValue(), roomCb.getValue(),
                dateF.getText().trim(), timeF.getText().trim(),
                Double.parseDouble(priceF.getText().trim()));
            CinemaManager.getInstance().addShowtime(st);
            clear.run();
        });

        updateBtn.setOnAction(e -> {
            Showtime sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { UIHelper.showError(errorLbl, "Select a showtime to update."); return; }
            if (!validateForm(movieCb, roomCb, dateF, timeF, priceF, errorLbl)) return;
            sel.setMovie(movieCb.getValue());
            sel.setRoom(roomCb.getValue());
            sel.setDate(dateF.getText().trim());
            sel.setTime(timeF.getText().trim());
            sel.setPrice(Double.parseDouble(priceF.getText().trim()));
            table.refresh();
            clear.run();
        });

        deleteBtn.setOnAction(e -> {
            Showtime sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { UIHelper.showError(errorLbl, "Select a showtime to delete."); return; }
            Alert a = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete showtime for \"" + sel.getMovieTitle() + "\"?",
                ButtonType.YES, ButtonType.NO);
            a.setHeaderText(null);
            a.showAndWait().ifPresent(bt -> {
                if (bt == ButtonType.YES) { CinemaManager.getInstance().deleteShowtime(sel, admin.getFullName()); clear.run(); }
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

    private void showHistoryDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Deleted Showtimes History");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        VBox content = new VBox(16);
        content.setPadding(new Insets(20, 24, 8, 24));
        content.setPrefWidth(900);
        content.setStyle("-fx-background-color:" + UIHelper.BG + ";");

        content.getChildren().addAll(
            UIHelper.lbl("Deleted Showtimes History", UIHelper.TEXT, 18, true),
            UIHelper.lbl("Showtimes removed from the schedule. You can restore any entry.",
                    UIHelper.TEXT2, 13, false),
            UIHelper.sep());

        TableView<DeletedShowtime> histTable = UIHelper.table();
        histTable.setPrefHeight(400);
        histTable.setItems(CinemaManager.getInstance().getDeletedShowtimes());

        TableColumn<DeletedShowtime, String> mCol = UIHelper.col("Movie", 180);
        mCol.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));

        TableColumn<DeletedShowtime, String> rCol = UIHelper.col("Room", 120);
        rCol.setCellValueFactory(new PropertyValueFactory<>("roomName"));

        TableColumn<DeletedShowtime, String> dCol = UIHelper.col("Date", 120);
        dCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<DeletedShowtime, String> tCol = UIHelper.col("Time", 90);
        tCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<DeletedShowtime, Double> pCol = UIHelper.col("Price", 80);
        pCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<DeletedShowtime, String> byCol = UIHelper.col("Deleted By", 130);
        byCol.setCellValueFactory(new PropertyValueFactory<>("deletedBy"));

        TableColumn<DeletedShowtime, String> atCol = UIHelper.col("Deleted At", 170);
        atCol.setCellValueFactory(new PropertyValueFactory<>("deletedAtFormatted"));

        TableColumn<DeletedShowtime, Void> actionCol = new TableColumn<>("Action");
        actionCol.setMinWidth(100);
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button restoreBtn = UIHelper.outlineBtn("Restore", UIHelper.GREEN);
            {
                restoreBtn.setPrefHeight(28);
                restoreBtn.setOnAction(e -> {
                    DeletedShowtime ds = getTableView().getItems().get(getIndex());
                    CinemaManager.getInstance().restoreShowtime(ds);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : restoreBtn);
            }
        });

        histTable.getColumns().addAll(mCol, rCol, dCol, tCol, pCol, byCol, atCol, actionCol);
        histTable.setPlaceholder(UIHelper.lbl("No showtimes have been deleted yet.", UIHelper.TEXT2, 13, false));

        content.getChildren().add(histTable);
        VBox.setVgrow(histTable, Priority.ALWAYS);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setStyle("-fx-background-color:" + UIHelper.BG + ";");
        dialog.showAndWait();
    }

    private boolean validateForm(ComboBox<Movie> movieCb, ComboBox<TheaterRoom> roomCb,
                                  TextField dateF, TextField timeF, TextField priceF, Label err) {
        if (movieCb.getValue() == null) { UIHelper.showError(err, "Select a movie."); return false; }
        if (roomCb.getValue()  == null) { UIHelper.showError(err, "Select a room."); return false; }
        if (dateF.getText().trim().isEmpty()) { UIHelper.showError(err, "Date is required."); return false; }
        if (timeF.getText().trim().isEmpty()) { UIHelper.showError(err, "Time is required."); return false; }
        try { Double.parseDouble(priceF.getText().trim()); }
        catch (NumberFormatException ex) { UIHelper.showError(err, "Price must be a number."); return false; }
        UIHelper.clearError(err);
        return true;
    }
}