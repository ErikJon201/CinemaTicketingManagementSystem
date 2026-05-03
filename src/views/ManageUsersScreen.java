package views;

import javafx.geometry.Insets;
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
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label title = new Label("Manage Users");
        TableView<User> table = new TableView<>();
        table.setItems(UserManager.getInstance().getUsers());

        TableColumn<User, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<User, String> userCol = new TableColumn<>("Username");
        userCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        table.getColumns().addAll(nameCol, userCol, roleCol);

        // Fields
        TextField nameField = new TextField(); nameField.setPromptText("Full Name");
        TextField userField = new TextField(); userField.setPromptText("Username");
        PasswordField passField = new PasswordField(); passField.setPromptText("Password");
        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("Admin", "Cashier");

        HBox form = new HBox(10, nameField, userField, passField, roleCombo);

        Button addBtn = new Button("Add User");
        addBtn.setOnAction(e -> {
            String role = roleCombo.getValue();
            int newId = UserManager.getInstance().getUsers().size() + 1;
            if(role.equals("Admin"))
                UserManager.getInstance().getUsers().add(new Admin(newId, userField.getText(), passField.getText(), nameField.getText()));
            else
                UserManager.getInstance().getUsers().add(new Cashier(newId, userField.getText(), passField.getText(), nameField.getText()));
        });

        Button deleteBtn = new Button("Delete Selected Cashier");
        deleteBtn.setOnAction(e -> {
            User selected = table.getSelectionModel().getSelectedItem();
            if (selected != null && selected instanceof Cashier) {
                UserManager.getInstance().getUsers().remove(selected);
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR, "You can only delete Cashiers!");
                a.show();
            }
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> stage.setScene(new AdminDashboard(stage, admin).getScene()));

        root.getChildren().addAll(title, table, form, new HBox(10, addBtn, deleteBtn, backBtn));
        return new Scene(root, 800, 600);
    }
}