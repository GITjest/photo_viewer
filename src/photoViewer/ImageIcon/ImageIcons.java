package photoViewer.ImageIcon;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import photoViewer.Main;
import photoViewer.messagaReceiver.Message;
import photoViewer.messagaReceiver.RequestType;
import photoViewer.messagaReceiver.Response;
import photoViewer.panel.Panel;

import java.util.List;

public class ImageIcons extends VBox {
    private final FlowPane flowPane;
    private List<String> listOfNames;
    private String selectedImage;
    private boolean show = true;

    public ImageIcons() {
        flowPane = new FlowPane();
        flowPane.getStyleClass().add("bottomMenu");
        flowPane.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefViewportHeight(200);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(flowPane);

        Button hide = new Button("⯆");
        hide.setMaxWidth(Double.MAX_VALUE);
        hide.setMaxHeight(10);
        hide.getStyleClass().add("hide");
        hide.setOnAction(e -> {
            if (show) {
                Panel.areaToDisplayImage.refreshArea(0,(int) -scrollPane.getHeight());
                hide.setText("⯅");
            } else {
                Panel.areaToDisplayImage.refreshArea(0,(int)scrollPane.getHeight());
                hide.setText("⯆");
            }
            show = !show;
            scrollPane.setVisible(show);
        });

        scrollPane.managedProperty().bind(scrollPane.visibleProperty());

        getChildren().addAll(hide, scrollPane);
    }

    public void addIcons(List<Image> listOfImages, List<String> listOfNames) {
        this.listOfNames = listOfNames;
        flowPane.getChildren().clear();
        for(int i = 0; i < listOfImages.size(); i++) {
            flowPane.getChildren().add(addIcon(listOfImages.get(i), listOfNames.get(i)));
        }
    }

    private Button addIcon(Image img, String tooltip) {
        ImageView imgView = new ImageView(img);
        imgView.setFitHeight(80 / 16 * 9);
        imgView.setFitWidth(80);
        Button button = new Button();
        button.getStyleClass().add("buttons");
        button.setGraphic(imgView);
        button.setTooltip(new Tooltip(tooltip));
        button.setOnAction(e -> {
            Panel.areaToDisplayImage.setImg(img);
            selectedImage = tooltip;
            Panel.userPanel.getSave().setDisable(false);
        });
        return button;
    }

    public void showIcons(String catalogPath) {
        Response response = new Response();
        response.setProgress(this::setProgressBar);
        response.setIcons(this::addIcons);
        Main.messageReceiver.sendMessage(new Message(RequestType.GET_IMAGE, catalogPath), response);
        setProgressBar(0,0);
        selectedImage = "";
    }

    public Boolean nameExist(String name) {
        return listOfNames.stream().anyMatch((n) -> n.substring(0, n.lastIndexOf('.')).equals(name));
    }

    public void setProgressBar(int progress, int end) {
        flowPane.getChildren().clear();
        ProgressBar pb = new ProgressBar((float)progress/end);
        pb.setPrefWidth(200);
        flowPane.getChildren().add(pb);
    }

    public String getSelectedImageName() {
        return selectedImage;
    }
}
