package photoViewer.box.information;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.scene.control.Label;

public class InfoBox {
    public static Boolean answer;

    public static void display(String category, String name, String path, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.resizableProperty().setValue(Boolean.FALSE);
        window.setTitle(message);
        window.setMinWidth(400);
        window.setOnCloseRequest(e -> answer = false);

        Label categoryLabel = new Label("Kategoria: " + category);
        Label nameLabel = new Label("Nazwa: " + name);
        Label pathLabel = new Label("Nazwa pliku: " + path);
        Label msg = new Label(message);

        Button yesButton = new Button("Tak");
        yesButton.getStyleClass().add("yes-button");
        yesButton.setMaxWidth(Double.MAX_VALUE);
        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });

        Button noButton = new Button("Nie");
        noButton.getStyleClass().add("no-button");
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });
        noButton.setMaxWidth(Double.MAX_VALUE);

        VBox container = new VBox();
        container.getStyleClass().add("container");

        if (!category.equals("")) container.getChildren().add(categoryLabel);
        if (!name.equals("")) container.getChildren().add(nameLabel);
        if (!path.equals("")) container.getChildren().add(pathLabel);
        container.getChildren().addAll(msg, yesButton, noButton);

        Scene scene = new Scene(container);
        scene.getStylesheets().add("resources/css/AddBox.css");
        window.setScene(scene);
        window.showAndWait();
    }

}
