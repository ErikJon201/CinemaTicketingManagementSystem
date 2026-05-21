package views;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.*;
import javafx.scene.layout.Priority;

public class ManageRoomsScreen {
    private Stage stage;
    private Admin admin;

    public ManageRoomsScreen(Stage stage, Admin admin) {
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
            () -> stage.setScene(new ManageShowtimesScreen(stage, admin).getScene()),
            null,
            () -> stage.setScene(new ManageUsersScreen(stage, admin).getScene()),
            () -> stage.setScene(new SalesReportScreen(stage, admin).getScene())
        };
        root.setLeft(UIHelper.sidebar(admin, "Theater Rooms", nav, acts,
                () -> stage.setScene(new LoginScreen(stage).getScene())));

        HBox main = new HBox(20);
        main.setPadding(new Insets(36, 36, 36, 36));
        main.setStyle("-fx-background-color:" + UIHelper.BG + ";");

        // ── Left: table ────────────────────────────────────────────────────────
        VBox leftPane = new VBox(16);
        HBox.setHgrow(leftPane, Priority.ALWAYS);

        HBox headerRow = new HBox(12);
        headerRow.setAlignment(Pos.CENTER_LEFT);
        VBox headerText = UIHelper.pageHeader("Theater Rooms",
                "Configure rooms, seating layout, and types.");
        HBox.setHgrow(headerText, Priority.ALWAYS);
        Button historyBtn = UIHelper.outlineBtn("Deleted History", UIHelper.GOLD);
        historyBtn.setOnAction(e -> showHistoryDialog());
        headerRow.getChildren().addAll(headerText, historyBtn);
        leftPane.getChildren().add(headerRow);

        TableView<TheaterRoom> table = UIHelper.table();
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<TheaterRoom, String> nameCol = UIHelper.col("Room Name", 160);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<TheaterRoom, String> typeCol = UIHelper.col("Type", 100);
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<TheaterRoom, Integer> rowsCol = UIHelper.col("Rows", 70);
        rowsCol.setCellValueFactory(new PropertyValueFactory<>("rows"));

        TableColumn<TheaterRoom, Integer> colsCol = UIHelper.col("Columns", 80);
        colsCol.setCellValueFactory(new PropertyValueFactory<>("cols"));

        TableColumn<TheaterRoom, Integer> capCol = UIHelper.col("Capacity", 90);
        capCol.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getCapacity()).asObject());

        table.getColumns().addAll(nameCol, typeCol, rowsCol, colsCol, capCol);
        table.setItems(CinemaManager.getInstance().getRooms());
        leftPane.getChildren().add(table);

        // ── Right: form ────────────────────────────────────────────────────────
        VBox formCard = UIHelper.card();
        formCard.setPrefWidth(290);
        formCard.setMaxWidth(290);

        Label formTitle = UIHelper.lbl("Room Details", UIHelper.TEXT, 15, true);

        TextField nameF = UIHelper.tf("e.g. Cinema 5");
        TextField rowsF = UIHelper.tf("Number of rows (e.g. 8)");
        TextField colsF = UIHelper.tf("Seats per row (e.g. 12)");
        ComboBox<String> typeCb = UIHelper.cb();
        typeCb.getItems().addAll(
            TheaterRoom.STANDARD, TheaterRoom.PREMIUM,
            TheaterRoom.IMAX, TheaterRoom.FOUR_DX);
        typeCb.setPromptText("Select type");

        Label errorLbl = UIHelper.errorLbl();

        Button addBtn    = UIHelper.primaryBtn("Add Room");
        Button updateBtn = UIHelper.outlineBtn("Update Selected", UIHelper.GOLD);
        Button deleteBtn = UIHelper.outlineBtn("Delete Selected", "#e74c3c");
        Button clearBtn  = UIHelper.ghostBtn("Clear Form");

        for (Button b : new Button[]{addBtn, updateBtn, deleteBtn, clearBtn})
            b.setMaxWidth(Double.MAX_VALUE);

        formCard.getChildren().addAll(
            formTitle, UIHelper.sep(),
            UIHelper.formRow("Room Name", nameF),
            UIHelper.formRow("Type",      typeCb),
            UIHelper.formRow("Rows",      rowsF),
            UIHelper.formRow("Columns",   colsF),
            errorLbl, UIHelper.sep(),
            new VBox(8, addBtn, updateBtn, deleteBtn, clearBtn));

        // ── Logic ──────────────────────────────────────────────────────────────

        Runnable clear = () -> {
            nameF.clear(); rowsF.clear(); colsF.clear();
            typeCb.getSelectionModel().clearSelection();
            table.getSelectionModel().clearSelection();
            UIHelper.clearError(errorLbl);
        };

        clearBtn.setOnAction(e -> clear.run());

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                nameF.setText(sel.getName());
                typeCb.setValue(sel.getType());
                rowsF.setText(String.valueOf(sel.getRows()));
                colsF.setText(String.valueOf(sel.getCols()));
            }
        });

        addBtn.setOnAction(e -> {
            if (!validate(nameF, typeCb, rowsF, colsF, errorLbl)) return;
            CinemaManager.getInstance().addRoom(new TheaterRoom(
                nameF.getText().trim(), typeCb.getValue(),
                Integer.parseInt(rowsF.getText().trim()),
                Integer.parseInt(colsF.getText().trim())));
            clear.run();
        });

        updateBtn.setOnAction(e -> {
            TheaterRoom sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { UIHelper.showError(errorLbl, "Select a room to update."); return; }
            if (!validate(nameF, typeCb, rowsF, colsF, errorLbl)) return;
            sel.setName(nameF.getText().trim());
            sel.setType(typeCb.getValue());
            sel.setRows(Integer.parseInt(rowsF.getText().trim()));
            sel.setCols(Integer.parseInt(colsF.getText().trim()));
            table.refresh();
            clear.run();
        });

        deleteBtn.setOnAction(e -> {
            TheaterRoom sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { UIHelper.showError(errorLbl, "Select a room to delete."); return; }
            Alert a = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete room \"" + sel.getName() + "\"?", ButtonType.YES, ButtonType.NO);
            a.setHeaderText(null);
            a.showAndWait().ifPresent(bt -> {
                if (bt == ButtonType.YES) { CinemaManager.getInstance().deleteRoom(sel, admin.getFullName()); clear.run(); }
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
        dialog.setTitle("Deleted Theater Rooms History");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        VBox content = new VBox(16);
        content.setPadding(new Insets(20, 24, 8, 24));
        content.setPrefWidth(820);
        content.setStyle("-fx-background-color:" + UIHelper.BG + ";");

        content.getChildren().addAll(
            UIHelper.lbl("Deleted Theater Rooms History", UIHelper.TEXT, 18, true),
            UIHelper.lbl("Rooms removed from the system. You can restore any entry.",
                    UIHelper.TEXT2, 13, false),
            UIHelper.sep());

        TableView<DeletedRoom> histTable = UIHelper.table();
        histTable.setPrefHeight(380);
        histTable.setItems(CinemaManager.getInstance().getDeletedRooms());

        TableColumn<DeletedRoom, String> nCol = UIHelper.col("Room Name", 160);
        nCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<DeletedRoom, String> tCol = UIHelper.col("Type", 100);
        tCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<DeletedRoom, Integer> rCol = UIHelper.col("Rows", 70);
        rCol.setCellValueFactory(new PropertyValueFactory<>("rows"));

        TableColumn<DeletedRoom, Integer> cCol = UIHelper.col("Cols", 70);
        cCol.setCellValueFactory(new PropertyValueFactory<>("cols"));

        TableColumn<DeletedRoom, Integer> capCol = UIHelper.col("Capacity", 80);
        capCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        TableColumn<DeletedRoom, String> byCol = UIHelper.col("Deleted By", 130);
        byCol.setCellValueFactory(new PropertyValueFactory<>("deletedBy"));

        TableColumn<DeletedRoom, String> atCol = UIHelper.col("Deleted At", 170);
        atCol.setCellValueFactory(new PropertyValueFactory<>("deletedAtFormatted"));

        TableColumn<DeletedRoom, Void> actionCol = new TableColumn<>("Action");
        actionCol.setMinWidth(100);
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button restoreBtn = UIHelper.outlineBtn("Restore", UIHelper.GREEN);
            {
                restoreBtn.setPrefHeight(28);
                restoreBtn.setOnAction(e -> {
                    DeletedRoom dr = getTableView().getItems().get(getIndex());
                    CinemaManager.getInstance().restoreRoom(dr);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : restoreBtn);
            }
        });

        histTable.getColumns().addAll(nCol, tCol, rCol, cCol, capCol, byCol, atCol, actionCol);
        histTable.setPlaceholder(UIHelper.lbl("No rooms have been deleted yet.", UIHelper.TEXT2, 13, false));

        content.getChildren().add(histTable);
        VBox.setVgrow(histTable, Priority.ALWAYS);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setStyle("-fx-background-color:" + UIHelper.BG + ";");
        dialog.showAndWait();
    }

    private boolean validate(TextField nameF, ComboBox<String> typeCb,
                              TextField rowsF, TextField colsF, Label err) {
        if (nameF.getText().trim().isEmpty()) { UIHelper.showError(err, "Room name is required."); return false; }
        if (typeCb.getValue() == null) { UIHelper.showError(err, "Type is required."); return false; }
        try { int r = Integer.parseInt(rowsF.getText().trim()); if (r < 1) throw new NumberFormatException(); }
        catch (NumberFormatException ex) { UIHelper.showError(err, "Rows must be a positive integer."); return false; }
        try { int c = Integer.parseInt(colsF.getText().trim()); if (c < 1) throw new NumberFormatException(); }
        catch (NumberFormatException ex) { UIHelper.showError(err, "Columns must be a positive integer."); return false; }
        UIHelper.clearError(err);
        return true;
    }
}