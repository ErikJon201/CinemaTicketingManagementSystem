package views;

import models.Cashier;
import models.CinemaManager;
import models.Movie;
import models.Showtime;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;

public class CashierDashboard {

    private Stage stage;
    private Cashier cashier;

    public CashierDashboard(Stage stage, Cashier cashier) {
        this.stage = stage;
        this.cashier = cashier;
    }

    public Scene getScene() {
        
        // ── Root ─────────────────────────────────────────────────────
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0b0f1a;");

        // ── Sidebar ──────────────────────────────────────────────────
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(240);
        sidebar.setStyle("-fx-background-color: #161b2e;");

        // Gold accent bar on the left edge of sidebar
        Rectangle accentBar = new Rectangle(4, 600);
        accentBar.setFill(Color.web("#c9a84c"));

        // Brand label at top of sidebar
        VBox brandBox = new VBox(4);
        brandBox.setPadding(new Insets(30, 20, 30, 20));
        brandBox.setStyle("-fx-border-color: transparent transparent #2b3250 transparent; -fx-border-width: 1;");

        Label brand = new Label("🎬  CINETICKET");
        brand.setStyle(
            "-fx-text-fill: #c9a84c;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;"
        );
        Label roleTag = new Label("CASHIER PORTAL");
        roleTag.setStyle(
            "-fx-text-fill: #3d4560;" +
            "-fx-font-size: 10px;" +
            "-fx-font-weight: bold;"
        );
        brandBox.getChildren().addAll(brand, roleTag);

        // Nav buttons
        Button searchMoviesBtn = navButton("🔍   Search Movies");
        Button sellTicketBtn   = navButton("🎟   Select Movie");
    
        // Spacer to push logout to bottom
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button logoutBtn = navButton("⎋   Logout");
        logoutBtn.setStyle(logoutNavStyle());
        logoutBtn.setOnMouseEntered(e -> logoutBtn.setStyle(logoutNavHoverStyle()));
        logoutBtn.setOnMouseExited(e  -> logoutBtn.setStyle(logoutNavStyle()));

        VBox.setMargin(logoutBtn, new Insets(0, 0, 20, 0));

        sidebar.getChildren().addAll(
            new HBox(accentBar, brandBox) {{ setAlignment(Pos.CENTER_LEFT); }},
            searchMoviesBtn, sellTicketBtn,
            spacer, logoutBtn
        );

        // ── Main Content ─────────────────────────────────────────────
        VBox content = new VBox(24);
        content.setPadding(new Insets(50, 50, 50, 50));
        content.setStyle("-fx-background-color: #0b0f1a;");
        // Top greeting
        Label greeting = new Label("Welcome back,");
        greeting.setStyle(
            "-fx-text-fill: #7a849a;" +
            "-fx-font-size: 14px;"
        );

        Label username = new Label(cashier.getFullName());
        username.setStyle(
            "-fx-text-fill: #eaeaea;" +
            "-fx-font-size: 32px;" +
            "-fx-font-weight: bold;"
        );

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #2b3250;");
        VBox.setMargin(sep, new Insets(4, 0, 12, 0));

        // Section label
        Label sectionLabel = new Label("QUICK ACTIONS");
        sectionLabel.setStyle(
            "-fx-text-fill: #7a849a;" +
            "-fx-font-size: 10px;" +
            "-fx-font-weight: bold;"
        );

        Label Top10 = new Label("TOP 10 MOVIES");
        Top10.setStyle(
            "-fx-text-fill: #7a849a;" +
            "-fx-font-size: 10px;" +
            "-fx-font-weight: bold;"
        );

        // Action cards row
        HBox cards = new HBox(20);

        VBox searchCard = actionCard(
            "🔍", "Search Movies",
            "Browse and look up movies\ncurrently in the system"
        );
        VBox sellCard = actionCard(
            "🎟", "Select movie",
            "Select a movie and choose\nseats for a customer"
        );
        VBox lgoutBox = actionCard(
            "⎋", "Exit / Logout",
            "Exit the dashboard and return\nto the login screen"
        );

        cards.getChildren().addAll(searchCard, sellCard, lgoutBox);

        // Footer
        Label footer = new Label("Authorized personnel only  •  " + cashier.getRole());
        footer.setStyle("-fx-text-fill: #2b3250; -fx-font-size: 11px;");
        Region contentSpacer = new Region();
        VBox.setVgrow(contentSpacer, Priority.ALWAYS);

        HBox cardsRow = new HBox(10);
        cardsRow.setPadding(new Insets(10, 4, 10, 4));

        ScrollPane movieScroll = new ScrollPane(cardsRow);
        movieScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        movieScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        movieScroll.setFitToHeight(true);
        movieScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

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

            // ── POSTER IMAGE ─────────────────────────────────────────
            // TODO: Replace the path below with your actual image file.
            // Put your poster images in: src/resources/images/
            // Name them to match the movie title e.g. "inception.jpg"
            // Example: new Image(getClass().getResourceAsStream("/images/inception.jpg"))
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

                // Rounded top clip
                Rectangle clip = new Rectangle(240, 300);
                clip.setArcWidth(12); clip.setArcHeight(12);
                iv.setClip(clip);
                posterBox.getChildren().add(iv);

            } catch (Exception ex) {
                // Fallback if image not found
                Label placeholder = new Label("🎬");
                placeholder.setStyle("-fx-font-size: 40px;");
                posterBox.getChildren().add(placeholder);
            }   
            

            // ── Movie Info ────────────────────────────────────────────
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

            // Hover effect
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

            // Click to go to seat selection
            card.setOnMouseClicked(ev ->
                stage.setScene(new SeatSelectionScreen(stage, cashier, st).getScene())
            );

            cardsRow.getChildren().add(card);
        }

        content.getChildren().addAll(
            greeting, username, sep,
            sectionLabel, cards, Top10,movieScroll,
            contentSpacer, footer
        );

        // ── Wire up buttons ──────────────────────────────────────────
        searchMoviesBtn.setOnAction(e ->
            stage.setScene(new SearchMovieScreen(stage, cashier).getScene())
        );
        sellTicketBtn.setOnAction(e ->
            stage.setScene(new MovieSelectionScreen(stage, cashier).getScene())
        );
        logoutBtn.setOnAction(e ->
            stage.setScene(new LoginScreen(stage).getScene())
        );

        // Card clicks mirror the nav buttons
        searchCard.setOnMouseClicked(e ->
            stage.setScene(new SearchMovieScreen(stage, cashier).getScene())
        );
        sellCard.setOnMouseClicked(e ->
            stage.setScene(new MovieSelectionScreen(stage, cashier).getScene())
        );
        lgoutBox.setOnMouseClicked(e ->
            stage.setScene(new LoginScreen(stage).getScene())
        );
        root.setLeft(sidebar);

        root.setCenter(content);
        return new Scene(root, 860, 520);
    }

    // ── Helpers ───────────────────────────────────────────────────────

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

    private VBox actionCard(String icon, String title, String desc) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(28, 28, 28, 28));
        card.setPrefWidth(220);
        card.setStyle(
            "-fx-background-color: #161b2e;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 16, 0, 0, 4);"
        );

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 26px;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-text-fill: #eaeaea;" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;"
        );

        Label descLabel = new Label(desc);
        descLabel.setStyle(
            "-fx-text-fill: #7a849a;" +
            "-fx-font-size: 12px;"
        );
        descLabel.setWrapText(true);

        // Gold bottom accent bar
        Rectangle bar = new Rectangle(40, 3);
        bar.setFill(Color.web("#c9a84c"));
        bar.setArcWidth(2); bar.setArcHeight(2);
        VBox.setMargin(bar, new Insets(6, 0, 0, 0));

        card.getChildren().addAll(iconLabel, titleLabel, descLabel, bar);

        // Hover lift effect
        card.setOnMouseEntered(e -> card.setStyle(
            "-fx-background-color: #1e2540;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 24, 0, 0, 8);"
        ));
        card.setOnMouseExited(e -> card.setStyle(
            "-fx-background-color: #161b2e;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 16, 0, 0, 4);"
        ));

        return card;
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