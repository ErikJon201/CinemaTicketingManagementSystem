import javafx.application.Application;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import views.LoginScreen;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Cinema Ticketing System");
        stage.setFullScreenExitHint("");

        stage.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                // Stretch root node to always fill the full scene
                if (newScene.getRoot() instanceof Region root) {
                    root.prefWidthProperty().bind(newScene.widthProperty());
                    root.prefHeightProperty().bind(newScene.heightProperty());
                }
                stage.setFullScreen(true);
            }
        });

        LoginScreen login = new LoginScreen(stage);
        stage.setScene(login.getScene());
        stage.setFullScreen(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}