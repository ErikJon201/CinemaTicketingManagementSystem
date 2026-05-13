package views;

import controllers.MovieController;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import models.*;

public class ManageMoviesScreen {

    private Stage stage;
    private Admin admin;
    private TableView<Movie> table;
    private MovieController controller = new MovieController();

    public ManageMoviesScreen(Stage stage, Admin admin) {
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
        manageMoviesBtn.setStyle(navActiveStyle());

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

        Label greeting = new Label("Movie Management");
        greeting.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 14px;");
        Label subtitle = new Label("Add, edit, or remove movies");
        subtitle.setStyle("-fx-text-fill: #eaeaea; -fx-font-size: 32px; -fx-font-weight: bold;");

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #2b3250;");
        VBox.setMargin(sep, new Insets(4, 0, 12, 0));

        // ── Table ─────────────────────────────────────────────────────
        Label tableLabel = new Label("ALL MOVIES");
        tableLabel.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 10px; -fx-font-weight: bold;");

        table = new TableView<>();
        table.setItems(CinemaManager.getInstance().getMovies());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(200);

        TableColumn<Movie, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setCellFactory(col -> styledCell());

        TableColumn<Movie, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreCol.setCellFactory(col -> styledCell());

        TableColumn<Movie, Integer> durCol = new TableColumn<>("Duration (min)");
        durCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        durCol.setCellFactory(col -> styledCell());

        table.getColumns().addAll(titleCol, genreCol, durCol);

        // ── Form ──────────────────────────────────────────────────────
        Label formLabel = new Label("ADD / EDIT MOVIE");
        formLabel.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 10px; -fx-font-weight: bold;");

        TextField txtTitle    = styledField("Movie Title");
        TextField txtGenre    = styledField("Genre");
        TextField txtDuration = styledField("Duration (min)");

        HBox form = new HBox(12, txtTitle, txtGenre, txtDuration);
        form.setAlignment(Pos.CENTER_LEFT);

        Label statusLabel = new Label("");

        // ── Buttons ───────────────────────────────────────────────────
        Button addBtn    = actionButton("＋  Add Movie",      "#c9a84c", "#0b0f1a");
        Button updateBtn = actionButton("✎  Update Selected", "#2b3250", "#eaeaea");
        Button deleteBtn = actionButton("✕  Delete Selected", "#2b3250", "#e05555");

        // Auto-fill on row select
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtTitle.setText(newVal.getTitle());
                txtGenre.setText(newVal.getGenre());
                txtDuration.setText(String.valueOf(newVal.getDuration()));
            }
        });

        // Add
        addBtn.setOnAction(e -> controller.handleAddMovie(
            txtTitle.getText().trim(),
            txtGenre.getText().trim(),
            txtDuration.getText().trim(),
            statusLabel,
            () -> { txtTitle.clear(); txtGenre.clear(); txtDuration.clear(); }
        ));

        // Update
        updateBtn.setOnAction(e -> controller.handleUpdateMovie(
            table.getSelectionModel().getSelectedItem(),
            txtTitle.getText().trim(),
            txtGenre.getText().trim(),
            txtDuration.getText().trim(),
            statusLabel,
            () -> { table.refresh(); txtTitle.clear(); txtGenre.clear(); txtDuration.clear(); }
        ));

        // Delete
        deleteBtn.setOnAction(e -> {
            Movie selected = table.getSelectionModel().getSelectedItem();
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                selected != null ? "Delete \"" + selected.getTitle() + "\"?" : "",
                ButtonType.YES, ButtonType.NO);
            confirm.setHeaderText("Confirm Delete");
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.YES)
                    controller.handleDeleteMovie(selected, statusLabel,
                        () -> { txtTitle.clear(); txtGenre.clear(); txtDuration.clear(); });
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
            ".column-header .label { -fx-text-fill: %237a849a; -fx-font-weight: bold; }"
        );
        return scene;
    }

    // ── Helpers ───────────────────────────────────────────────────────

    private <T> TableCell<Movie, T> styledCell() {
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