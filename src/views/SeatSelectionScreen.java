package views;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import models.*;

public class SeatSelectionScreen {
    private Stage stage;
    private Cashier cashier;
    private Showtime showtime;

    public SeatSelectionScreen(Stage stage, Cashier cashier, Showtime showtime) {
        this.stage = stage;
        this.cashier = cashier;
        this.showtime = showtime;
    }

    public Scene getScene() {

        // ── Root ─────────────────────────────────────────────────────
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0b0f1a;");

        // ── Sidebar ───────────────────────────────────────────────────
        BorderPane sidebar = new BorderPane();
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #161b2e;");

        // ── Sidebar Top ───────────────────────────────────────────────
        VBox sideTop = new VBox(0);

        Rectangle accentBar = new Rectangle(4, 500);
        accentBar.setFill(Color.web("#c9a84c"));

        VBox brandBox = new VBox(4);
        brandBox.setPadding(new Insets(30, 20, 30, 20));
        brandBox.setStyle("-fx-border-color: transparent transparent #2b3250 transparent; -fx-border-width: 1;");
        Label brand = new Label("🎬  CINETICKET");
        brand.setStyle("-fx-text-fill: #c9a84c; -fx-font-size: 13px; -fx-font-weight: bold;");
        Label portalTag = new Label("SEAT SELECTION");
        portalTag.setStyle("-fx-text-fill: #3d4560; -fx-font-size: 10px; -fx-font-weight: bold;");
        brandBox.getChildren().addAll(brand, portalTag);

        // ── Booking Summary Card ──────────────────────────────────────
        VBox summaryCard = new VBox(14);
        summaryCard.setPadding(new Insets(20, 20, 20, 20));
        summaryCard.setStyle(
            "-fx-background-color: #0f1422;" +
            "-fx-border-color: #2b3250;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;"
        );
        VBox.setMargin(summaryCard, new Insets(20, 16, 0, 16));

        Label summaryTitle = new Label("BOOKING SUMMARY");
        summaryTitle.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 10px; -fx-font-weight: bold;");

        Label movieName = new Label(showtime.getMovie().getTitle());
        movieName.setStyle("-fx-text-fill: #eaeaea; -fx-font-size: 14px; -fx-font-weight: bold;");
        movieName.setWrapText(true);

        Label timeLabel = new Label("🕐  " + showtime.getTime());
        timeLabel.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 12px;");

        Label pricePerSeat = new Label("PHP " + showtime.getPrice() + " / seat");
        pricePerSeat.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 12px;");

        Separator sumSep = new Separator();
        sumSep.setStyle("-fx-background-color: #2b3250;");

        double[] total = {0};
        boolean[][] seats = showtime.getSeats();

        int availableCount = 0;
        for (boolean[] row : seats)
            for (boolean seat : row)
                if (!seat) availableCount++;

        Label availableLabel = new Label("Available: " + availableCount + " seats");
        availableLabel.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 12px;");

        Label totalLabel = new Label("Total:  PHP 0.00");
        totalLabel.setStyle(
            "-fx-text-fill: #c9a84c;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;"
        );

        summaryCard.getChildren().addAll(
            summaryTitle, movieName, timeLabel, pricePerSeat,
            sumSep, availableLabel, totalLabel
        );

        // ── Legend ────────────────────────────────────────────────────
        VBox legendBox = new VBox(8);
        legendBox.setPadding(new Insets(20, 20, 0, 20));

        Label legendTitle = new Label("LEGEND");
        legendTitle.setStyle("-fx-text-fill: #3d4560; -fx-font-size: 10px; -fx-font-weight: bold;");

        HBox availLegend   = legendItem("#2e7d4f", "Available");
        HBox takenLegend   = legendItem("#7a2020", "Taken");
        HBox selectedLegend = legendItem("#c9a84c", "Selected");

        legendBox.getChildren().addAll(legendTitle, availLegend, takenLegend, selectedLegend);

        sideTop.getChildren().addAll(
            new HBox(accentBar, brandBox) {{ setAlignment(Pos.CENTER_LEFT); }},
            summaryCard, legendBox
        );

        // ── Sidebar Bottom: Buttons always pinned ─────────────────────
        Button buyBtn = new Button("CONFIRM PURCHASE");
        buyBtn.setMaxWidth(Double.MAX_VALUE);
        buyBtn.setPadding(new Insets(13, 0, 13, 0));
        buyBtn.setStyle(
            "-fx-background-color: #c9a84c;" +
            "-fx-text-fill: #0b0f1a;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;"
        );
        buyBtn.setOnMouseEntered(e -> buyBtn.setStyle(
            "-fx-background-color: #e0bb6a; -fx-text-fill: #0b0f1a;" +
            "-fx-font-size: 12px; -fx-font-weight: bold;" +
            "-fx-background-radius: 4; -fx-cursor: hand;"
        ));
        buyBtn.setOnMouseExited(e -> buyBtn.setStyle(
            "-fx-background-color: #c9a84c; -fx-text-fill: #0b0f1a;" +
            "-fx-font-size: 12px; -fx-font-weight: bold;" +
            "-fx-background-radius: 4; -fx-cursor: hand;"
        ));

        Button backBtn = new Button("← Back");
        backBtn.setMaxWidth(Double.MAX_VALUE);
        backBtn.setPadding(new Insets(10, 0, 10, 0));
        backBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #3d4560;" +
            "-fx-font-size: 12px;" +
            "-fx-cursor: hand;"
        );
        backBtn.setOnMouseEntered(e -> backBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #7a849a;" +
            "-fx-font-size: 12px; -fx-cursor: hand;"
        ));
        backBtn.setOnMouseExited(e -> backBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #3d4560;" +
            "-fx-font-size: 12px; -fx-cursor: hand;"
        ));

        VBox sideBottom = new VBox(6);
        sideBottom.setPadding(new Insets(0, 16, 24, 16));
        sideBottom.getChildren().addAll(buyBtn, backBtn);

        sidebar.setTop(sideTop);
        sidebar.setBottom(sideBottom);

        // ── Main: Screen + Seat Grid ──────────────────────────────────
        VBox content = new VBox(28);
        content.setPadding(new Insets(40, 50, 40, 50));
        content.setAlignment(Pos.TOP_CENTER);

        Label screenLabel = new Label("S  C  R  E  E  N");
        screenLabel.setMaxWidth(Double.MAX_VALUE);
        screenLabel.setAlignment(Pos.CENTER);
        screenLabel.setPadding(new Insets(8, 0, 8, 0));
        screenLabel.setStyle(
            "-fx-text-fill: #3d4560;" +
            "-fx-font-size: 11px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-color: #161b2e;" +
            "-fx-background-radius: 4;" +
            "-fx-border-color: #2b3250;" +
            "-fx-border-radius: 4;" +
            "-fx-border-width: 1;"
        );

        // ── Seat Grid ─────────────────────────────────────────────────
        GridPane seatGrid = new GridPane();
        seatGrid.setHgap(10);
        seatGrid.setVgap(10);
        seatGrid.setAlignment(Pos.CENTER);

        boolean[] selectedState = new boolean[seats.length * seats[0].length];

        for (int r = 0; r < seats.length; r++) {
            Label rowLabel = new Label(String.valueOf((char)('A' + r)));
            rowLabel.setStyle("-fx-text-fill: #3d4560; -fx-font-size: 11px; -fx-font-weight: bold;");
            rowLabel.setAlignment(Pos.CENTER_RIGHT);
            rowLabel.setPrefWidth(20);
            seatGrid.add(rowLabel, 0, r);

            for (int c = 0; c < seats[r].length; c++) {
                double price = showtime.getPrice();
                Button seatBtn = new Button((char)('A' + r) + "" + (c + 1));
                seatBtn.setPrefSize(48, 40);
                seatBtn.setStyle(seats[r][c] ? takenStyle() : availStyle());
                if (seats[r][c]) seatBtn.setDisable(true);

                int idx = r * seats[0].length + c;

                if (!seats[r][c]) {
                    seatBtn.setOnAction(e -> {
                        if (!selectedState[idx]) {
                            selectedState[idx] = true;
                            seatBtn.setStyle(selectedStyle());
                            total[0] += price;
                        } else {
                            selectedState[idx] = false;
                            seatBtn.setStyle(availStyle());
                            total[0] -= price;
                        }
                        totalLabel.setText(String.format("Total:  PHP %.2f", total[0]));
                    });
                }

                seatGrid.add(seatBtn, c + 1, r);
            }
        }

        content.getChildren().addAll(screenLabel, seatGrid);

        // ── Wire up buttons ───────────────────────────────────────────
        buyBtn.setOnAction(e -> {
            if (total[0] == 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Seats Selected");
                alert.setHeaderText(null);
                alert.setContentText("Please select at least one seat before confirming.");
                alert.showAndWait();
                return;
            }
            for (int r = 0; r < seats.length; r++) {
                for (int c = 0; c < seats[r].length; c++) {
                    int idx = r * seats[0].length + c;
                    if (selectedState[idx]) {
                        showtime.bookSeat(r, c);
                        SalesManager.getInstance().recordSale(
                            showtime.getMovieTitle(), showtime.getPrice()
                        );
                    }
                }
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Purchase Confirmed");
            alert.setHeaderText(null);
            alert.setContentText("Purchase confirmed!\nTotal paid: PHP " + String.format("%.2f", total[0]));
            alert.showAndWait();
            stage.setScene(new CashierDashboard(stage, cashier).getScene());
        });

        backBtn.setOnAction(e -> stage.setScene(new CashierDashboard(stage, cashier).getScene()));

        root.setLeft(sidebar);
        root.setCenter(content);
        return new Scene(root, 920, 580);
    }

    // ── Style Helpers ─────────────────────────────────────────────────

    private String availStyle() {
        return  "-fx-background-color: #2e7d4f;" +
                "-fx-text-fill: #eaeaea;" +
                "-fx-font-size: 10px;" +
                "-fx-background-radius: 4;" +
                "-fx-cursor: hand;";
    }

    private String takenStyle() {
        return  "-fx-background-color: #7a2020;" +
                "-fx-text-fill: #3d4560;" +
                "-fx-font-size: 10px;" +
                "-fx-background-radius: 4;";
    }

    private String selectedStyle() {
        return  "-fx-background-color: #c9a84c;" +
                "-fx-text-fill: #0b0f1a;" +
                "-fx-font-size: 10px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 4;" +
                "-fx-cursor: hand;";
    }

    private HBox legendItem(String color, String label) {
        Rectangle box = new Rectangle(14, 14);
        box.setFill(Color.web(color));
        box.setArcWidth(4); box.setArcHeight(4);
        Label lbl = new Label(label);
        lbl.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 11px;");
        HBox row = new HBox(8, box, lbl);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }
}