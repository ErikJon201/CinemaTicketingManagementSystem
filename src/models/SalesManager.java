package models;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    public void recordSale(String title, double price) {
        sales.add(new Sale(title, price));
    }

    public ObservableList<Sale> getSales() { return sales; }

    public Map<String, Double> getSalesByMovie() {
        return sales.stream().collect(Collectors.groupingBy(
                Sale::getMovieTitle,
                Collectors.summingDouble(Sale::getAmount)
        ));
    }
}