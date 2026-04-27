import javafx.application.Application;
import javafx.stage.Stage;
import views.LoginScreen;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Cinema Ticketing System");
        LoginScreen login = new LoginScreen(stage);
        stage.setScene(login.getScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}