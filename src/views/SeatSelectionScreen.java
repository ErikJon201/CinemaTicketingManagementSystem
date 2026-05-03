package views;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));

        Label info = new Label("Movie: " + showtime.getMovie().getTitle() + " | Time: " + showtime.getTime());

        GridPane seatGrid = new GridPane();
        seatGrid.setHgap(10);
        seatGrid.setVgap(10);
        boolean[][] seats = showtime.getSeats();

        for (int r = 0; r < seats.length; r++) {
            for (int c = 0; c < seats[r].length; c++) {

                Button seatBtn = new Button((char)('A' + r) + "" + (c + 1));
                seatBtn.setPrefSize(50, 50);

                if (seats[r][c]) {
                    seatBtn.setStyle("-fx-background-color: red;");
                    seatBtn.setDisable(true);
                } else {
                    seatBtn.setStyle("-fx-background-color: green;");

                    int row = r;
                    int col = c;

                    seatBtn.setOnAction(e -> {
                        showtime.bookSeat(row, col);
                        seatBtn.setStyle("-fx-background-color: red;");
                        seatBtn.setDisable(true);

                        SalesManager.getInstance().recordSale(
                                showtime.getMovieTitle(),
                                showtime.getPrice()
                        );

                        Alert alert = new Alert(
                                Alert.AlertType.INFORMATION,
                                "Ticket Purchased for " + showtime.getPrice()
                        );
                        alert.show();
                    });
                }

                seatGrid.add(seatBtn, c, r);
            }
        }

        Button backBtn = new Button("Back to Dashboard");
        backBtn.setOnAction(e -> stage.setScene(new CashierDashboard(stage, cashier).getScene()));

        root.getChildren().addAll(info, seatGrid, backBtn);
        return new Scene(root, 800, 500);
    }
}