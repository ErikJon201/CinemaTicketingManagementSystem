package views;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import models.*;

public class LoginScreen {
    private Stage stage;

    public LoginScreen(Stage stage) { this.stage = stage; }

    public Scene getScene() {

        // ── Background ──────────────────────────────────────────────
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #0b0f1a;");

        // Decorative accent bar on the left
        Rectangle accentBar = new Rectangle(5, 460);
        accentBar.setFill(Color.web("#c9a84c"));
        StackPane.setAlignment(accentBar, Pos.CENTER_LEFT);

        // ── Card ─────────────────────────────────────────────────────
        VBox card = new VBox(18);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(45, 50, 45, 50));
        card.setMaxWidth(420);
        card.setStyle(
            "-fx-background-color: #161b2e;" +
            "-fx-background-radius: 6;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 30, 0, 0, 8);"
        );

        // ── Header ───────────────────────────────────────────────────
        Label brand = new Label("🎬  CINETICKET");
        brand.setStyle(
            "-fx-text-fill: #c9a84c;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-letter-spacing: 3;"
        );

        Label heading = new Label("Staff Login");
        heading.setStyle(
            "-fx-text-fill: #eaeaea;" +
            "-fx-font-size: 28px;" +
            "-fx-font-weight: bold;"
        );

        Label subheading = new Label("Sign in to access the ticketing portal");
        subheading.setStyle(
            "-fx-text-fill: #7a849a;" +
            "-fx-font-size: 13px;"
        );

        // ── Divider ──────────────────────────────────────────────────
        Separator divider = new Separator();
        divider.setStyle("-fx-background-color: #2b3250;");
        VBox.setMargin(divider, new Insets(4, 0, 4, 0));

        // ── Role Toggle ──────────────────────────────────────────────
        Label roleLabel = styledLabel("ROLE");

        ToggleGroup group = new ToggleGroup();
        ToggleButton tbAdmin   = roleToggle("Admin",   group);
        ToggleButton tbCashier = roleToggle("Cashier", group);
        tbCashier.setSelected(true);

        HBox roleRow = new HBox(0, tbAdmin, tbCashier);
        roleRow.setStyle(
            "-fx-background-color: #0f1422;" +
            "-fx-background-radius: 4;" +
            "-fx-border-color: #2b3250;" +
            "-fx-border-radius: 4;" +
            "-fx-border-width: 1;"
        );

        // ── Username ─────────────────────────────────────────────────
        Label userLabel = styledLabel("USERNAME");
        TextField userF = styledField("Enter your username");

        // ── Password ─────────────────────────────────────────────────
        Label passLabel = styledLabel("PASSWORD");
        PasswordField passF = new PasswordField();
        passF.setPromptText("Enter your password");
        passF.setStyle(fieldStyle());

        // ── Login Button ─────────────────────────────────────────────
        Button loginBtn = new Button("SIGN IN");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setStyle(
            "-fx-background-color: #c9a84c;" +
            "-fx-text-fill: #0b0f1a;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 13 0;" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;" +
            "-fx-letter-spacing: 1;"
        );
        VBox.setMargin(loginBtn, new Insets(8, 0, 0, 0));

        // Hover effect
        loginBtn.setOnMouseEntered(e ->
            loginBtn.setStyle(
                "-fx-background-color: #e0bb6a;" +
                "-fx-text-fill: #0b0f1a;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 13 0;" +
                "-fx-background-radius: 4;" +
                "-fx-cursor: hand;" +
                "-fx-letter-spacing: 1;"
            )
        );
        loginBtn.setOnMouseExited(e ->
            loginBtn.setStyle(
                "-fx-background-color: #c9a84c;" +
                "-fx-text-fill: #0b0f1a;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 13 0;" +
                "-fx-background-radius: 4;" +
                "-fx-cursor: hand;" +
                "-fx-letter-spacing: 1;"
            )
        );

        // ── Footer note ──────────────────────────────────────────────
        Label footer = new Label("Authorized personnel only");
        footer.setStyle("-fx-text-fill: #3d4560; -fx-font-size: 11px;");
        footer.setMaxWidth(Double.MAX_VALUE);
        footer.setAlignment(Pos.CENTER);
        VBox.setMargin(footer, new Insets(4, 0, 0, 0));

        // ── Action ───────────────────────────────────────────────────
        loginBtn.setOnAction(e -> {
            String role = tbAdmin.isSelected() ? "Admin" : "Cashier";
            User u = UserManager.getInstance().login(userF.getText(), passF.getText(), role);
            if (u != null) {
                if (u instanceof Admin)
                    stage.setScene(new AdminDashboard(stage, (Admin) u).getScene());
                else
                    stage.setScene(new CashierDashboard(stage, (Cashier) u).getScene());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid username or password. Please try again.");
                alert.show();
            }
        });

        card.getChildren().addAll(
            brand, heading, subheading, divider,
            roleLabel, roleRow,
            userLabel, userF,
            passLabel, passF,
            loginBtn, footer
        );

        root.getChildren().addAll(accentBar, card);
        return new Scene(root, 480, 560);
    }

    // ── Helpers ───────────────────────────────────────────────────────

    private Label styledLabel(String text) {
        Label lbl = new Label(text);
        lbl.setStyle(
            "-fx-text-fill: #7a849a;" +
            "-fx-font-size: 10px;" +
            "-fx-font-weight: bold;"
        );
        VBox.setMargin(lbl, new Insets(4, 0, 3, 0));
        return lbl;
    }

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

    private ToggleButton roleToggle(String label, ToggleGroup group) {
        ToggleButton tb = new ToggleButton(label);
        tb.setToggleGroup(group);
        tb.setPrefWidth(160);
        tb.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #7a849a;" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 9 0;" +
            "-fx-cursor: hand;" +
            "-fx-background-radius: 3;"
        );
        // Highlight when selected
        tb.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                tb.setStyle(
                    "-fx-background-color: #c9a84c;" +
                    "-fx-text-fill: #0b0f1a;" +
                    "-fx-font-size: 13px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 9 0;" +
                    "-fx-cursor: hand;" +
                    "-fx-background-radius: 3;"
                );
            } else {
                tb.setStyle(
                    "-fx-background-color: transparent;" +
                    "-fx-text-fill: #7a849a;" +
                    "-fx-font-size: 13px;" +
                    "-fx-padding: 9 0;" +
                    "-fx-cursor: hand;" +
                    "-fx-background-radius: 3;"
                );
            }
        });
        return tb;
    }
}