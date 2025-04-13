package src;

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import src.backend.ConnectH2;
import src.handlers.WindowHandler;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException, SQLException {

        // Uncomment when a new DB needs to be generated
        ConnectH2.initDatabase();

        var task = new InitTask();

        task.setOnSucceeded(_ -> WindowHandler.initWindows(stage));
        task.setOnFailed(_ -> {
            var alert = new Alert(AlertType.WARNING);
            alert.initOwner(stage);
            alert.setTitle("ERROR");
            alert.setHeaderText("Failed to start the app.");
            alert.setContentText("Please check the log file for further information.");
            alert.showAndWait();

            Platform.exit();
        });

        // Allows the InitTask to run while the app starts up
        var thread = new Thread(task, "init-thread");
        thread.setDaemon(true);
        thread.start();

        // Creates the loading screen scene
        var scene = new Scene(createSplashScreen(task), 600, 400);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }

    private VBox createSplashScreen(InitTask task) {
        Label loadingMessage = new Label("Now Loading...");
        loadingMessage.setTextFill(Color.WHITE);

        // Updates the ProgressBar as the InitTask progresses
        var bar = new ProgressBar();
        bar.progressProperty().bind(task.progressProperty());

        // Creates a VBox to hold the contents of the scene
        VBox vb = new VBox(loadingMessage, bar);
        vb.setAlignment(Pos.CENTER);

        // Sets the background
        BackgroundSize bs = new BackgroundSize(1, 1, true, true, false, false);
        vb.setBackground(new Background(new BackgroundImage(
                new Image(getClass().getResource("/images/Loading_Screen.gif").toString()), null, null, null, bs)));

        return vb;
    }

    public static void main(String[] args) {
        launch();
    }
}