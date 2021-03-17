package photoViewer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import photoViewer.box.information.AlertBox;
import photoViewer.messagaReceiver.*;
import photoViewer.panel.Panel;

import java.io.IOException;
import java.util.logging.Logger;

public class Main extends Application {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static MessageReceiver messageReceiver;
    private final int WIDTH = 1440;
    private final int HEIGHT = WIDTH / 16 * 9;

    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");
        try {
            messageReceiver = new MessageReceiver();
            launch(args);
        } catch (IOException e) {
            LOG.warning(e.getMessage());
            Platform.runLater(() -> AlertBox.display("Brak połączenia z serverem"));
        }
    }

    @Override
    public void start(Stage window) {
        window.setTitle("Photo Viewer");
        window.setOnCloseRequest(e -> closeProgram());

        Scene scene = new Scene(new Panel(), WIDTH, HEIGHT);
        scene.getStylesheets().add("resources/css/mainWindow.css");
        window.setScene(scene);
        window.show();
    }

    private void closeProgram() {
        messageReceiver.closeConnection();
        System.exit(0);
    }

}
