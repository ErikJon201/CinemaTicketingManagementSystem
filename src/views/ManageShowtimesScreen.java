package views;

import controllers.ShowtimeController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import models.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

public class ManageShowtimesScreen {

    private Stage stage;
    private Admin admin;
    private TableView<Showtime> table;
    private ShowtimeController controller = new ShowtimeController();

    public ManageShowtimesScreen(Stage stage, Admin admin) {
        this.stage = stage;
        this.admin = admin;
    }

    public Scene getScene() {

        // ── Root ─────────────────────────────────────────────────────
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0b0f1a;");

        // ── Sidebar ──────────────────────────────────────────────────
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(240);
        sidebar.setStyle("-fx-background-color: #161b2e;");

        Rectangle accentBar = new Rectangle(4, 600);
        accentBar.setFill(Color.web("#c9a84c"));

        VBox brandBox = new VBox(4);
        brandBox.setPadding(new Insets(30, 20, 30, 20));
        brandBox.setStyle("-fx-border-color: transparent transparent #2b3250 transparent; -fx-border-width: 1;");

        Label brand = new Label("🎬  CINETICKET");
        brand.setStyle("-fx-text-fill: #c9a84c; -fx-font-size: 13px; -fx-font-weight: bold;");
        Label roleTag = new Label("ADMIN PORTAL");
        roleTag.setStyle("-fx-text-fill: #3d4560; -fx-font-size: 10px; -fx-font-weight: bold;");
        brandBox.getChildren().addAll(brand, roleTag);

        Button manageMoviesBtn    = navButton("🎥   Manage Movies");
        Button manageShowtimesBtn = navButton("🕒   Manage Showtimes");
        Button manageUsersBtn     = navButton("👥   Manage Users");
        Button salesReportBtn     = navButton("📊   Sales Report");
        manageShowtimesBtn.setStyle(navActiveStyle());

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button logoutBtn = navButton("⎋   Logout");
        logoutBtn.setStyle(logoutNavStyle());
        logoutBtn.setOnMouseEntered(e -> logoutBtn.setStyle(logoutNavHoverStyle()));
        logoutBtn.setOnMouseExited(e  -> logoutBtn.setStyle(logoutNavStyle()));
        VBox.setMargin(logoutBtn, new Insets(0, 0, 20, 0));

        sidebar.getChildren().addAll(
            new HBox(accentBar, brandBox) {{ setAlignment(Pos.CENTER_LEFT); }},
            manageMoviesBtn, manageShowtimesBtn, manageUsersBtn, salesReportBtn,
            spacer, logoutBtn
        );

        // ── Main Content ─────────────────────────────────────────────
        VBox content = new VBox(24);
        content.setPadding(new Insets(50, 50, 50, 50));
        content.setStyle("-fx-background-color: #0b0f1a;");

        Label greeting = new Label("Showtime Management");
        greeting.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 14px;");
        Label subtitle = new Label("Schedule and manage showtimes");
        subtitle.setStyle("-fx-text-fill: #eaeaea; -fx-font-size: 32px; -fx-font-weight: bold;");

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #2b3250;");
        VBox.setMargin(sep, new Insets(4, 0, 12, 0));

        // ── Table ─────────────────────────────────────────────────────
        Label tableLabel = new Label("ALL SHOWTIMES");
        tableLabel.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 10px; -fx-font-weight: bold;");

        table = new TableView<>();
        table.setItems(CinemaManager.getInstance().getShowtimes());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(200);

        TableColumn<Showtime, String> movieCol = new TableColumn<>("Movie");
        movieCol.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));
        movieCol.setCellFactory(col -> styledCell());

        TableColumn<Showtime, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setCellFactory(col -> styledCell());

        TableColumn<Showtime, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        timeCol.setCellFactory(col -> styledCell());

        TableColumn<Showtime, String> roomCol = new TableColumn<>("Room");
        roomCol.setCellValueFactory(new PropertyValueFactory<>("roomName"));
        roomCol.setCellFactory(col -> styledCell());

        TableColumn<Showtime, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setCellFactory(col -> styledCell());
        priceCol.setMaxWidth(100);
        priceCol.setMinWidth(100);

        table.getColumns().addAll(movieCol, dateCol, timeCol, roomCol, priceCol);

        // ── Form ──────────────────────────────────────────────────────
        Label formLabel = new Label("ADD / EDIT SHOWTIME");
        formLabel.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 10px; -fx-font-weight: bold;");

        // MOVIES: deduplicate by title
        java.util.LinkedHashMap<String, Movie> uniqueMap = new java.util.LinkedHashMap<>();
        for (Movie m : CinemaManager.getInstance().getMovies())
            uniqueMap.putIfAbsent(m.getTitle(), m);
        ComboBox<Movie> movieCombo = new ComboBox<>(
            FXCollections.observableArrayList(uniqueMap.values()));
        movieCombo.setPromptText("Select Movie");
        movieCombo.setPrefWidth(200);
        movieCombo.setStyle(fieldStyle());
        movieCombo.setConverter(new javafx.util.StringConverter<Movie>() {
            @Override public String toString(Movie m)   { return m == null ? "" : m.getTitle(); }
            @Override public Movie fromString(String s) { return null; }
        });
        movieCombo.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Movie item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null || empty ? "" : item.getTitle());
            }
        });
        // setButtonCell with ONLY -fx-text-fill, no background — background comes from fieldStyle on the combo itself
        movieCombo.setButtonCell(new ListCell<Movie>() {
            @Override protected void updateItem(Movie item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "Select Movie" : item.getTitle());
                setStyle("-fx-text-fill: #eaeaea; -fx-padding: 0;");
            }
        });

        // ROOM
        ComboBox<String> roomCombo = new ComboBox<>();
        for (int i = 1; i <= 10; i++) roomCombo.getItems().add("Cinema " + i);
        roomCombo.setPromptText("Select Room");
        roomCombo.setPrefWidth(150);
        roomCombo.setStyle(fieldStyle());
        roomCombo.setButtonCell(new ListCell<String>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "Select Room" : item);
                setStyle("-fx-text-fill: #eaeaea; -fx-padding: 0;");
            }
        });

        // DATE PICKER
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select Date");
        datePicker.setPrefWidth(170);
        datePicker.setStyle(fieldStyle());
        // Apply after JavaFX's CSS pass via Platform.runLater
        Runnable applyEditorStyle = () -> Platform.runLater(() ->
            datePicker.getEditor().setStyle(
                "-fx-background-color: #161b2e;" +
                "-fx-text-fill: #eaeaea;" +
                "-fx-prompt-text-fill: #3d4560;" +
                "-fx-border-color: transparent;" +
                "-fx-padding: 0 4 0 4;"
            )
        );
        applyEditorStyle.run();
        datePicker.valueProperty().addListener((o, ov, nv)              -> applyEditorStyle.run());
        datePicker.focusedProperty().addListener((o, ov, nv)            -> applyEditorStyle.run());
        datePicker.getEditor().focusedProperty().addListener((o, ov, nv)-> applyEditorStyle.run());
        datePicker.getEditor().textProperty().addListener((o, ov, nv)   -> applyEditorStyle.run());
        // Inject dark CSS into the popup's own scene when it opens
        String popupCss = "data:text/css," +
            ".date-picker-popup { -fx-background-color: %23161b2e; -fx-border-color: %232b3250; }" +
            ".date-picker-popup .month-year-pane { -fx-background-color: %231e2540; }" +
            ".date-picker-popup .month-year-pane .label { -fx-text-fill: %23eaeaea; }" +
            ".date-picker-popup .spinner .button { -fx-background-color: %231e2540; }" +
            ".date-picker-popup .spinner .left-button .left-arrow { -fx-background-color: %23eaeaea; }" +
            ".date-picker-popup .spinner .right-button .right-arrow { -fx-background-color: %23eaeaea; }" +
            ".date-picker-popup .day-name-cell { -fx-text-fill: %237a849a; -fx-background-color: %23161b2e; }" +
            ".date-picker-popup .day-cell { -fx-text-fill: %23eaeaea; -fx-background-color: %23161b2e; }" +
            ".date-picker-popup .day-cell .text { -fx-fill: %23eaeaea; }" +
            ".date-picker-popup .day-cell:hover { -fx-background-color: %231e2540; }" +
            ".date-picker-popup .day-cell:empty { -fx-background-color: %23161b2e; }" +
            ".date-picker-popup .selected { -fx-background-color: %23c9a84c; -fx-text-fill: %230b0f1a; }" +
            ".date-picker-popup .selected .text { -fx-fill: %230b0f1a; }" +
            ".date-picker-popup .today { -fx-border-color: %23c9a84c; }" +
            ".date-picker-popup .calendar-grid { -fx-background-color: %23161b2e; }" +
            ".date-picker-popup .week-number-cell { -fx-text-fill: %237a849a; -fx-background-color: %23161b2e; }";
        datePicker.showingProperty().addListener((obs, wasShowing, isShowing) -> {
            if (isShowing) {
                Platform.runLater(() -> {
                    // The popup skin is in the scene of the datepicker's skin
                    javafx.scene.Node popupContent = datePicker.lookup(".date-picker-popup");
                    if (popupContent != null && popupContent.getScene() != null) {
                        popupContent.getScene().getStylesheets().add(popupCss);
                    }
                });
            }
        });

        // TIME combos — setButtonCell with text-fill only, no background override
        ComboBox<String> hourCombo = new ComboBox<>();
        for (int i = 1; i <= 12; i++) hourCombo.getItems().add(String.format("%02d", i));
        hourCombo.setPromptText("HH");
        hourCombo.setPrefWidth(80);
        hourCombo.setStyle(fieldStyle());
        hourCombo.setButtonCell(new ListCell<String>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "HH" : item);
                setStyle("-fx-text-fill: #eaeaea; -fx-padding: 0;");
            }
        });

        ComboBox<String> minuteCombo = new ComboBox<>();
        minuteCombo.getItems().addAll("00", "15", "30", "45");
        minuteCombo.setPromptText("MM");
        minuteCombo.setPrefWidth(80);
        minuteCombo.setStyle(fieldStyle());
        minuteCombo.setButtonCell(new ListCell<String>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "MM" : item);
                setStyle("-fx-text-fill: #eaeaea; -fx-padding: 0;");
            }
        });

        ComboBox<String> amPmCombo = new ComboBox<>();
        amPmCombo.getItems().addAll("AM", "PM");
        amPmCombo.setPromptText("AM/PM");
        amPmCombo.setPrefWidth(95);
        amPmCombo.setStyle(fieldStyle());
        amPmCombo.setButtonCell(new ListCell<String>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "AM/PM" : item);
                setStyle("-fx-text-fill: #eaeaea; -fx-padding: 0;");
            }
        });

        TextField txtPrice = styledField("Price (PHP)");
        txtPrice.setPrefWidth(140);

        Label timeLabel = new Label("Time:");
        timeLabel.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 12px;");
        HBox timeBox = new HBox(8, timeLabel, hourCombo, minuteCombo, amPmCombo);
        timeBox.setAlignment(Pos.CENTER_LEFT);

        HBox formRow1 = new HBox(12, movieCombo, roomCombo, datePicker);
        HBox formRow2 = new HBox(12, timeBox, txtPrice);
        formRow1.setAlignment(Pos.CENTER_LEFT);
        formRow2.setAlignment(Pos.CENTER_LEFT);
        VBox form = new VBox(10, formRow1, formRow2);

        Label statusLabel = new Label("");
        statusLabel.setStyle("-fx-text-fill: #c9a84c; -fx-font-size: 12px;");

        // ── Date/Time helpers ─────────────────────────────────────────
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("MMM dd, yyyy");

        Supplier<String> getDate = () ->
            datePicker.getValue() == null ? ""
                : datePicker.getValue().format(dateFmt);

        Supplier<String> getTime = () -> {
            if (hourCombo.getValue() == null || minuteCombo.getValue() == null
                    || amPmCombo.getValue() == null) return "";
            return hourCombo.getValue() + ":" + minuteCombo.getValue()
                 + " " + amPmCombo.getValue();
        };

        // ── Clear form ────────────────────────────────────────────────
        Runnable clearForm = () -> {
            movieCombo.setValue(null);
            roomCombo.setValue(null);
            datePicker.setValue(null);
            hourCombo.setValue(null);
            minuteCombo.setValue(null);
            amPmCombo.setValue(null);
            txtPrice.clear();
        };

        // ── Auto-fill on row select ───────────────────────────────────
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) return;

            // Match by title against the deduplicated combo list
            String title = newVal.getMovieTitle();
            movieCombo.getItems().stream()
                .filter(m -> m.getTitle().equals(title))
                .findFirst()
                .ifPresent(movieCombo::setValue);

            roomCombo.setValue(newVal.getRoomName());

            try {
                datePicker.setValue(LocalDate.parse(newVal.getDate(), dateFmt));
            } catch (Exception ex) {
                datePicker.setValue(null);
            }

            // Parse "HH:MM AM/PM"  e.g. "10:00 AM"
            try {
                String raw = newVal.getTime();
                if (raw != null && raw.contains(":") && raw.contains(" ")) {
                    String[] colon = raw.split(":");          // ["10", "00 AM"]
                    String hh = colon[0].trim();              // "10"
                    String[] space = colon[1].trim().split(" "); // ["00", "AM"]
                    hourCombo.setValue(hh);
                    minuteCombo.setValue(space[0]);
                    amPmCombo.setValue(space[1]);
                }
            } catch (Exception ex) {
                hourCombo.setValue(null);
                minuteCombo.setValue(null);
                amPmCombo.setValue(null);
            }

            txtPrice.setText(String.valueOf(newVal.getPrice()));
        });

        // ── Buttons ───────────────────────────────────────────────────
        Button addBtn    = actionButton("＋  Add Showtime",   "#c9a84c", "#0b0f1a");
        Button updateBtn = actionButton("✎  Update Selected", "#2b3250", "#eaeaea");
        Button deleteBtn = actionButton("✕  Delete Selected", "#2b3250", "#e05555");

        addBtn.setOnAction(e -> controller.handleAddShowtime(
            movieCombo.getValue(),
            roomCombo.getValue() == null ? "" : roomCombo.getValue(),
            getDate.get(), getTime.get(),
            txtPrice.getText().trim(),
            statusLabel, clearForm
        ));

        updateBtn.setOnAction(e -> controller.handleUpdateShowtime(
            table.getSelectionModel().getSelectedItem(),
            roomCombo.getValue() == null ? "" : roomCombo.getValue(),
            getDate.get(), getTime.get(),
            txtPrice.getText().trim(),
            statusLabel,
            () -> { table.refresh(); clearForm.run(); }
        ));

        deleteBtn.setOnAction(e -> {
            Showtime selected = table.getSelectionModel().getSelectedItem();
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                selected != null ? "Delete this showtime?" : "",
                ButtonType.YES, ButtonType.NO);
            confirm.setHeaderText("Confirm Delete");
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.YES)
                    controller.handleDeleteShowtime(selected, statusLabel, clearForm);
            });
        });

        HBox buttons = new HBox(12, addBtn, updateBtn, deleteBtn);

        // ── Footer ────────────────────────────────────────────────────
        Region contentSpacer = new Region();
        VBox.setVgrow(contentSpacer, Priority.ALWAYS);
        Label footer = new Label("Authorized personnel only  •  " + admin.getRole());
        footer.setStyle("-fx-text-fill: #2b3250; -fx-font-size: 11px;");

        content.getChildren().addAll(
            greeting, subtitle, sep,
            tableLabel, table,
            formLabel, form,
            buttons, statusLabel,
            contentSpacer, footer
        );

        // ── Wire Sidebar ──────────────────────────────────────────────
        manageMoviesBtn.setOnAction(e ->
            stage.setScene(new ManageMoviesScreen(stage, admin).getScene()));
        manageShowtimesBtn.setOnAction(e ->
            stage.setScene(new ManageShowtimesScreen(stage, admin).getScene()));
        manageUsersBtn.setOnAction(e ->
            stage.setScene(new ManageUsersScreen(stage, admin).getScene()));
        salesReportBtn.setOnAction(e ->
            stage.setScene(new SalesReportScreen(stage, admin).getScene()));
        logoutBtn.setOnAction(e ->
            stage.setScene(new LoginScreen(stage).getScene()));

        root.setLeft(sidebar);
        root.setCenter(content);

        Scene scene = new Scene(root, 1000, 620);
        scene.getStylesheets().add("data:text/css," +
            ".table-view { -fx-background-color: %23161b2e; }" +
            ".table-view .column-header { -fx-background-color: %231e2540; }" +
            ".table-view .column-header-background { -fx-background-color: %231e2540; }" +
            ".table-view .table-row-cell { -fx-background-color: %23161b2e; -fx-border-color: transparent transparent %232b3250 transparent; }" +
            ".table-view .table-row-cell:selected { -fx-background-color: %231e2540; }" +
            ".table-view .table-row-cell:hover { -fx-background-color: %231a2035; }" +
            ".table-view .table-cell { -fx-text-fill: %23eaeaea; }" +
            ".column-header .label { -fx-text-fill: %237a849a; -fx-font-weight: bold; }" +
            // ComboBox: style the button cell (selected value display) and dropdown
            ".combo-box { -fx-background-color: %23161b2e; -fx-border-color: %232b3250; -fx-border-radius: 4; -fx-background-radius: 4; }" +
            ".combo-box .list-cell { -fx-text-fill: %23eaeaea; -fx-background-color: %23161b2e; -fx-padding: 8 12 8 12; }" +
            ".combo-box:hover { -fx-border-color: %23c9a84c; }" +
            ".combo-box .arrow-button { -fx-background-color: %23161b2e; }" +
            ".combo-box .arrow-button .arrow { -fx-background-color: %237a849a; }" +
            ".combo-box-popup .list-view { -fx-background-color: %23161b2e; -fx-border-color: %232b3250; }" +
            ".combo-box-popup .list-cell { -fx-text-fill: %23eaeaea; -fx-background-color: %23161b2e; }" +
            ".combo-box-popup .list-cell:hover { -fx-background-color: %231e2540; -fx-text-fill: %23c9a84c; }" +
            ".combo-box-popup .list-cell:filled:selected { -fx-background-color: %231e2540; -fx-text-fill: %23c9a84c; }" +
            // DatePicker
            ".date-picker { -fx-background-color: %23161b2e; }" +
            ".date-picker .text-field { -fx-background-color: %23161b2e; -fx-text-fill: %23eaeaea; -fx-prompt-text-fill: %233d4560; }" +
            ".date-picker > .text-field { -fx-text-fill: %23eaeaea; }" +
            ".date-picker .arrow-button { -fx-background-color: %23161b2e; }" +
            ".date-picker .arrow-button .arrow { -fx-background-color: %237a849a; }" +
            // DatePicker popup calendar
            ".date-picker-popup { -fx-background-color: %23161b2e; }" +
            ".date-picker-popup .month-year-pane { -fx-background-color: %231e2540; }" +
            ".date-picker-popup .month-year-pane .label { -fx-text-fill: %23eaeaea; }" +
            ".date-picker-popup .spinner .button { -fx-background-color: %231e2540; }" +
            ".date-picker-popup .spinner .left-button .left-arrow { -fx-background-color: %23eaeaea; }" +
            ".date-picker-popup .spinner .right-button .right-arrow { -fx-background-color: %23eaeaea; }" +
            ".date-picker-popup .day-name-cell { -fx-text-fill: %237a849a; }" +
            ".date-picker-popup .day-cell { -fx-text-fill: %23eaeaea; }" +
            ".date-picker-popup .day-cell:hover { -fx-background-color: %231e2540; }" +
            ".date-picker-popup .selected { -fx-background-color: %23c9a84c; -fx-text-fill: %230b0f1a; }" +
            ".date-picker-popup .today { -fx-border-color: %23c9a84c; }"
        );
        return scene;
    }

    // ── Helpers ───────────────────────────────────────────────────────

    private <T> TableCell<Showtime, T> styledCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
                setStyle("-fx-text-fill: #eaeaea; -fx-alignment: CENTER-LEFT;");
            }
        };
    }

    private TextField styledField(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        f.setStyle(fieldStyle());
        return f;
    }

    private String fieldStyle() {
        return "-fx-background-color: #161b2e;" +
               "-fx-text-fill: #eaeaea;" +
               "-fx-prompt-text-fill: #3d4560;" +
               "-fx-border-color: #2b3250;" +
               "-fx-border-radius: 4;" +
               "-fx-background-radius: 4;" +
               "-fx-padding: 8 12 8 12;";
    }

    private Button actionButton(String text, String bg, String fg) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-background-color: " + bg + ";" +
            "-fx-text-fill: " + fg + ";" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 4;" +
            "-fx-padding: 10 20 10 20;" +
            "-fx-cursor: hand;"
        );
        return btn;
    }

    private Button navButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(14, 24, 14, 24));
        btn.setStyle(navStyle());
        btn.setOnMouseEntered(e -> btn.setStyle(navHoverStyle()));
        btn.setOnMouseExited(e  -> btn.setStyle(navStyle()));
        return btn;
    }

    private String navStyle() {
        return "-fx-background-color: transparent;" +
               "-fx-text-fill: #7a849a;" +
               "-fx-font-size: 13px;" +
               "-fx-cursor: hand;";
    }

    private String navHoverStyle() {
        return "-fx-background-color: #1e2540;" +
               "-fx-text-fill: #c9a84c;" +
               "-fx-font-size: 13px;" +
               "-fx-cursor: hand;";
    }

    private String navActiveStyle() {
        return "-fx-background-color: #1e2540;" +
               "-fx-text-fill: #c9a84c;" +
               "-fx-font-size: 13px;" +
               "-fx-cursor: hand;" +
               "-fx-border-color: transparent transparent transparent #c9a84c;" +
               "-fx-border-width: 3;";
    }

    private String logoutNavStyle() {
        return "-fx-background-color: transparent;" +
               "-fx-text-fill: #3d4560;" +
               "-fx-font-size: 13px;" +
               "-fx-cursor: hand;";
    }

    private String logoutNavHoverStyle() {
        return "-fx-background-color: transparent;" +
               "-fx-text-fill: #e05555;" +
               "-fx-font-size: 13px;" +
               "-fx-cursor: hand;";
    }
}