package views;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
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
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label label = new Label("Select a Showtime:");
        ListView<Showtime> listView = new ListView<>();

        // Load showtimes from CinemaManager
        listView.getItems().addAll(CinemaManager.getInstance().getShowtimes());

        // Custom Cell Factory to show Movie Title and Time
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Showtime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(item.getMovie().getTitle() + " - " + item.getTime() + " (PHP " + item.getPrice() + ")");
            }
        });

        Button nextBtn = new Button("Select Seats");
        nextBtn.setOnAction(e -> {
            Showtime selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                SeatSelectionScreen seatScreen = new SeatSelectionScreen(stage, cashier, selected);
                stage.setScene(seatScreen.getScene());
            }
        });

        root.getChildren().addAll(label, listView, nextBtn);
        return new Scene(root, 800, 500);
    }
}
