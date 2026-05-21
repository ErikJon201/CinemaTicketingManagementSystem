package views;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.*;

public class LoginScreen {
    private Stage stage;

    public LoginScreen(Stage stage) {
        this.stage = stage;
    }

    public Scene getScene() {

        HBox root = new HBox(0);
        root.setStyle("-fx-background-color:" + UIHelper.BG + ";");

        // ── Left brand panel ───────────────────────────────────────────────────
        VBox left = new VBox();
        left.setPrefWidth(440);
        left.setMinWidth(440);
        left.setStyle("-fx-background-color:#0d0d1e;");

        Region topAccent = new Region();
        topAccent.setPrefHeight(4);
        topAccent.setStyle("-fx-background-color:" + UIHelper.RED + ";");

        VBox leftContent = new VBox(20);
        leftContent.setPadding(new Insets(60, 48, 60, 48));
        leftContent.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(leftContent, Priority.ALWAYS);

        Label logo = UIHelper.lbl("CINEMAX", UIHelper.RED, 40, true);
        Label logoSub = UIHelper.lbl("Cinema Management System", UIHelper.TEXT, 16, false);

        Region gap = new Region();
        gap.setPrefHeight(36);

        Label desc = UIHelper.lbl(
            "A complete cinema ticketing platform for\n" +
            "managing movies, schedules, and sales.", UIHelper.TEXT2, 13, false);
        desc.setWrapText(true);

        Region gap2 = new Region();
        gap2.setPrefHeight(28);

        VBox features = new VBox(14);
        features.getChildren().addAll(
            bullet("Manage movies and showtimes"),
            bullet("Real-time seat availability"),
            bullet("Daily and total sales reports"),
            bullet("Role-based staff access")
        );

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label version = UIHelper.lbl("CINEMAX v2.0   2026", UIHelper.MUTED, 11, false);

        leftContent.getChildren().addAll(logo, logoSub, gap, desc, gap2, features, spacer, version);
        left.getChildren().addAll(topAccent, leftContent);

        // ── Right form panel ───────────────────────────────────────────────────
        VBox right = new VBox();
        HBox.setHgrow(right, Priority.ALWAYS);
        right.setAlignment(Pos.CENTER);
        right.setStyle("-fx-background-color:" + UIHelper.BG + ";");
        right.setPadding(new Insets(0, 80, 0, 80));

        VBox form = new VBox(0);
        form.setMaxWidth(360);
        form.setAlignment(Pos.TOP_LEFT);

        Label heading = UIHelper.lbl("Welcome back", UIHelper.TEXT, 28, true);
        Label sub = UIHelper.lbl("Sign in to access the portal", UIHelper.TEXT2, 13, false);
        VBox.setMargin(sub, new Insets(6, 0, 28, 0));

        // Role toggle
        Label roleHdr = UIHelper.lbl("ROLE", UIHelper.TEXT2, 11, true);
        VBox.setMargin(roleHdr, new Insets(0, 0, 6, 0));

        ToggleGroup tg = new ToggleGroup();
        ToggleButton tbAdmin   = roleBtn("Admin",   tg);
        ToggleButton tbCashier = roleBtn("Cashier", tg);
        tbAdmin.setSelected(true);

        HBox roleRow = new HBox(0, tbAdmin, tbCashier);
        roleRow.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(tbAdmin,   Priority.ALWAYS);
        HBox.setHgrow(tbCashier, Priority.ALWAYS);
        roleRow.setStyle("-fx-background-color:" + UIHelper.INPUT +
                ";-fx-background-radius:8;-fx-border-color:" + UIHelper.BORDER +
                ";-fx-border-radius:8;-fx-border-width:1;");
        VBox.setMargin(roleRow, new Insets(0, 0, 20, 0));

        // Username
        Label userHdr = UIHelper.lbl("USERNAME", UIHelper.TEXT2, 11, true);
        VBox.setMargin(userHdr, new Insets(0, 0, 6, 0));
        TextField userF = UIHelper.tf("Enter your username");
        userF.setPrefHeight(42);
        VBox.setMargin(userF, new Insets(0, 0, 16, 0));

        // Password
        Label passHdr = UIHelper.lbl("PASSWORD", UIHelper.TEXT2, 11, true);
        VBox.setMargin(passHdr, new Insets(0, 0, 6, 0));
        PasswordField passF = UIHelper.pf("Enter your password");
        passF.setPrefHeight(42);
        VBox.setMargin(passF, new Insets(0, 0, 12, 0));

        // Error
        Label errorLbl = UIHelper.errorLbl();
        VBox.setMargin(errorLbl, new Insets(0, 0, 8, 0));

        // Login button
        Button loginBtn = UIHelper.btn("Sign In", UIHelper.RED, "#ffffff");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setPrefHeight(44);
        VBox.setMargin(loginBtn, new Insets(4, 0, 20, 0));

        Label footer = UIHelper.lbl("Authorized personnel only", UIHelper.MUTED, 11, false);
        footer.setMaxWidth(Double.MAX_VALUE);
        footer.setAlignment(Pos.CENTER);

        loginBtn.setOnAction(e -> attemptLogin(
                userF.getText().trim(), passF.getText(),
                tbAdmin.isSelected() ? "Admin" : "Cashier",
                errorLbl));
        passF.setOnAction(e -> loginBtn.fire());

        form.getChildren().addAll(
            heading, sub,
            roleHdr, roleRow,
            userHdr, userF,
            passHdr, passF,
            errorLbl, loginBtn, footer);

        right.getChildren().add(form);
        root.getChildren().addAll(left, right);
        return new Scene(root, 900, 580);
    }

    private void attemptLogin(String username, String password, String role, Label err) {
        if (username.isEmpty() || password.isEmpty()) {
            UIHelper.showError(err, "Please enter your username and password.");
            return;
        }
        User u = UserManager.getInstance().login(username, password, role);
        if (u != null) {
            UIHelper.clearError(err);
            if (u instanceof Admin a)
                stage.setScene(new AdminDashboard(stage, a).getScene());
            else
                stage.setScene(new CashierDashboard(stage, (Cashier) u).getScene());
        } else {
            UIHelper.showError(err, "Invalid credentials. Please try again.");
        }
    }

    private HBox bullet(String text) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getChildren().addAll(
            UIHelper.lbl("—", UIHelper.RED, 13, true),
            UIHelper.lbl(text, UIHelper.TEXT2, 13, false));
        return row;
    }

    private ToggleButton roleBtn(String label, ToggleGroup tg) {
        ToggleButton tb = new ToggleButton(label);
        tb.setToggleGroup(tg);
        tb.setPadding(new Insets(9, 0, 9, 0));
        String sel = "-fx-background-color:" + UIHelper.RED +
                ";-fx-text-fill:#fff;-fx-font-size:13;-fx-font-weight:bold;" +
                "-fx-cursor:hand;-fx-background-radius:7;-fx-border-color:transparent;";
        String unsel = "-fx-background-color:transparent;-fx-text-fill:" + UIHelper.TEXT2 +
                ";-fx-font-size:13;-fx-cursor:hand;-fx-background-radius:7;" +
                "-fx-border-color:transparent;";
        tb.setStyle(unsel);
        tb.selectedProperty().addListener((obs, was, is) -> tb.setStyle(is ? sel : unsel));
        return tb;
    }
}