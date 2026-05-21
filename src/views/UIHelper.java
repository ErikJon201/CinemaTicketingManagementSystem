package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.User;

public class UIHelper {

    // ── Palette ────────────────────────────────────────────────────────────────
    public static final String BG      = "#0a0a12";
    public static final String SIDEBAR = "#0d0d1a";
    public static final String CARD    = "#141428";
    public static final String CARD2   = "#1c1c32";
    public static final String BORDER  = "#26263e";
    public static final String RED     = "#e50914";
    public static final String GOLD    = "#f5c518";
    public static final String GREEN   = "#27ae60";
    public static final String BLUE    = "#2980b9";
    public static final String PURPLE  = "#8e44ad";
    public static final String ORANGE  = "#e67e22";
    public static final String TEXT    = "#f0f0ff";
    public static final String TEXT2   = "#8888aa";
    public static final String MUTED   = "#44445a";
    public static final String INPUT   = "#16162a";

    // seat colours
    public static final String SEAT_FREE = "#1a4731";
    public static final String SEAT_FREE_B = "#27ae60";
    public static final String SEAT_TAKEN = "#4a1010";
    public static final String SEAT_TAKEN_B = "#c0392b";
    public static final String SEAT_SEL = "#4a3a00";
    public static final String SEAT_SEL_B = "#f5c518";

    public static final double SW = 250.0;

    // ── Buttons ────────────────────────────────────────────────────────────────

    public static Button btn(String text, String bg, String fg) {
        Button b = new Button(text);
        String base = "-fx-background-color:" + bg + ";-fx-text-fill:" + fg +
                ";-fx-font-size:13;-fx-font-weight:bold;-fx-background-radius:8;" +
                "-fx-cursor:hand;-fx-padding:9 22;";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(base + "-fx-opacity:.82;"));
        b.setOnMouseExited(e -> b.setStyle(base));
        return b;
    }

    public static Button primaryBtn(String text) {
        return btn(text, RED, "#ffffff");
    }

    public static Button successBtn(String text) {
        return btn(text, GREEN, "#ffffff");
    }

    public static Button dangerBtn(String text) {
        return btn(text, "#c0392b", "#ffffff");
    }

    public static Button outlineBtn(String text, String color) {
        Button b = new Button(text);
        String base = "-fx-background-color:transparent;-fx-text-fill:" + color +
                ";-fx-border-color:" + color + ";-fx-border-width:1.5;-fx-border-radius:8;" +
                "-fx-background-radius:8;-fx-font-size:13;-fx-cursor:hand;-fx-padding:8 20;";
        String hover = "-fx-background-color:" + color + ";-fx-text-fill:#0a0a12;" +
                "-fx-border-color:" + color + ";-fx-border-width:1.5;-fx-border-radius:8;" +
                "-fx-background-radius:8;-fx-font-size:13;-fx-font-weight:bold;-fx-cursor:hand;-fx-padding:8 20;";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e -> b.setStyle(base));
        return b;
    }

    public static Button ghostBtn(String text) {
        Button b = new Button(text);
        String base = "-fx-background-color:transparent;-fx-text-fill:" + TEXT2 +
                ";-fx-font-size:13;-fx-cursor:hand;-fx-padding:9 20;";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(base.replace(TEXT2, TEXT)));
        b.setOnMouseExited(e -> b.setStyle(base));
        return b;
    }

    // ── Inputs ─────────────────────────────────────────────────────────────────

    private static final String IN_BASE =
            "-fx-background-color:" + INPUT + ";-fx-text-fill:" + TEXT +
            ";-fx-border-color:" + BORDER + ";-fx-border-radius:8;-fx-background-radius:8;" +
            "-fx-padding:9 12;-fx-font-size:13;-fx-prompt-text-fill:" + MUTED + ";";

    public static TextField tf(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        f.setStyle(IN_BASE);
        f.setMaxWidth(Double.MAX_VALUE);
        return f;
    }

    public static PasswordField pf(String prompt) {
        PasswordField f = new PasswordField();
        f.setPromptText(prompt);
        f.setStyle(IN_BASE);
        f.setMaxWidth(Double.MAX_VALUE);
        return f;
    }

    public static TextArea ta(String prompt, int rows) {
        TextArea a = new TextArea();
        a.setPromptText(prompt);
        a.setPrefRowCount(rows);
        a.setWrapText(true);
        a.setStyle(IN_BASE + "-fx-control-inner-background:" + INPUT + ";");
        a.setMaxWidth(Double.MAX_VALUE);
        return a;
    }

    public static <T> ComboBox<T> cb() {
        ComboBox<T> c = new ComboBox<>();
        c.setStyle(IN_BASE);
        c.setMaxWidth(Double.MAX_VALUE);
        return c;
    }

    // ── Labels ─────────────────────────────────────────────────────────────────

    public static Label lbl(String text, String color, int size, boolean bold) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill:" + color + ";-fx-font-size:" + size + ";" +
                (bold ? "-fx-font-weight:bold;" : ""));
        return l;
    }

    public static Label title(String text) { return lbl(text, TEXT, 26, true); }
    public static Label subtitle(String text) { return lbl(text, TEXT2, 14, false); }
    public static Label sectionLbl(String text) { return lbl(text.toUpperCase(), MUTED, 10, true); }

    public static Label badge(String text, String bg) {
        Label l = lbl(text, "#ffffff", 11, true);
        l.setStyle(l.getStyle() + "-fx-background-color:" + bg +
                ";-fx-background-radius:6;-fx-padding:2 8;");
        return l;
    }

    public static Label errorLbl() {
        Label l = lbl("", "#e74c3c", 12, false);
        l.setVisible(false);
        l.setManaged(false);
        return l;
    }

    public static void showError(Label l, String msg) {
        l.setText("  " + msg);
        l.setVisible(true);
        l.setManaged(true);
    }

    public static void clearError(Label l) {
        l.setVisible(false);
        l.setManaged(false);
    }

    // ── Containers ─────────────────────────────────────────────────────────────

    public static VBox card() {
        VBox v = new VBox(12);
        v.setStyle("-fx-background-color:" + CARD + ";-fx-background-radius:12;" +
                "-fx-border-color:" + BORDER + ";-fx-border-radius:12;-fx-border-width:1;");
        v.setPadding(new Insets(20));
        return v;
    }

    public static VBox formRow(String labelText, javafx.scene.Node input) {
        VBox row = new VBox(6);
        row.getChildren().addAll(lbl(labelText, TEXT2, 12, true), input);
        return row;
    }

    public static Separator sep() {
        Separator s = new Separator();
        s.setStyle("-fx-background-color:" + BORDER + ";");
        return s;
    }

    // ── Stat card ──────────────────────────────────────────────────────────────

    public static HBox statCard(String value, String label, String accent) {
        HBox box = new HBox(0);
        box.setStyle("-fx-background-color:" + CARD + ";-fx-background-radius:12;" +
                "-fx-border-color:" + BORDER + ";-fx-border-radius:12;-fx-border-width:1;");
        box.setMinWidth(180);

        Region bar = new Region();
        bar.setPrefWidth(4);
        bar.setPrefHeight(80);
        bar.setStyle("-fx-background-color:" + accent +
                ";-fx-background-radius:12 0 0 12;");

        VBox content = new VBox(5);
        content.setPadding(new Insets(16, 18, 16, 14));
        HBox.setHgrow(content, Priority.ALWAYS);
        content.getChildren().addAll(
                lbl(value, accent, 26, true),
                lbl(label, TEXT2, 13, false));

        box.getChildren().addAll(bar, content);
        return box;
    }

    // ── Table ──────────────────────────────────────────────────────────────────

    public static <T> TableView<T> table() {
        TableView<T> t = new TableView<>();
        t.setStyle(
                "-fx-background-color:" + CARD + ";-fx-border-color:" + BORDER +
                ";-fx-border-radius:10;-fx-background-radius:10;" +
                "-fx-table-cell-border-color:transparent;");
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        t.setFixedCellSize(42);
        t.setPlaceholder(lbl("No data found.", TEXT2, 13, false));
        return t;
    }

    public static <S, T> TableColumn<S, T> col(String header, int minW) {
        TableColumn<S, T> c = new TableColumn<>(header);
        c.setMinWidth(minW);
        c.setStyle("-fx-alignment:CENTER-LEFT;");
        return c;
    }

    // ── Sidebar ────────────────────────────────────────────────────────────────

    public static VBox sidebar(User user, String activeLabel,
                               String[] navLabels, Runnable[] navActions,
                               Runnable logoutAction) {
        VBox sb = new VBox();
        sb.setPrefWidth(SW);
        sb.setMinWidth(SW);
        sb.setMaxWidth(SW);
        sb.setStyle("-fx-background-color:" + SIDEBAR + ";");

        // Brand
        VBox brand = new VBox(3);
        brand.setPadding(new Insets(26, 22, 22, 22));
        brand.setStyle("-fx-border-color:transparent transparent " + BORDER
                + " transparent;-fx-border-width:1;");
        Label logo = lbl("CINEMAX", RED, 20, true);
        Label tag = lbl("Cinema Management", TEXT2, 11, false);
        brand.getChildren().addAll(logo, tag);

        // Nav
        VBox nav = new VBox(2);
        nav.setPadding(new Insets(14, 10, 14, 10));
        VBox.setVgrow(nav, Priority.ALWAYS);

        for (int i = 0; i < navLabels.length; i++) {
            final Runnable action = (i < navActions.length) ? navActions[i] : null;
            String label = navLabels[i];
            boolean active = label.equals(activeLabel);

            Label item = new Label(label);
            item.setMaxWidth(Double.MAX_VALUE);
            item.setPrefWidth(Double.MAX_VALUE);

            String aStyle = "-fx-background-color:" + RED + "2a;-fx-text-fill:" + TEXT +
                    ";-fx-background-radius:8;-fx-padding:10 16;-fx-font-size:13;" +
                    "-fx-font-weight:bold;-fx-cursor:hand;";
            String nStyle = "-fx-background-color:transparent;-fx-text-fill:" + TEXT2 +
                    ";-fx-background-radius:8;-fx-padding:10 16;-fx-font-size:13;-fx-cursor:hand;";
            String hStyle = "-fx-background-color:" + CARD2 + ";-fx-text-fill:" + TEXT +
                    ";-fx-background-radius:8;-fx-padding:10 16;-fx-font-size:13;-fx-cursor:hand;";

            item.setStyle(active ? aStyle : nStyle);
            if (!active && action != null) {
                item.setOnMouseEntered(e -> item.setStyle(hStyle));
                item.setOnMouseExited(e -> item.setStyle(nStyle));
                item.setOnMouseClicked(e -> action.run());
            }
            nav.getChildren().add(item);
        }

        // User / logout
        VBox userBox = new VBox(8);
        userBox.setPadding(new Insets(14, 14, 20, 14));
        userBox.setStyle("-fx-border-color:" + BORDER
                + " transparent transparent transparent;-fx-border-width:1;");

        userBox.getChildren().addAll(
                lbl(user.getFullName(), TEXT, 13, true),
                lbl(user.getRole(), TEXT2, 11, false));

        Button logoutBtn = new Button("Sign Out");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        String lb = "-fx-background-color:transparent;-fx-text-fill:#e74c3c;" +
                "-fx-border-color:#e74c3c33;-fx-border-width:1;-fx-border-radius:8;" +
                "-fx-background-radius:8;-fx-font-size:12;-fx-cursor:hand;-fx-padding:7 14;";
        String lbH = "-fx-background-color:#e74c3c1a;-fx-text-fill:#e74c3c;" +
                "-fx-border-color:#e74c3c;-fx-border-width:1;-fx-border-radius:8;" +
                "-fx-background-radius:8;-fx-font-size:12;-fx-cursor:hand;-fx-padding:7 14;";
        logoutBtn.setStyle(lb);
        logoutBtn.setOnMouseEntered(e -> logoutBtn.setStyle(lbH));
        logoutBtn.setOnMouseExited(e -> logoutBtn.setStyle(lb));
        logoutBtn.setOnAction(e -> logoutAction.run());
        userBox.getChildren().add(logoutBtn);

        sb.getChildren().addAll(brand, nav, userBox);
        return sb;
    }

    // ── Page header ────────────────────────────────────────────────────────────

    public static VBox pageHeader(String t, String sub) {
        VBox h = new VBox(6);
        h.setPadding(new Insets(0, 0, 20, 0));
        h.getChildren().addAll(title(t), subtitle(sub));
        return h;
    }
}