package photoViewer.box;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import photoViewer.panel.Panel;

import java.io.File;

public abstract class Box {
    public static File selectedFile;
    public static TreeItem<String> selectedItem;
    public static String selectedImg;
    public Stage window;
    private final String title;

    public Box(String title, Boolean setSelectedImg) {
        this.title = title;
        selectedItem = Panel.categoryTree.getSelectedCatalog();
        selectedImg = Panel.imageIcons.getSelectedImageName();

        window = new Stage();
        window.initModality(Modality.WINDOW_MODAL);
        window.resizableProperty().setValue(Boolean.FALSE);
        window.setTitle(title);
        window.setMinWidth(400);

        display(setSelectedImg);
    }

    public void display(Boolean setSelectedImg) {
        Label categoryLabel = new Label("Kategoria");
        TextField categoryTextField = new TextField();
        categoryTextField.setText(selectedItem.getValue());

        Label fileNameLabel = new Label("Nazwa");
        TextField fileNameTextField = new TextField();

        Label pathFileLabel = new Label();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                //  new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg")
        );

        Button fileChooserButton = new Button("Wybierz plik");

        if (setSelectedImg) {
            if (selectedImg.equals("")) {
                fileNameTextField.setDisable(true);
                fileChooserButton.setDisable(true);
            } else fileNameTextField.setText(selectedImg.substring(0, selectedImg.lastIndexOf('.')));
        }

        fileChooserButton.setMaxWidth(Double.MAX_VALUE);
        fileChooserButton.setOnAction(e -> {
            selectedFile = fileChooser.showOpenDialog(window);
            if (selectedFile != null) {
                pathFileLabel.setText(selectedFile.getPath());
            }
        });

        Button acceptButton = new Button(title);
        acceptButton.setMaxWidth(Double.MAX_VALUE);
        acceptButton.setOnAction(e -> checkTheCorrectness(categoryTextField.getText(), fileNameTextField.getText(), pathFileLabel.getText()));

        Button closeButton = new Button("Zamknij");
        closeButton.setMaxWidth(Double.MAX_VALUE);
        closeButton.setOnAction(e -> window.close());

        VBox buttons = new VBox();
        buttons.getStyleClass().add("buttons");
        buttons.getChildren().addAll(fileChooserButton, acceptButton, closeButton);

        VBox container = new VBox();
        container.getStyleClass().add("container");
        container.getChildren().addAll(categoryLabel, categoryTextField, fileNameLabel, fileNameTextField, pathFileLabel, buttons);

        Scene scene = new Scene(container);
        scene.getStylesheets().add("resources/css/AddBox.css");
        window.setScene(scene);
        window.show();
    }

    abstract void checkTheCorrectness(String category, String name, String path);

}
