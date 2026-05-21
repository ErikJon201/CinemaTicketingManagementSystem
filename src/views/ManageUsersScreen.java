package views;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
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
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:" + UIHelper.BG + ";");

        String[] nav = {"Dashboard", "Movies", "Showtimes", "Theater Rooms", "Staff", "Sales Report"};
        Runnable[] acts = {
            () -> stage.setScene(new AdminDashboard(stage, admin).getScene()),
            () -> stage.setScene(new ManageMoviesScreen(stage, admin).getScene()),
            () -> stage.setScene(new ManageShowtimesScreen(stage, admin).getScene()),
            () -> stage.setScene(new ManageRoomsScreen(stage, admin).getScene()),
            null,
            () -> stage.setScene(new SalesReportScreen(stage, admin).getScene())
        };
        root.setLeft(UIHelper.sidebar(admin, "Staff", nav, acts,
                () -> stage.setScene(new LoginScreen(stage).getScene())));

        HBox main = new HBox(20);
        main.setPadding(new Insets(36, 36, 36, 36));
        main.setStyle("-fx-background-color:" + UIHelper.BG + ";");

        // ── Left: table ────────────────────────────────────────────────────────
        VBox leftPane = new VBox(16);
        HBox.setHgrow(leftPane, Priority.ALWAYS);

        HBox headerRow = new HBox(12);
        headerRow.setAlignment(Pos.CENTER_LEFT);
        VBox headerText = UIHelper.pageHeader("Staff Management",
                "Manage administrator and cashier accounts.");
        HBox.setHgrow(headerText, Priority.ALWAYS);
        Button historyBtn = UIHelper.outlineBtn("Deleted History", UIHelper.GOLD);
        historyBtn.setOnAction(e -> showHistoryDialog());
        headerRow.getChildren().addAll(headerText, historyBtn);
        leftPane.getChildren().add(headerRow);

        TableView<User> table = UIHelper.table();
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<User, Integer> idCol = UIHelper.col("#", 44);
        idCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

        TableColumn<User, String> nameCol = UIHelper.col("Full Name", 180);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<User, String> userCol = UIHelper.col("Username", 130);
        userCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> roleCol = UIHelper.col("Role", 90);
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        table.getColumns().addAll(idCol, nameCol, userCol, roleCol);
        table.setItems(UserManager.getInstance().getUsers());
        leftPane.getChildren().add(table);

        // ── Right: form ────────────────────────────────────────────────────────
        VBox formCard = UIHelper.card();
        formCard.setPrefWidth(300);
        formCard.setMaxWidth(300);

        Label formTitle = UIHelper.lbl("Account Details", UIHelper.TEXT, 15, true);

        TextField nameF  = UIHelper.tf("Full name");
        TextField userF  = UIHelper.tf("Username");
        PasswordField passF = UIHelper.pf("Password");
        PasswordField confirmF = UIHelper.pf("Confirm password");
        ComboBox<String> roleCb = UIHelper.cb();
        roleCb.getItems().addAll("Admin", "Cashier");
        roleCb.setPromptText("Select role");

        Label errorLbl = UIHelper.errorLbl();

        Button addBtn    = UIHelper.primaryBtn("Add Account");
        Button updateBtn = UIHelper.outlineBtn("Update Selected", UIHelper.GOLD);
        Button deleteBtn = UIHelper.outlineBtn("Delete Selected", "#e74c3c");
        Button clearBtn  = UIHelper.ghostBtn("Clear Form");

        for (Button b : new Button[]{addBtn, updateBtn, deleteBtn, clearBtn})
            b.setMaxWidth(Double.MAX_VALUE);

        formCard.getChildren().addAll(
            formTitle, UIHelper.sep(),
            UIHelper.formRow("Full Name",        nameF),
            UIHelper.formRow("Username",         userF),
            UIHelper.formRow("Password",         passF),
            UIHelper.formRow("Confirm Password", confirmF),
            UIHelper.formRow("Role",             roleCb),
            errorLbl, UIHelper.sep(),
            new VBox(8, addBtn, updateBtn, deleteBtn, clearBtn));

        // ── Logic ──────────────────────────────────────────────────────────────

        Runnable clear = () -> {
            nameF.clear(); userF.clear(); passF.clear(); confirmF.clear();
            roleCb.getSelectionModel().clearSelection();
            table.getSelectionModel().clearSelection();
            UIHelper.clearError(errorLbl);
        };

        clearBtn.setOnAction(e -> clear.run());

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                nameF.setText(sel.getFullName());
                userF.setText(sel.getUsername());
                passF.clear(); confirmF.clear();
                roleCb.setValue(sel.getRole());
                UIHelper.clearError(errorLbl);
            }
        });

        addBtn.setOnAction(e -> {
            if (!validateForm(nameF, userF, passF, confirmF, roleCb, null, errorLbl)) return;
            UserManager um = UserManager.getInstance();
            int id = um.getNextId();
            User newUser = roleCb.getValue().equals("Admin")
                ? new Admin(id, userF.getText().trim(), passF.getText(), nameF.getText().trim())
                : new Cashier(id, userF.getText().trim(), passF.getText(), nameF.getText().trim());
            um.addUser(newUser);
            clear.run();
        });

        updateBtn.setOnAction(e -> {
            User sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { UIHelper.showError(errorLbl, "Select an account to update."); return; }
            if (nameF.getText().trim().isEmpty()) { UIHelper.showError(errorLbl, "Full name is required."); return; }
            if (userF.getText().trim().isEmpty()) { UIHelper.showError(errorLbl, "Username is required."); return; }
            if (UserManager.getInstance().usernameExists(userF.getText().trim(), sel)) {
                UIHelper.showError(errorLbl, "Username already taken."); return; }
            sel.setFullName(nameF.getText().trim());
            sel.setUsername(userF.getText().trim());
            if (!passF.getText().isEmpty()) {
                if (!passF.getText().equals(confirmF.getText())) {
                    UIHelper.showError(errorLbl, "Passwords do not match."); return; }
                sel.setPassword(passF.getText());
            }
            table.refresh();
            clear.run();
        });

        deleteBtn.setOnAction(e -> {
            User sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { UIHelper.showError(errorLbl, "Select an account to delete."); return; }
            if (sel == admin) { UIHelper.showError(errorLbl, "Cannot delete the current admin."); return; }
            Alert a = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete account \"" + sel.getUsername() + "\"?",
                ButtonType.YES, ButtonType.NO);
            a.setHeaderText(null);
            a.showAndWait().ifPresent(bt -> {
                if (bt == ButtonType.YES) { UserManager.getInstance().deleteUser(sel, admin.getFullName()); clear.run(); }
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
        dialog.setTitle("Deleted Staff History");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        VBox content = new VBox(16);
        content.setPadding(new Insets(20, 24, 8, 24));
        content.setPrefWidth(800);
        content.setStyle("-fx-background-color:" + UIHelper.BG + ";");

        content.getChildren().addAll(
            UIHelper.lbl("Deleted Staff History", UIHelper.TEXT, 18, true),
            UIHelper.lbl("Staff accounts removed from the system. You can restore any entry.",
                    UIHelper.TEXT2, 13, false),
            UIHelper.sep());

        TableView<DeletedUser> histTable = UIHelper.table();
        histTable.setPrefHeight(380);
        histTable.setItems(UserManager.getInstance().getDeletedUsers());

        TableColumn<DeletedUser, String> nameCol = UIHelper.col("Full Name", 180);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<DeletedUser, String> userCol = UIHelper.col("Username", 130);
        userCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<DeletedUser, String> roleCol = UIHelper.col("Role", 90);
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<DeletedUser, String> byCol = UIHelper.col("Deleted By", 150);
        byCol.setCellValueFactory(new PropertyValueFactory<>("deletedBy"));

        TableColumn<DeletedUser, String> atCol = UIHelper.col("Deleted At", 180);
        atCol.setCellValueFactory(new PropertyValueFactory<>("deletedAtFormatted"));

        TableColumn<DeletedUser, Void> actionCol = new TableColumn<>("Action");
        actionCol.setMinWidth(100);
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button restoreBtn = UIHelper.outlineBtn("Restore", UIHelper.GREEN);
            {
                restoreBtn.setPrefHeight(28);
                restoreBtn.setOnAction(e -> {
                    DeletedUser du = getTableView().getItems().get(getIndex());
                    UserManager.getInstance().restoreUser(du);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : restoreBtn);
            }
        });

        histTable.getColumns().addAll(nameCol, userCol, roleCol, byCol, atCol, actionCol);
        histTable.setPlaceholder(UIHelper.lbl("No staff accounts have been deleted yet.", UIHelper.TEXT2, 13, false));

        content.getChildren().add(histTable);
        VBox.setVgrow(histTable, Priority.ALWAYS);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setStyle("-fx-background-color:" + UIHelper.BG + ";");
        dialog.showAndWait();
    }

    private boolean validateForm(TextField nameF, TextField userF,
                                  PasswordField passF, PasswordField confirmF,
                                  ComboBox<String> roleCb, User existing, Label err) {
        if (nameF.getText().trim().isEmpty()) { UIHelper.showError(err, "Full name is required."); return false; }
        if (userF.getText().trim().isEmpty()) { UIHelper.showError(err, "Username is required."); return false; }
        if (passF.getText().isEmpty()) { UIHelper.showError(err, "Password is required."); return false; }
        if (!passF.getText().equals(confirmF.getText())) { UIHelper.showError(err, "Passwords do not match."); return false; }
        if (roleCb.getValue() == null) { UIHelper.showError(err, "Role is required."); return false; }
        if (UserManager.getInstance().usernameExists(userF.getText().trim(), existing)) {
            UIHelper.showError(err, "Username already taken."); return false; }
        UIHelper.clearError(err);
        return true;
    }
}