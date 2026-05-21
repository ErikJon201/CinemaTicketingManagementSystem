package views;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.*;

import java.util.Map;

public class SalesReportScreen {
    private Stage stage;
    private Admin admin;

    public SalesReportScreen(Stage stage, Admin admin) {
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
            () -> stage.setScene(new ManageUsersScreen(stage, admin).getScene()),
            null
        };
        root.setLeft(UIHelper.sidebar(admin, "Sales Report", nav, acts,
                () -> stage.setScene(new LoginScreen(stage).getScene())));

        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background:transparent;-fx-background-color:transparent;");

        VBox content = new VBox(28);
        content.setPadding(new Insets(44, 48, 44, 48));
        content.setStyle("-fx-background-color:" + UIHelper.BG + ";");

        content.getChildren().add(UIHelper.pageHeader("Sales Report",
                "Revenue analytics and transaction history."));

        // ── Summary stats ──────────────────────────────────────────────────────
        SalesManager sm = SalesManager.getInstance();

        HBox statsRow = new HBox(16);
        HBox s1 = UIHelper.statCard(String.format("PHP %.2f", sm.getTotalRevenue()),
                "Total Revenue", UIHelper.GREEN);
        HBox s2 = UIHelper.statCard(String.valueOf(sm.getTotalTicketsSold()),
                "Tickets Sold", UIHelper.BLUE);
        HBox s3 = UIHelper.statCard(String.valueOf(sm.getTransactionCount()),
                "Transactions", UIHelper.PURPLE);
        HBox s4 = UIHelper.statCard(String.format("PHP %.2f", sm.getRevenueToday()),
                "Today's Revenue", UIHelper.GOLD);
        for (HBox s : new HBox[]{s1, s2, s3, s4}) HBox.setHgrow(s, Priority.ALWAYS);
        statsRow.getChildren().addAll(s1, s2, s3, s4);

        // ── Revenue by movie ───────────────────────────────────────────────────
        Label movSec = UIHelper.sectionLbl("Revenue by Movie");
        VBox.setMargin(movSec, new Insets(8, 0, 4, 0));

        TableView<Map.Entry<String, Double>> movieTable = UIHelper.table();
        movieTable.setPrefHeight(220);
        movieTable.setMaxHeight(260);

        TableColumn<Map.Entry<String, Double>, String> mTitleCol = UIHelper.col("Movie", 300);
        mTitleCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getKey()));

        Map<String, Integer> ticketsByMovie = sm.getTicketsByMovie();
        TableColumn<Map.Entry<String, Double>, String> mTicketsCol = UIHelper.col("Tickets Sold", 130);
        mTicketsCol.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(
                        ticketsByMovie.getOrDefault(data.getValue().getKey(), 0))));

        TableColumn<Map.Entry<String, Double>, String> mRevCol = UIHelper.col("Revenue (PHP)", 160);
        mRevCol.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("PHP %.2f", data.getValue().getValue())));

        movieTable.getColumns().addAll(mTitleCol, mTicketsCol, mRevCol);

        javafx.collections.ObservableList<Map.Entry<String, Double>> movieEntries =
                javafx.collections.FXCollections.observableArrayList(
                        sm.getSalesByMovie().entrySet());
        movieTable.setItems(movieEntries);

        if (movieEntries.isEmpty()) {
            movieTable.setPlaceholder(UIHelper.lbl("No sales recorded yet.", UIHelper.TEXT2, 13, false));
        }

        // ── Transaction history ────────────────────────────────────────────────
        Label txSec = UIHelper.sectionLbl("Transaction History");
        VBox.setMargin(txSec, new Insets(8, 0, 4, 0));

        TableView<Sale> txTable = UIHelper.table();
        VBox.setVgrow(txTable, Priority.ALWAYS);
        txTable.setMinHeight(200);

        TableColumn<Sale, Integer> txIdCol = UIHelper.col("ID", 50);
        txIdCol.setCellValueFactory(new PropertyValueFactory<>("saleId"));

        TableColumn<Sale, String> txMovieCol = UIHelper.col("Movie", 200);
        txMovieCol.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));

        TableColumn<Sale, String> txSeatsCol = UIHelper.col("Seats", 140);
        txSeatsCol.setCellValueFactory(new PropertyValueFactory<>("seatsDisplay"));

        TableColumn<Sale, Integer> txQtyCol = UIHelper.col("Qty", 50);
        txQtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Sale, String> txAmtCol = UIHelper.col("Amount (PHP)", 120);
        txAmtCol.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("PHP %.2f", data.getValue().getAmount())));

        TableColumn<Sale, String> txCashierCol = UIHelper.col("Cashier", 140);
        txCashierCol.setCellValueFactory(new PropertyValueFactory<>("cashierName"));

        TableColumn<Sale, String> txDateCol = UIHelper.col("Date & Time", 180);
        txDateCol.setCellValueFactory(new PropertyValueFactory<>("dateTimeFormatted"));

        txTable.getColumns().addAll(txIdCol, txMovieCol, txSeatsCol, txQtyCol,
                txAmtCol, txCashierCol, txDateCol);
        txTable.setItems(sm.getSales());

        content.getChildren().addAll(statsRow, movSec, movieTable, txSec, txTable);
        scroll.setContent(content);
        root.setCenter(scroll);
        return new Scene(root, 1280, 760);
    }
}