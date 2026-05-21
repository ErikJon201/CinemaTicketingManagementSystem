package views;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import models.*;

import java.util.ArrayList;
import java.util.List;

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
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:" + UIHelper.BG + ";");

        boolean[][] seats = showtime.getSeats();
        boolean[] selected = new boolean[seats.length * seats[0].length];
        List<String> selectedLabels = new ArrayList<>();
        double[] total = {0};

        // ── Sidebar ────────────────────────────────────────────────────────────
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(270);
        sidebar.setMinWidth(270);
        sidebar.setStyle("-fx-background-color:" + UIHelper.SIDEBAR + ";");

        // Brand
        VBox brand = new VBox(3);
        brand.setPadding(new Insets(24, 20, 20, 20));
        brand.setStyle("-fx-border-color:transparent transparent " + UIHelper.BORDER +
                " transparent;-fx-border-width:1;");
        brand.getChildren().addAll(
            UIHelper.lbl("CINEMAX", UIHelper.RED, 20, true),
            UIHelper.lbl("Seat Selection", UIHelper.TEXT2, 11, false));

        // Booking summary card
        VBox summaryCard = UIHelper.card();
        VBox.setMargin(summaryCard, new Insets(16, 14, 0, 14));

        Label sumTitle = UIHelper.lbl("BOOKING SUMMARY", UIHelper.MUTED, 10, true);

        Label movieLbl = UIHelper.lbl(showtime.getMovieTitle(), UIHelper.TEXT, 14, true);
        movieLbl.setWrapText(true);
        Label genreLbl = UIHelper.lbl(showtime.getMovieGenre() + "  •  " +
                showtime.getMovie().getDurationFormatted(), UIHelper.TEXT2, 12, false);
        Label dateLbl  = UIHelper.lbl(showtime.getDateTime(), UIHelper.TEXT2, 12, false);
        Label roomLbl  = UIHelper.lbl(showtime.getRoomName(), UIHelper.TEXT2, 12, false);
        Label priceLbl = UIHelper.lbl(String.format("PHP %.0f / seat", showtime.getPrice()),
                UIHelper.TEXT2, 12, false);

        int initAvail = showtime.getAvailableSeats();
        Label availLbl  = UIHelper.lbl("Available: " + initAvail + " seats",
                UIHelper.GREEN, 12, false);
        Label selectedLbl = UIHelper.lbl("Selected: 0 seats", UIHelper.TEXT2, 12, false);
        Label totalLbl  = UIHelper.lbl("Total: PHP 0.00", UIHelper.GOLD, 20, true);

        summaryCard.getChildren().addAll(
            sumTitle, UIHelper.sep(),
            movieLbl, genreLbl, dateLbl, roomLbl, priceLbl,
            UIHelper.sep(),
            availLbl, selectedLbl, totalLbl);

        // Legend
        VBox legend = new VBox(8);
        legend.setPadding(new Insets(16, 20, 16, 20));
        legend.getChildren().addAll(
            UIHelper.sectionLbl("Legend"),
            legendRow(UIHelper.SEAT_FREE, UIHelper.SEAT_FREE_B,   "Available"),
            legendRow(UIHelper.SEAT_TAKEN, UIHelper.SEAT_TAKEN_B, "Taken"),
            legendRow(UIHelper.SEAT_SEL,  UIHelper.SEAT_SEL_B,    "Selected"));

        // Action buttons
        VBox actions = new VBox(8);
        actions.setPadding(new Insets(0, 14, 20, 14));
        actions.setStyle("-fx-border-color:" + UIHelper.BORDER +
                " transparent transparent transparent;-fx-border-width:1;");

        Button confirmBtn = UIHelper.btn("Confirm Purchase", UIHelper.GREEN, "#ffffff");
        confirmBtn.setMaxWidth(Double.MAX_VALUE);
        confirmBtn.setPrefHeight(44);

        Button backBtn = new Button("Back");
        backBtn.setMaxWidth(Double.MAX_VALUE);
        backBtn.setStyle("-fx-background-color:transparent;-fx-text-fill:" + UIHelper.TEXT2 +
                ";-fx-font-size:13;-fx-cursor:hand;-fx-padding:9 0;");
        backBtn.setOnMouseEntered(e ->
            backBtn.setStyle("-fx-background-color:transparent;-fx-text-fill:" + UIHelper.TEXT +
                    ";-fx-font-size:13;-fx-cursor:hand;-fx-padding:9 0;"));
        backBtn.setOnMouseExited(e ->
            backBtn.setStyle("-fx-background-color:transparent;-fx-text-fill:" + UIHelper.TEXT2 +
                    ";-fx-font-size:13;-fx-cursor:hand;-fx-padding:9 0;"));
        backBtn.setOnAction(e ->
            stage.setScene(new CashierDashboard(stage, cashier).getScene()));

        actions.getChildren().addAll(confirmBtn, backBtn);

        Region sidespacer = new Region();
        VBox.setVgrow(sidespacer, Priority.ALWAYS);
        sidebar.getChildren().addAll(brand, summaryCard, legend, sidespacer, actions);

        // ── Seat grid ──────────────────────────────────────────────────────────
        VBox seatArea = new VBox(20);
        seatArea.setPadding(new Insets(36, 44, 44, 44));
        seatArea.setAlignment(Pos.TOP_CENTER);

        // Screen indicator
        HBox screenBox = new HBox();
        screenBox.setAlignment(Pos.CENTER);
        screenBox.setMaxWidth(Double.MAX_VALUE);
        Label screenLbl = new Label("S  C  R  E  E  N");
        screenLbl.setMaxWidth(Double.MAX_VALUE);
        screenLbl.setAlignment(Pos.CENTER);
        screenLbl.setPadding(new Insets(7, 0, 7, 0));
        screenLbl.setStyle("-fx-text-fill:" + UIHelper.MUTED + ";-fx-font-size:11;" +
                "-fx-font-weight:bold;-fx-background-color:" + UIHelper.CARD2 +
                ";-fx-background-radius:4;-fx-border-color:" + UIHelper.BORDER +
                ";-fx-border-radius:4;-fx-border-width:1;");
        HBox.setHgrow(screenLbl, Priority.ALWAYS);
        screenBox.getChildren().add(screenLbl);
        screenBox.setMaxWidth(700);

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setAlignment(Pos.CENTER);

        // Column number headers
        for (int c = 0; c < seats[0].length; c++) {
            Label colHdr = UIHelper.lbl(String.valueOf(c + 1), UIHelper.MUTED, 10, false);
            colHdr.setPrefWidth(42);
            colHdr.setAlignment(Pos.CENTER);
            grid.add(colHdr, c + 1, 0);
        }

        for (int r = 0; r < seats.length; r++) {
            Label rowHdr = UIHelper.lbl(String.valueOf((char)('A' + r)), UIHelper.MUTED, 11, true);
            rowHdr.setPrefWidth(20);
            rowHdr.setAlignment(Pos.CENTER_RIGHT);
            grid.add(rowHdr, 0, r + 1);

            for (int c = 0; c < seats[r].length; c++) {
                Button seatBtn = new Button((char)('A' + r) + "" + (c + 1));
                seatBtn.setPrefSize(42, 36);
                seatBtn.setStyle(seats[r][c] ? takenStyle() : freeStyle());
                if (seats[r][c]) seatBtn.setDisable(true);

                int rr = r, cc = c;
                int idx = r * seats[0].length + c;
                String seatLabel = (char)('A' + r) + "" + (c + 1);
                double price = showtime.getPrice();

                if (!seats[r][c]) {
                    seatBtn.setOnAction(e -> {
                        if (!selected[idx]) {
                            selected[idx] = true;
                            selectedLabels.add(seatLabel);
                            seatBtn.setStyle(selectedStyle());
                            total[0] += price;
                        } else {
                            selected[idx] = false;
                            selectedLabels.remove(seatLabel);
                            seatBtn.setStyle(freeStyle());
                            total[0] -= price;
                        }
                        int cnt = selectedLabels.size();
                        selectedLbl.setText("Selected: " + cnt + " seat" + (cnt != 1 ? "s" : ""));
                        totalLbl.setText(String.format("Total: PHP %.2f", total[0]));
                    });
                }
                grid.add(seatBtn, c + 1, r + 1);
            }
        }

        ScrollPane gridScroll = new ScrollPane(grid);
        gridScroll.setFitToWidth(true);
        gridScroll.setStyle("-fx-background:transparent;-fx-background-color:transparent;");

        seatArea.getChildren().addAll(screenBox, gridScroll);

        // ── Confirm action ─────────────────────────────────────────────────────
        confirmBtn.setOnAction(e -> {
            if (selectedLabels.isEmpty()) {
                Alert warn = new Alert(Alert.AlertType.WARNING, "Please select at least one seat.");
                warn.setHeaderText(null);
                warn.showAndWait();
                return;
            }

            // Book seats and record single sale
            for (int r = 0; r < seats.length; r++)
                for (int c = 0; c < seats[r].length; c++)
                    if (selected[r * seats[0].length + c])
                        showtime.bookSeat(r, c);

            List<String> boughtSeats = new ArrayList<>(selectedLabels);
            SalesManager.getInstance().recordSale(
                showtime.getMovieTitle(), total[0],
                cashier.getFullName(), boughtSeats);

            // Receipt dialog
            showReceipt(boughtSeats, total[0]);
        });

        root.setLeft(sidebar);
        root.setCenter(seatArea);
        return new Scene(root, 1280, 760);
    }

    private void showReceipt(List<String> seats, double amount) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Purchase Confirmed");

        VBox receipt = new VBox(14);
        receipt.setPadding(new Insets(24, 28, 24, 28));
        receipt.setPrefWidth(380);
        receipt.setStyle("-fx-background-color:" + UIHelper.CARD + ";");

        receipt.getChildren().addAll(
            UIHelper.lbl("CINEMAX", UIHelper.RED, 22, true),
            UIHelper.lbl("Purchase Receipt", UIHelper.TEXT, 16, true),
            UIHelper.sep(),
            receiptRow("Movie:",    showtime.getMovieTitle()),
            receiptRow("Date/Time:", showtime.getDateTime()),
            receiptRow("Room:",     showtime.getRoomName()),
            receiptRow("Seats:",    String.join(", ", seats)),
            receiptRow("Qty:",      seats.size() + " seat" + (seats.size() != 1 ? "s" : "")),
            receiptRow("Price/Seat:", String.format("PHP %.2f", showtime.getPrice())),
            UIHelper.sep(),
            receiptRow("TOTAL:",    String.format("PHP %.2f", amount)),
            UIHelper.sep(),
            UIHelper.lbl("Served by: " + cashier.getFullName(), UIHelper.TEXT2, 12, false),
            UIHelper.lbl("Thank you! Enjoy the show.", UIHelper.GREEN, 13, true));

        dialog.getDialogPane().setContent(receipt);
        dialog.getDialogPane().setStyle("-fx-background-color:" + UIHelper.CARD + ";");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();

        stage.setScene(new CashierDashboard(stage, cashier).getScene());
    }

    private HBox receiptRow(String key, String value) {
        HBox row = new HBox();
        Label k = UIHelper.lbl(key,   UIHelper.TEXT2, 12, false); k.setPrefWidth(110);
        Label v = UIHelper.lbl(value, UIHelper.TEXT,  12, true);
        v.setWrapText(true);
        HBox.setHgrow(v, Priority.ALWAYS);
        row.getChildren().addAll(k, v);
        return row;
    }

    private HBox legendRow(String bg, String border, String label) {
        Rectangle box = new Rectangle(14, 14);
        box.setFill(Color.web(bg));
        box.setStroke(Color.web(border));
        box.setStrokeWidth(1.5);
        box.setArcWidth(3);
        box.setArcHeight(3);
        Label lbl = UIHelper.lbl(label, UIHelper.TEXT2, 12, false);
        HBox row = new HBox(8, box, lbl);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private String freeStyle() {
        return "-fx-background-color:" + UIHelper.SEAT_FREE +
                ";-fx-text-fill:#aaaaaa;-fx-font-size:10;" +
                "-fx-background-radius:4;-fx-border-color:" + UIHelper.SEAT_FREE_B +
                ";-fx-border-radius:4;-fx-border-width:1;-fx-cursor:hand;";
    }

    private String takenStyle() {
        return "-fx-background-color:" + UIHelper.SEAT_TAKEN +
                ";-fx-text-fill:#555555;-fx-font-size:10;" +
                "-fx-background-radius:4;-fx-border-color:" + UIHelper.SEAT_TAKEN_B +
                ";-fx-border-radius:4;-fx-border-width:1;";
    }

    private String selectedStyle() {
        return "-fx-background-color:" + UIHelper.SEAT_SEL +
                ";-fx-text-fill:#ffffff;-fx-font-size:10;-fx-font-weight:bold;" +
                "-fx-background-radius:4;-fx-border-color:" + UIHelper.SEAT_SEL_B +
                ";-fx-border-radius:4;-fx-border-width:1.5;-fx-cursor:hand;";
    }
}