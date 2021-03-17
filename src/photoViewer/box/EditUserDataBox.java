package photoViewer.box;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import photoViewer.Main;
import photoViewer.box.information.AlertBox;
import photoViewer.messagaReceiver.Error;
import photoViewer.messagaReceiver.Message;
import photoViewer.messagaReceiver.RequestType;
import photoViewer.messagaReceiver.Response;
import photoViewer.panel.Panel;

public class EditUserDataBox {
    private final Stage window;

    public EditUserDataBox() {
        window = new Stage();
        window.initModality(Modality.WINDOW_MODAL);
        window.resizableProperty().setValue(Boolean.FALSE);
        window.setTitle("Edycja danych konta");
        window.setMinWidth(400);
        display();
    }

    private void display() {
        Label nickLabel = new Label("Nazwa");
        TextField nickTextField = new TextField();
        nickTextField.setText(Panel.userPanel.getUserName());

        Label newPassLabel = new Label("Nowe hasło");
        PasswordField newPassTextField = new PasswordField();

        Label oldPassLabel = new Label("Stare hasło");
        PasswordField oldPassTextField = new PasswordField();

        Label emptyabel = new Label();

        Button acceptButton = new Button("Edytuj");
        acceptButton.setMaxWidth(Double.MAX_VALUE);
        acceptButton.setOnAction(e -> checkTheCorrectness(nickTextField.getText(), newPassTextField.getText(), oldPassTextField.getText()));

        VBox conteiner = new VBox();
        conteiner.getStyleClass().add("container");
        conteiner.getChildren().addAll(nickLabel, nickTextField, newPassLabel, newPassTextField, oldPassLabel, oldPassTextField,emptyabel, acceptButton);

        Scene scene = new Scene(conteiner);
        scene.getStylesheets().add("resources/css/AddBox.css");
        window.setScene(scene);
        window.show();
    }

    private void checkTheCorrectness(String nick, String newPass, String oldPass){
        if(!oldPass.equals("")) {
            if(!nick.equals("")) {
                Response response = new Response();
                response.setErrorMessage(Error.NOT_UPDATED, "Nazwa i hasło nie zostało zmienione");
                response.setHandlingResponse((r) -> {
                    AlertBox.display("Dane zostały zmienione!");
                    Panel.categoryTree.setCatalogName(nick, 1);
                    Panel.userPanel.setUserName(nick);
                });
                Main.messageReceiver.sendMessage(new Message(RequestType.UPDATE_ACCOUNT, Panel.userPanel.getUserName(), nick, oldPass, newPass), response);
            } else AlertBox.display("Pole nicku nie może być puste!");
        } else AlertBox.display("Pole hasła nie może być puste!");
    }
}
