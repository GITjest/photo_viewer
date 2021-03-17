package photoViewer.box.information;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {

    public static void display(String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(message);
        window.setWidth(400);

        Label messageLabel = new Label(message);

        Button okButton = new Button("OK");
        okButton.setOnAction(e -> window.close());
        okButton.setMaxWidth(Double.MAX_VALUE);

        VBox container = new VBox();
        container.getChildren().addAll(messageLabel, okButton);
        container.getStyleClass().add("container");
        container.setAlignment(Pos.CENTER);

        Scene scene = new Scene(container);
        scene.getStylesheets().add("resources/css/AddBox.css");
        window.setScene(scene);
        window.showAndWait();
    }
}
