package controllers;

import models.Sale;
import models.SalesManager;
import java.util.Map;
import java.util.stream.Collectors;

public class SalesController {

    public Map<String, Double> getSalesByMovieSorted() {
        return SalesManager.getInstance().getSalesByMovie().entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                java.util.LinkedHashMap::new
            ));
    }

    public double getGrandTotal() {
        return SalesManager.getInstance().getSales()
            .stream()
            .mapToDouble(Sale::getAmount)
            .sum();
    }

    public int getTotalTicketsSold() {
        return SalesManager.getInstance().getSales().size();
    }

    public double getAverageTicketPrice() {
        if (SalesManager.getInstance().getSales().isEmpty()) return 0;
        return getGrandTotal() / getTotalTicketsSold();
    }

    public String getTopMovie() {
        return SalesManager.getInstance().getSalesByMovie().entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("No sales yet");
    }
}