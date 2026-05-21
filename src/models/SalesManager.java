package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SalesManager {
    private static SalesManager instance;
    private ObservableList<Sale> sales = FXCollections.observableArrayList();

    private SalesManager() {}

    public static SalesManager getInstance() {
        if (instance == null) instance = new SalesManager();
        return instance;
    }

    public void recordSale(String movieTitle, double totalAmount,
                           String cashierName, List<String> seatLabels) {
        sales.add(new Sale(movieTitle, totalAmount, cashierName, seatLabels));
    }

    public ObservableList<Sale> getSales() { return sales; }

    // ── Analytics ─────────────────────────────────────────────────────────────

    public double getTotalRevenue() {
        return sales.stream().mapToDouble(Sale::getAmount).sum();
    }

    public int getTotalTicketsSold() {
        return sales.stream().mapToInt(Sale::getQuantity).sum();
    }

    public double getRevenueToday() {
        return sales.stream().filter(Sale::isToday)
                .mapToDouble(Sale::getAmount).sum();
    }

    public int getTicketsSoldToday() {
        return sales.stream().filter(Sale::isToday)
                .mapToInt(Sale::getQuantity).sum();
    }

    public int getTransactionCount() { return sales.size(); }

    public Map<String, Double> getSalesByMovie() {
        return sales.stream().collect(Collectors.groupingBy(
                Sale::getMovieTitle,
                Collectors.summingDouble(Sale::getAmount)));
    }

    public Map<String, Integer> getTicketsByMovie() {
        return sales.stream().collect(Collectors.groupingBy(
                Sale::getMovieTitle,
                Collectors.summingInt(Sale::getQuantity)));
    }
}