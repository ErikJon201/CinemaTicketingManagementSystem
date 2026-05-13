package views;

import controllers.UserController;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import models.*;

public class ManageUsersScreen {
    private Stage stage;
    private Admin admin;
    private UserController controller = new UserController();

    public ManageUsersScreen(Stage stage, Admin admin) {
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
        manageUsersBtn.setStyle(navActiveStyle()); // highlight current screen

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

        // Greeting
        Label greeting = new Label("User Management");
        greeting.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 14px;");
        Label subtitle = new Label("User Management");
        subtitle.setStyle("-fx-text-fill: #eaeaea; -fx-font-size: 32px; -fx-font-weight: bold;");

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #2b3250;");
        VBox.setMargin(sep, new Insets(4, 0, 12, 0));

        // ── Table ─────────────────────────────────────────────────────
        Label tableLabel = new Label("ALL USERS");
        tableLabel.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 10px; -fx-font-weight: bold;");

        TableView<User> table = new TableView<>();
        table.setItems(UserManager.getInstance().getUsers());
        table.setStyle(
            "-fx-background-color: #161b2e;" +
            "-fx-border-color: #2b3250;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;"
        );
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(200);

        TableColumn<User, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        idCol.setStyle("-fx-text-fill: #eaeaea;");
        idCol.setCellFactory(col -> new TableCell<>() {
        @Override
        protected void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty || item == null ? null : String.valueOf(item));
            setStyle("-fx-text-fill: #eaeaea;");
            }
        });

        TableColumn<User, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<User, String> userCol = new TableColumn<>("Username");
        userCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        table.getColumns().addAll(idCol, nameCol, userCol, roleCol);
        styleTableColumns(table);

        // ── Form ──────────────────────────────────────────────────────
        Label formLabel = new Label("ADD / EDIT USER");
        formLabel.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 10px; -fx-font-weight: bold;");

        TextField nameField = styledField("Full Name");
        TextField userField = styledField("Username");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setStyle(fieldStyle());

        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("Admin", "Cashier");
        roleCombo.setPromptText("Role");
        roleCombo.setStyle(fieldStyle());
        roleCombo.setButtonCell(new ListCell<>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(item == null ? "Role" : item);
            setStyle("-fx-text-fill: " + (item == null ? "#7a849a" : "#eaeaea") + ";");
            }
        });
        roleCombo.setPrefWidth(120);

        HBox form = new HBox(12, nameField, userField, passField, roleCombo);
        form.setAlignment(Pos.CENTER_LEFT);

        // Status label
        Label statusLabel = new Label("");
        statusLabel.setStyle("-fx-font-size: 12px;");

        // ── Buttons ───────────────────────────────────────────────────
        Button addBtn    = actionButton("＋  Add User", "#c9a84c", "#0b0f1a");
        Button editBtn   = actionButton("✎  Edit Selected", "#2b3250", "#eaeaea");
        Button deleteBtn = actionButton("✕  Delete User", "#2b3250", "#e05555");

        // Add
        addBtn.setOnAction(e -> {
            controller.handleAddUser(
                nameField.getText().trim(),
                userField.getText().trim(),
                passField.getText().trim(),
                roleCombo.getValue(),
                statusLabel
            );
            if (statusLabel.getText().startsWith("✓")) {
                nameField.clear();
                userField.clear();
                passField.clear();
                roleCombo.setValue(null);
            }
        });

        // Edit — populate fields
        editBtn.setOnAction(e -> {
            User selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                statusLabel.setStyle("-fx-text-fill: #e05555; -fx-font-size: 12px;");
                statusLabel.setText("⚠  Select a user to edit.");
                return;
            }
            nameField.setText(selected.getFullName());
            userField.setText(selected.getUsername());
            passField.setText(selected.getPassword());
            roleCombo.setValue(selected.getRole());

            addBtn.setText("💾  Save Changes");
            addBtn.setOnAction(ev -> {
                controller.handleEditUser(
                    selected,
                    nameField.getText().trim(),
                    userField.getText().trim(),
                    passField.getText().trim(),
                    statusLabel,
                    () -> {
                        table.refresh();
                        nameField.clear();
                        userField.clear();
                        passField.clear();
                        roleCombo.setValue(null);
                        addBtn.setText("＋  Add User");
                        resetAddButton(addBtn, nameField, userField,
                                       passField, roleCombo, statusLabel);
                    }
                );
            });
        });

        // Delete
        deleteBtn.setOnAction(e -> {
            User selected = table.getSelectionModel().getSelectedItem();
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                selected != null ? "Delete user \"" + selected.getUsername() + "\"?" : "",
                ButtonType.YES, ButtonType.NO);
            confirm.setHeaderText("Confirm Delete");
            confirm.getDialogPane().setStyle("-fx-background-color: #161b2e;");
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.YES) {
                    controller.handleDeleteUser(selected, admin, statusLabel, () -> {});
                }
            });
        });

        HBox buttons = new HBox(12, addBtn, editBtn, deleteBtn);

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

    private void resetAddButton(Button addBtn, TextField nameField, TextField userField,
                                  PasswordField passField, ComboBox<String> roleCombo,
                                  Label statusLabel) {
        addBtn.setOnAction(e -> {
            controller.handleAddUser(
                nameField.getText().trim(),
                userField.getText().trim(),
                passField.getText().trim(),
                roleCombo.getValue(),
                statusLabel
            );
            if (statusLabel.getText().startsWith("✓")) {
                nameField.clear();
                userField.clear();
                passField.clear();
                roleCombo.setValue(null);
            }
        });
    }

    private void styleTableColumns(TableView<User> table) {
        table.setStyle(
            "-fx-background-color: #161b2e;" +
            "-fx-border-color: #2b3250;" +
            "-fx-text-fill: #eaeaea;"
        );
    }

    private TextField styledField(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        f.setStyle(fieldStyle());
        return f;
    }

    private String fieldStyle() {
        return  "-fx-background-color: #161b2e;" +
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
        return  "-fx-background-color: transparent;" +
                "-fx-text-fill: #7a849a;" +
                "-fx-font-size: 13px;" +
                "-fx-cursor: hand;";
    }

    private String navHoverStyle() {
        return  "-fx-background-color: #1e2540;" +
                "-fx-text-fill: #c9a84c;" +
                "-fx-font-size: 13px;" +
                "-fx-cursor: hand;";
    }

    private String navActiveStyle() {
        return  "-fx-background-color: #1e2540;" +
                "-fx-text-fill: #c9a84c;" +
                "-fx-font-size: 13px;" +
                "-fx-cursor: hand;" +
                "-fx-border-color: transparent transparent transparent #c9a84c;" +
                "-fx-border-width: 3;";
    }

    private String logoutNavStyle() {
        return  "-fx-background-color: transparent;" +
                "-fx-text-fill: #3d4560;" +
                "-fx-font-size: 13px;" +
                "-fx-cursor: hand;";
    }

    private String logoutNavHoverStyle() {
        return  "-fx-background-color: transparent;" +
                "-fx-text-fill: #e05555;" +
                "-fx-font-size: 13px;" +
                "-fx-cursor: hand;";
    }
}