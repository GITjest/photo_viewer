package photoViewer.userPanel;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import photoViewer.Main;
import photoViewer.box.*;
import photoViewer.box.information.AlertBox;
import photoViewer.box.information.InfoBox;
import photoViewer.messagaReceiver.Error;
import photoViewer.messagaReceiver.Message;
import photoViewer.messagaReceiver.RequestType;
import photoViewer.messagaReceiver.Response;
import photoViewer.panel.Panel;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class UserPanel extends HBox {
    private final Button addButton;
    private final Button editButton;
    private final Button deleteButton;
    private final Button save;
    private final VBox rightMenu;
    private File selectedDirectory;
    private TextField nameInput;
    private PasswordField passInput;
    private Label userName;
    private boolean show = true;

    public UserPanel() {
        rightMenu = new VBox();
        rightMenu.setPrefWidth(248);
        rightMenu.getStyleClass().add("rightMenu");
        rightMenu.setAlignment(Pos.BASELINE_CENTER);
        rightMenu.managedProperty().bind(rightMenu.visibleProperty());

        Button hide = new Button("⯈");
        hide.setMaxHeight(Double.MAX_VALUE);
        hide.getStyleClass().add("hide");
        hide.setOnAction(e -> {
            if (show) {
                Panel.areaToDisplayImage.refreshArea((int) -rightMenu.getWidth(), 0);
                hide.setText("⯇");
            } else {
                Panel.areaToDisplayImage.refreshArea((int) rightMenu.getWidth(), 0);
                hide.setText("⯈");
            }
            show = !show;
            rightMenu.setVisible(show);
        });

        getChildren().addAll(hide, rightMenu);
        addButton = new Button("Dodaj");
        editButton = new Button("Edytuj");
        deleteButton = new Button("Usuń");
        save = new Button("Pobierz zdjęcie");

        createLoginPanel();
    }

    public void createLoginPanel() {
        rightMenu.getChildren().clear();

        Label login = new Label("Zaloguj się");
        login.getStyleClass().add("loginLabel");

        nameInput = new TextField();
        nameInput.setPromptText("login");

        passInput = new PasswordField();
        passInput.setPromptText("haslo");

        Button loginButton = new Button("Zaloguj");
        loginButton.getStyleClass().add("loginButton");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setOnAction(e -> {
            if (!nameInput.getText().equals("") && !passInput.getText().equals("")) {
                Response response = new Response();
                response.setHandlingResponse((r) -> {
                    Panel.categoryTree.addCategories(nameInput.getText(), r);
                    createUserPanel();
                });
                response.setErrorMessage(Error.NOT_EXIST, "Użytkownik o podanej nazwie lub hasle nie istnieje");
                Main.messageReceiver.sendMessage(new Message(RequestType.LOGIN, nameInput.getText(), passInput.getText()), response);
            } else {
                AlertBox.display("Wszystkie pola muszą być wypełnione!");
            }
        });

        Button registrationButton = new Button("Zarejestruj się");
        registrationButton.getStyleClass().add("rejButton");
        registrationButton.setMaxWidth(Double.MAX_VALUE);
        registrationButton.setOnAction(e -> {
            if (!nameInput.getText().equals("") && !passInput.getText().equals("")) {
                Response response = new Response();
                response.setHandlingResponse((r) -> AlertBox.display("Konto zostało utworzone, możesz teraz śię zalogować"));
                response.setErrorMessage(Error.EXIST, "Użytkownik o podanej nazwie istnieje");
                Main.messageReceiver.sendMessage(new Message(RequestType.CREATE_ACCOUNT, nameInput.getText(), passInput.getText()), response);
            } else {
                AlertBox.display("Wszystkie pola muszą być wypełnione!");
            }
        });

        rightMenu.getChildren().addAll(login, nameInput, passInput, loginButton, registrationButton);
    }

    public void createUserPanel() {
        rightMenu.getChildren().clear();
        userName = new Label(nameInput.getText());
        userName.getStyleClass().add("userNameLabel");

        addButton.setDisable(true);
        addButton.setMaxWidth(Double.MAX_VALUE);
        addButton.getStyleClass().add("addButton");
        addButton.setOnAction(e -> new AddBox());

        editButton.setDisable(true);
        editButton.setMaxWidth(Double.MAX_VALUE);
        editButton.getStyleClass().add("editButton");
        editButton.setOnAction(e -> new EditBox());

        deleteButton.setDisable(true);
        deleteButton.setMaxWidth(Double.MAX_VALUE);
        deleteButton.getStyleClass().add("deleteButton");
        deleteButton.setOnAction(e -> Panel.categoryTree.confirmDeletion());

        DirectoryChooser directoryChooser = new DirectoryChooser();
        save.getStyleClass().add("saveButton");
        save.setDisable(true);
        save.setMaxWidth(Double.MAX_VALUE);
        save.setOnAction(e -> {
            selectedDirectory = directoryChooser.showDialog(new Stage());
            if (selectedDirectory != null) {
                String catalogName = Panel.categoryTree.getSelectedCatalogName();
                String imageName = Panel.imageIcons.getSelectedImageName();
                InfoBox.display(catalogName, imageName , selectedDirectory.getPath(), "Zapisać zdjecie?");
                if (InfoBox.answer) {
                    Response response = new Response();
                    response.setProgress((p, end) -> AlertBox.display("Zdjęcie zostało pobrane"));
                    response.setIcons((image, name) -> {
                        try {
                            String extension = name.get(0).substring(name.get(0).lastIndexOf('.') + 1);
                            ImageIO.write(SwingFXUtils.fromFXImage(image.get(0), null), extension, new File(selectedDirectory.getPath() + "/" + name.get(0)));
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                    Main.messageReceiver.sendMessage(new Message(RequestType.GET_IMAGE, Panel.categoryTree.getSelectedParentCatalogName() + "/" + catalogName + "/" + imageName), response);
                }
            }
        });

        Button log = new Button("Wyloguj");
        log.getStyleClass().add("logButton");
        log.setMaxWidth(Double.MAX_VALUE);
        log.setOnAction(e -> logout());

        Button ediUserData = new Button("Zmień dane konta");
        ediUserData.getStyleClass().add("addButton");
        ediUserData.setMaxWidth(Double.MAX_VALUE);
        ediUserData.setOnAction(e -> new EditUserDataBox());

        Button delete = new Button("Usuń konto");
        delete.getStyleClass().add("logButton");
        delete.setMaxWidth(Double.MAX_VALUE);
        delete.setOnAction(e -> {
            InfoBox.display("", userName.getText(), "", "Czy napewno chcesz usunąć konto?");
            if (InfoBox.answer) {
                Response response = new Response();
                response.setHandlingResponse((r) -> {
                    AlertBox.display("Konto zostało usunięte");
                    logout();
                });
                response.setErrorMessage(Error.NOT_REMOVED, "Konto nie zostało usunięte");
                response.setErrorMessage(Error.NOT_EXIST, "Konto nie istnieje");
                Main.messageReceiver.sendMessage(new Message(RequestType.DELETE_ACCOUNT, nameInput.getText()), response);
            }
        });
        rightMenu.getChildren().addAll(userName, addButton, editButton, deleteButton, save, log, ediUserData, delete);
    }

    public void logout() {
        Panel.areaToDisplayImage.setNode(null);
        Panel.categoryTree.removeTree(1);
        nameInput.setText("");
        passInput.setText("");
        createLoginPanel();
    }

    public void setDisableButtons(Boolean set) {
        addButton.setDisable(set);
        editButton.setDisable(set);
        deleteButton.setDisable(set);
    }

    public Button getSave() {
        return save;
    }

    public String getUserName() {
        return userName.getText();
    }

    public void setUserName(String userName) {
        this.userName.setText(userName);
    }
}
