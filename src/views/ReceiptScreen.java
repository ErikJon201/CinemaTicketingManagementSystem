package views;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import models.*;
import java.time.format.DateTimeFormatter;

public class ReceiptScreen {
    private Stage stage;
    private Cashier cashier;
    private Receipt receipt;

    public ReceiptScreen(Stage stage, Cashier cashier, Receipt receipt) {
        this.stage   = stage;
        this.cashier = cashier;
        this.receipt = receipt;
    }

    public Scene getScene() {

        Ticket ticket = receipt.getTicket();

        // Root
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0b0f1a;");

        // Sidebar
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #161b2e;");

        Rectangle accentBar = new Rectangle(4, 600);
        accentBar.setFill(Color.web("#c9a84c"));

        VBox brandBox = new VBox(4);
        brandBox.setPadding(new Insets(30, 20, 30, 20));
        brandBox.setStyle("-fx-border-color: transparent transparent #2b3250 transparent; -fx-border-width: 1;");

        Label brand = new Label("🎬  CINETICKET");
        brand.setStyle("-fx-text-fill: #c9a84c; -fx-font-size: 13px; -fx-font-weight: bold;");
        Label portalTag = new Label("RECEIPT");
        portalTag.setStyle("-fx-text-fill: #3d4560; -fx-font-size: 10px; -fx-font-weight: bold;");
        brandBox.getChildren().addAll(brand, portalTag);

        // Sidebar summary card
        VBox summaryCard = new VBox(10);
        summaryCard.setPadding(new Insets(16, 16, 16, 16));
        summaryCard.setStyle(
            "-fx-background-color: #0f1422;" +
            "-fx-border-color: #2b3250;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;"
        );
        VBox.setMargin(summaryCard, new Insets(20, 16, 0, 16));

        Label summaryTitle = new Label("TICKET SUMMARY");
        summaryTitle.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 10px; -fx-font-weight: bold;");

        Label ticketIdLbl = new Label("#" + String.format("%04d", ticket.getTicketId()));
        ticketIdLbl.setStyle("-fx-text-fill: #c9a84c; -fx-font-size: 22px; -fx-font-weight: bold;");

        Label movieLblSide = new Label(ticket.getShowtime().getMovieTitle());
        movieLblSide.setStyle("-fx-text-fill: #eaeaea; -fx-font-size: 13px; -fx-font-weight: bold;");
        movieLblSide.setWrapText(true);

        Label timeLblSide = new Label("🕐  " + ticket.getShowtime().getTime());
        timeLblSide.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 11px;");

        Label roomLblSide = new Label("🏛  " + ticket.getShowtime().getRoomName());
        roomLblSide.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 11px;");

        Label seatsLblSide = new Label("💺  " + String.join(", ", ticket.getSeatLabels()));
        seatsLblSide.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 11px;");
        seatsLblSide.setWrapText(true);

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #2b3250;");

        Label totalSideLbl = new Label("PHP " + String.format("%.2f", ticket.getTotalAmount()));
        totalSideLbl.setStyle("-fx-text-fill: #c9a84c; -fx-font-size: 18px; -fx-font-weight: bold;");

        summaryCard.getChildren().addAll(
            summaryTitle, ticketIdLbl, movieLblSide,
            timeLblSide, roomLblSide, seatsLblSide, sep, totalSideLbl
        );

        sidebar.getChildren().addAll(
            new HBox(accentBar, brandBox) {{ setAlignment(Pos.CENTER_LEFT); }},
            summaryCard
        );

        // Sidebar bottom button
        Button dashBtn = new Button("← Back to Dashboard");
        dashBtn.setMaxWidth(Double.MAX_VALUE);
        dashBtn.setPadding(new Insets(13, 0, 13, 0));
        dashBtn.setStyle(
            "-fx-background-color: #c9a84c;" +
            "-fx-text-fill: #0b0f1a;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;"
        );
        dashBtn.setOnMouseEntered(e -> dashBtn.setStyle(
            "-fx-background-color: #e0bb6a; -fx-text-fill: #0b0f1a;" +
            "-fx-font-size: 12px; -fx-font-weight: bold;" +
            "-fx-background-radius: 4; -fx-cursor: hand;"
        ));
        dashBtn.setOnMouseExited(e -> dashBtn.setStyle(
            "-fx-background-color: #c9a84c; -fx-text-fill: #0b0f1a;" +
            "-fx-font-size: 12px; -fx-font-weight: bold;" +
            "-fx-background-radius: 4; -fx-cursor: hand;"
        ));
        dashBtn.setOnAction(e -> stage.setScene(new CashierDashboard(stage, cashier).getScene()));

        VBox sideBottom = new VBox(6);
        sideBottom.setPadding(new Insets(0, 16, 24, 16));
        sideBottom.getChildren().add(dashBtn);

        BorderPane sidePane = new BorderPane();
        sidePane.setPrefWidth(220);
        sidePane.setStyle("-fx-background-color: #161b2e;");
        sidePane.setTop(sidebar);
        sidePane.setBottom(sideBottom);

        // Main: Centered receipt card
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox outer = new VBox();
        outer.setAlignment(Pos.TOP_CENTER);
        outer.setPadding(new Insets(40, 20, 40, 20));

        // Receipt card
        VBox card = new VBox(0);
        card.setPrefWidth(340);
        card.setMaxWidth(340);
        card.setStyle(
            "-fx-background-color: #161b2e;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #2b3250;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 10;"
        );

        // Card header
        VBox header = new VBox(4);
        header.setPadding(new Insets(24, 24, 20, 24));
        header.setAlignment(Pos.CENTER);
        header.setStyle(
            "-fx-background-color: #0b0f1a;" +
            "-fx-background-radius: 10 10 0 0;"
        );

        Label brandLbl = new Label("CINETICKET");
        brandLbl.setStyle("-fx-text-fill: #c9a84c; -fx-font-size: 11px; -fx-font-weight: bold;");

        Label movieLbl = new Label(ticket.getShowtime().getMovieTitle());
        movieLbl.setStyle("-fx-text-fill: #eaeaea; -fx-font-size: 18px; -fx-font-weight: bold;");
        movieLbl.setWrapText(true);
        movieLbl.setTextAlignment(TextAlignment.CENTER);
        movieLbl.setAlignment(Pos.CENTER);
        movieLbl.setMaxWidth(290);

        Label genreLbl = new Label(
            ticket.getShowtime().getMovieGenre() + "  •  " +
            ticket.getShowtime().getMovieDuration() + " min"
        );
        genreLbl.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 11px;");

        header.getChildren().addAll(brandLbl, movieLbl, genreLbl);

        // Card body
        VBox body = new VBox(0);
        body.setPadding(new Insets(16, 24, 16, 24));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM dd, yyyy  hh:mm a");

        body.getChildren().addAll(
            receiptRow("Ticket #",  String.format("#%04d", ticket.getTicketId())),
            receiptRow("Showtime",  ticket.getShowtime().getTime()),
            receiptRow("Room",      ticket.getShowtime().getRoomName()),
            receiptRow("Seats",     String.join(", ", ticket.getSeatLabels())),
            receiptRow("Cashier",   ticket.getCashier().getFullName()),
            receiptRow("Date",      ticket.getPurchaseTime().format(fmt))
        );

        // Dashed divider
        Label dashedLine = new Label("- - - - - - - - - - - - - - - - - - - - - - -");
        dashedLine.setStyle("-fx-text-fill: #2b3250; -fx-font-size: 11px;");
        dashedLine.setMaxWidth(Double.MAX_VALUE);
        dashedLine.setAlignment(Pos.CENTER);
        VBox.setMargin(dashedLine, new Insets(4, 0, 4, 0));

        // Total row
        HBox totalRow = new HBox();
        totalRow.setPadding(new Insets(10, 24, 16, 24));
        totalRow.setAlignment(Pos.CENTER_LEFT);
        Label totalKey = new Label("Total");
        totalKey.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 13px;");
        Region totalSpacer = new Region();
        HBox.setHgrow(totalSpacer, Priority.ALWAYS);
        Label totalVal = new Label("PHP " + String.format("%.2f", ticket.getTotalAmount()));
        totalVal.setStyle("-fx-text-fill: #c9a84c; -fx-font-size: 20px; -fx-font-weight: bold;");
        totalRow.getChildren().addAll(totalKey, totalSpacer, totalVal);

        // Card footer
        VBox cardFooter = new VBox();
        cardFooter.setPadding(new Insets(12, 24, 16, 24));
        cardFooter.setAlignment(Pos.CENTER);
        cardFooter.setStyle(
            "-fx-border-color: #2b3250 transparent transparent transparent;" +
            "-fx-border-width: 1;"
        );
        Label thankYou = new Label("Thank you for choosing CineTicket!");
        thankYou.setStyle("-fx-text-fill: #3d4560; -fx-font-size: 11px;");
        cardFooter.getChildren().add(thankYou);

        card.getChildren().addAll(header, body, dashedLine, totalRow, cardFooter);

        // New Transaction button
        Button newTxnBtn = new Button("+ New Transaction");
        VBox.setMargin(newTxnBtn, new Insets(20, 0, 0, 0));
        newTxnBtn.setPadding(new Insets(10, 24, 10, 24));
        newTxnBtn.setStyle(
            "-fx-background-color: #1e2540;" +
            "-fx-text-fill: #7a849a;" +
            "-fx-font-size: 12px;" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;"
        );
        newTxnBtn.setOnMouseEntered(e -> newTxnBtn.setStyle(
            "-fx-background-color: #2b3250; -fx-text-fill: #eaeaea;" +
            "-fx-font-size: 12px; -fx-background-radius: 4; -fx-cursor: hand;"
        ));
        newTxnBtn.setOnMouseExited(e -> newTxnBtn.setStyle(
            "-fx-background-color: #1e2540; -fx-text-fill: #7a849a;" +
            "-fx-font-size: 12px; -fx-background-radius: 4; -fx-cursor: hand;"
        ));
        newTxnBtn.setOnAction(e -> stage.setScene(new MovieSelectionScreen(stage, cashier).getScene()));

        outer.getChildren().addAll(card, newTxnBtn);
        scrollPane.setContent(outer);

        root.setLeft(sidePane);
        root.setCenter(scrollPane);
        return new Scene(root, 920, 580);
    }

    // Helper
    private HBox receiptRow(String key, String value) {
        HBox row = new HBox();
        row.setPadding(new Insets(7, 0, 7, 0));
        row.setStyle("-fx-border-color: transparent transparent #1e2540 transparent; -fx-border-width: 1;");

        Label keyLbl = new Label(key);
        keyLbl.setStyle("-fx-text-fill: #7a849a; -fx-font-size: 12px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label valLbl = new Label(value);
        valLbl.setStyle("-fx-text-fill: #eaeaea; -fx-font-size: 12px; -fx-font-weight: bold;");
        valLbl.setWrapText(true);
        valLbl.setMaxWidth(200);
        valLbl.setTextAlignment(TextAlignment.RIGHT);
        valLbl.setAlignment(Pos.CENTER_RIGHT);

        row.getChildren().addAll(keyLbl, spacer, valLbl);
        return row;
    }
}