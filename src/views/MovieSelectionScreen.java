package views;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import models.*;

public class MovieSelectionScreen {
    private Stage stage;
    private Cashier cashier;

    public MovieSelectionScreen(Stage stage, Cashier cashier) {
        this.stage = stage;
        this.cashier = cashier;
    }

    public Scene getScene() {

        // ── Root ─────────────────────────────────────────────────────
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0b0f1a;");

        // ── Sidebar (same as dashboard) ───────────────────────────────
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(200);
        sidebar.setStyle("-fx-background-color: #161b2e;");

        Rectangle accentBar = new Rectangle(4, 500);
        accentBar.setFill(Color.web("#c9a84c"));

        VBox brandBox = new VBox(4);
        brandBox.setPadding(new Insets(30, 20, 30, 20));
        brandBox.setStyle("-fx-border-color: transparent transparent #2b3250 transparent; -fx-border-width: 1;");
        Label brand = new Label("🎬  CINETICKET");
        brand.setStyle("-fx-text-fill: #c9a84c; -fx-font-size: 13px; -fx-font-weight: bold;");
        Label portalTag = new Label("CASHIER PORTAL");
        portalTag.setStyle("-fx-text-fill: #3d4560; -fx-font-size: 10px; -fx-font-weight: bold;");
        brandBox.getChildren().addAll(brand, portalTag);

        Button backBtn = navButton("← Back to Dashboard");
        backBtn.setOnAction(e -> stage.setScene(new CashierDashboard(stage, cashier).getScene()));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        Label footer = new Label("Select a movie\nto begin booking");
        footer.setStyle("-fx-text-fill: #3d4560; -fx-font-size: 11px;");
        footer.setPadding(new Insets(0, 20, 24, 24));

        sidebar.getChildren().addAll(
            new HBox(accentBar, brandBox) {{ setAlignment(Pos.CENTER_LEFT); }},
            backBtn, spacer, footer
        );

        // ── Main Content ─────────────────────────────────────────────
        VBox content = new VBox(24);
        content.setPadding(new Insets(40, 40, 40, 40));

        Label nowShowing = new Label("Now Showing");
        nowShowing.setStyle(
            "-fx-text-fill: #eaeaea;" +
            "-fx-font-size: 22px;" +
            "-fx-font-weight: bold;"
        );

        Label sub = new Label("Click a movie to select seats");
        sub.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 12px;");

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #2b3250;");

        // ── Movie Cards Row ───────────────────────────────────────────
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToHeight(true);

        GridPane cardsRow = new GridPane();
        cardsRow.setHgap(10);
        cardsRow.setVgap(16);
        cardsRow.setPadding(new Insets(10, 4, 10, 4));

        int[] cardIndex = {0};

        for (Showtime st : CinemaManager.getInstance().getShowtimes()) {
            Movie movie = st.getMovie();

            VBox card = new VBox(0);
            card.setPrefWidth(240);
            card.setStyle(
                "-fx-background-color: #161b2e;" +
                "-fx-background-radius: 6;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian,rgba(0,0,0,0.5),12,0,0,4);"
            );

            StackPane posterBox = new StackPane();
            posterBox.setPrefHeight(300);
            posterBox.setStyle(
                "-fx-background-color: #2b3250;" +
                "-fx-background-radius: 6 6 0 0;"
            );

            try {
                String imageName = movie.getTitle().toLowerCase()
                    .replace(" ", "_").replace(":", "") + ".jpg";
                Image img = new Image(
                    getClass().getResourceAsStream("/images/" + imageName),
                    240, 300, false, true
                );
                ImageView iv = new ImageView(img);
                iv.setFitWidth(240);
                iv.setFitHeight(300);
                iv.setPreserveRatio(false);

                Rectangle clip = new Rectangle(240, 300);
                clip.setArcWidth(12); clip.setArcHeight(12);
                iv.setClip(clip);
                posterBox.getChildren().add(iv);

            } catch (Exception ex) {
                Label placeholder = new Label("🎬");
                placeholder.setStyle("-fx-font-size: 40px;");
                posterBox.getChildren().add(placeholder);
            }

            VBox info = new VBox(4);
            info.setPadding(new Insets(12, 12, 14, 12));

            Label titleLbl = new Label(movie.getTitle());
            titleLbl.setStyle(
                "-fx-text-fill: #eaeaea;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;"
            );
            titleLbl.setWrapText(true);

            Label genreDuration = new Label(
                movie.getGenre() + "  •  " + movie.getDuration() + " min"
            );
            genreDuration.setStyle(
                "-fx-text-fill: #7a849a;" +
                "-fx-font-size: 11px;"
            );

            Label timeLbl = new Label("🕐 " + st.getTime());
            timeLbl.setStyle("-fx-text-fill: #c9a84c; -fx-font-size: 11px;");

            Label priceLbl = new Label("PHP " + st.getPrice());
            priceLbl.setStyle(
                "-fx-text-fill: #3d4560;" +
                "-fx-font-size: 11px;"
            );

            info.getChildren().addAll(titleLbl, genreDuration, timeLbl, priceLbl);
            card.getChildren().addAll(posterBox, info);

            card.setOnMouseEntered(ev -> card.setStyle(
                "-fx-background-color: #1e2540;" +
                "-fx-background-radius: 6;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian,rgba(0,0,0,0.7),20,0,0,8);"
            ));
            card.setOnMouseExited(ev -> card.setStyle(
                "-fx-background-color: #161b2e;" +
                "-fx-background-radius: 6;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian,rgba(0,0,0,0.5),12,0,0,4);"
            ));
            card.setOnMouseClicked(ev ->
                stage.setScene(new SeatSelectionScreen(stage, cashier, st).getScene())
            );

            // Add to grid AFTER card is fully built
            int col = cardIndex[0] % 10;
            int row = cardIndex[0] / 10;
            cardsRow.add(card, col, row);
            cardIndex[0]++;
        }

        scrollPane.setContent(cardsRow);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        content.getChildren().addAll(nowShowing, sub, sep, scrollPane);

        root.setLeft(sidebar);
        root.setCenter(content);
        return new Scene(root, 860, 520);
    }

    private Button navButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(14, 24, 14, 24));
        btn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #7a849a;" +
            "-fx-font-size: 13px;" +
            "-fx-cursor: hand;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: #1e2540;" +
            "-fx-text-fill: #c9a84c;" +
            "-fx-font-size: 13px;" +
            "-fx-cursor: hand;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #7a849a;" +
            "-fx-font-size: 13px;" +
            "-fx-cursor: hand;"
        ));
        return btn;
    }
}