package views;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import models.*;

public class ManageUsersScreen {
    private Stage stage;
    private Admin admin;

    public ManageUsersScreen(Stage stage, Admin admin) {
        this.stage = stage;
        this.admin = admin;
    }

    public Scene getScene() {

        // ── Root ─────────────────────────────────────────────────────
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0b0f1a;");

        // ── Sidebar ───────────────────────────────────────────────────
        BorderPane sidebar = new BorderPane();
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #161b2e;");

        Rectangle accentBar = new Rectangle(4, 700);
        accentBar.setFill(Color.web("#c9a84c"));

        VBox brandBox = new VBox(4);
        brandBox.setPadding(new Insets(30, 20, 30, 20));
        brandBox.setStyle("-fx-border-color: transparent transparent #2b3250 transparent; -fx-border-width: 1;");
        Label brand = new Label("🎬  CINETICKET");
        brand.setStyle("-fx-text-fill: #c9a84c; -fx-font-size: 13px; -fx-font-weight: bold;");
        Label portalTag = new Label("MANAGE USERS");
        portalTag.setStyle("-fx-text-fill: #3d4560; -fx-font-size: 10px; -fx-font-weight: bold;");
        brandBox.getChildren().addAll(brand, portalTag);

        VBox sideTop = new VBox(0);
        sideTop.getChildren().add(
            new HBox(accentBar, brandBox) {{ setAlignment(Pos.CENTER_LEFT); }}
        );

        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setMaxWidth(Double.MAX_VALUE);
        backBtn.setPadding(new Insets(13, 0, 13, 0));
        backBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #3d4560; -fx-font-size: 12px; -fx-cursor: hand;"
        );
        backBtn.setOnMouseEntered(e -> backBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #7a849a; -fx-font-size: 12px; -fx-cursor: hand;"
        ));
        backBtn.setOnMouseExited(e -> backBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #3d4560; -fx-font-size: 12px; -fx-cursor: hand;"
        ));

        VBox sideBottom = new VBox(6);
        sideBottom.setPadding(new Insets(0, 16, 24, 16));
        sideBottom.getChildren().add(backBtn);

        sidebar.setTop(sideTop);
        sidebar.setBottom(sideBottom);

        // ── Main Content ──────────────────────────────────────────────
        VBox content = new VBox(20);
        content.setPadding(new Insets(40, 50, 40, 50));

        Label heading = new Label("Manage Users");
        heading.setStyle(
            "-fx-text-fill: #eaeaea;" +
            "-fx-font-size: 26px;" +
            "-fx-font-weight: bold;"
        );
        Label sub = new Label("Add or remove cashier and admin accounts");
        sub.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 13px;");

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #2b3250;");

        // ── Table ─────────────────────────────────────────────────────
        TableView<User> table = new TableView<>();
        table.setItems(UserManager.getInstance().getUsers());
        table.setStyle(
            "-fx-background-color: #0f1422;" +
            "-fx-border-color: #2b3250;" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-border-width: 1;"
        );
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<User, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<User, String> userCol = new TableColumn<>("Username");
        userCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        table.getColumns().addAll(nameCol, userCol, roleCol);

        table.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            row.setStyle("-fx-background-color: #0f1422; -fx-text-fill: #eaeaea;");
            row.setOnMouseEntered(e -> {
                if (!row.isEmpty()) row.setStyle("-fx-background-color: #1e2540; -fx-text-fill: #eaeaea;");
            });
            row.setOnMouseExited(e -> {
                if (!row.isEmpty()) row.setStyle("-fx-background-color: #0f1422; -fx-text-fill: #eaeaea;");
            });
            return row;
        });

        // ── Form Fields ───────────────────────────────────────────────
        Label formLabel = new Label("USER DETAILS");
        formLabel.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 10px; -fx-font-weight: bold;");

        TextField nameField  = styledField("Full Name");
        TextField userField  = styledField("Username");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setStyle(fieldStyle());

        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("Admin", "Cashier");
        roleCombo.setPromptText("Select Role");
        roleCombo.setMaxWidth(Double.MAX_VALUE);
        roleCombo.setStyle(
            "-fx-background-color: #0f1422;" +
            "-fx-text-fill: #eaeaea;" +
            "-fx-prompt-text-fill: #3d4560;" +
            "-fx-border-color: #2b3250;" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-border-width: 1;" +
            "-fx-font-size: 13px;"
        );
        roleCombo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
                setStyle("-fx-background-color: #0f1422; -fx-text-fill: #eaeaea;");
            }
        });
        roleCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Select Role" : item);
                setStyle("-fx-background-color: #0f1422; -fx-text-fill: #eaeaea;");
            }
        });

        HBox form = new HBox(12, nameField, userField, passField, roleCombo);
        HBox.setHgrow(nameField,  Priority.ALWAYS);
        HBox.setHgrow(userField,  Priority.ALWAYS);
        HBox.setHgrow(passField,  Priority.ALWAYS);
        HBox.setHgrow(roleCombo,  Priority.ALWAYS);

        // ── Action Buttons ────────────────────────────────────────────
        Button addBtn    = primaryButton("＋  Add User");
        Button deleteBtn = dangerButton("✕  Delete Selected");

        HBox actions = new HBox(12, addBtn, deleteBtn);

        // ── Logic (unchanged) ─────────────────────────────────────────
        addBtn.setOnAction(e -> {
            String role = roleCombo.getValue();
            if (role == null || nameField.getText().isEmpty() ||
                userField.getText().isEmpty() || passField.getText().isEmpty()) {
                showError("Please fill all fields!");
                return;
            }
            int newId = UserManager.getInstance().getUsers().size() + 1;
            if (role.equals("Admin"))
                UserManager.getInstance().getUsers().add(
                    new Admin(newId, userField.getText(), passField.getText(), nameField.getText())
                );
            else
                UserManager.getInstance().getUsers().add(
                    new Cashier(newId, userField.getText(), passField.getText(), nameField.getText())
                );
            nameField.clear(); userField.clear(); passField.clear();
            roleCombo.setValue(null);
        });

        deleteBtn.setOnAction(e -> {
            User selected = table.getSelectionModel().getSelectedItem();
            if (selected instanceof Cashier) {
                UserManager.getInstance().getUsers().remove(selected);
            } else {
                showError("You can only delete Cashier accounts!");
            }
        });

        backBtn.setOnAction(e -> stage.setScene(new AdminDashboard(stage, admin).getScene()));

        content.getChildren().addAll(heading, sub, sep, table, formLabel, form, actions);

        root.setLeft(sidebar);
        root.setCenter(content);
        return new Scene(root, 900, 580);
    }

    // ── Helpers ───────────────────────────────────────────────────────

    private TextField styledField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle(fieldStyle());
        return tf;
    }

    private String fieldStyle() {
        return  "-fx-background-color: #0f1422;" +
                "-fx-text-fill: #eaeaea;" +
                "-fx-prompt-text-fill: #3d4560;" +
                "-fx-border-color: #2b3250;" +
                "-fx-border-radius: 4;" +
                "-fx-background-radius: 4;" +
                "-fx-border-width: 1;" +
                "-fx-padding: 10 12;" +
                "-fx-font-size: 13px;";
    }

    private Button primaryButton(String text) {
        Button btn = new Button(text);
        btn.setPadding(new Insets(10, 20, 10, 20));
        btn.setStyle(
            "-fx-background-color: #c9a84c; -fx-text-fill: #0b0f1a;" +
            "-fx-font-size: 12px; -fx-font-weight: bold;" +
            "-fx-background-radius: 4; -fx-cursor: hand;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: #e0bb6a; -fx-text-fill: #0b0f1a;" +
            "-fx-font-size: 12px; -fx-font-weight: bold;" +
            "-fx-background-radius: 4; -fx-cursor: hand;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: #c9a84c; -fx-text-fill: #0b0f1a;" +
            "-fx-font-size: 12px; -fx-font-weight: bold;" +
            "-fx-background-radius: 4; -fx-cursor: hand;"
        ));
        return btn;
    }

    private Button dangerButton(String text) {
        Button btn = new Button(text);
        btn.setPadding(new Insets(10, 20, 10, 20));
        btn.setStyle(
            "-fx-background-color: #7a2020; -fx-text-fill: #eaeaea;" +
            "-fx-font-size: 12px; -fx-background-radius: 4; -fx-cursor: hand;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: #a02828; -fx-text-fill: #eaeaea;" +
            "-fx-font-size: 12px; -fx-background-radius: 4; -fx-cursor: hand;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: #7a2020; -fx-text-fill: #eaeaea;" +
            "-fx-font-size: 12px; -fx-background-radius: 4; -fx-cursor: hand;"
        ));
        return btn;
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}