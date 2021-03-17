package photoViewer.areaToDisplayImage;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class AreaToDisplayImage extends HBox {
    private ImageView selectedImg;

    public AreaToDisplayImage() {
        getChildren().add(getTitle());
        setAlignment(Pos.CENTER);
    }

    private Text getTitle() {
        Text photoViewer = new Text("Photo Viewer");
        photoViewer.setFont(Font.font ("Verdana", 100));
        photoViewer.setFill(Color.WHITE);
        return photoViewer;
    }

    public void setImg(Image img) {
        ImageView newImg = new ImageView(img);
        selectedImg = newImg;
        if(img.getHeight() > getHeight()) newImg.setFitHeight(getHeight());
        if(img.getWidth() > getWidth()) newImg.setFitWidth(getWidth());
        setNode(newImg);
    }

    public void refreshArea(int width, int height) {
        if(selectedImg != null){
            if(selectedImg.getFitWidth() >= getWidth())
                selectedImg.setFitWidth(selectedImg.getFitWidth() - width);
            if(selectedImg.getFitHeight() >= getHeight())
                selectedImg.setFitHeight(selectedImg.getFitHeight() - height);
            setNode(selectedImg);
        }
    }

    public void setNode(Node node){
        getChildren().clear();
        getChildren().add(node != null ? node : getTitle());
    }
}
