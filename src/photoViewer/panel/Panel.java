package photoViewer.panel;

import javafx.scene.layout.BorderPane;
import photoViewer.ImageIcon.ImageIcons;
import photoViewer.areaToDisplayImage.AreaToDisplayImage;
import photoViewer.categoryTree.CategoryTree;
import photoViewer.userPanel.UserPanel;

public class Panel extends BorderPane {
    public static AreaToDisplayImage areaToDisplayImage = new AreaToDisplayImage();
    public static ImageIcons imageIcons = new ImageIcons();
    public static CategoryTree categoryTree = new CategoryTree();
    public static UserPanel userPanel = new UserPanel();

    public Panel() {
        BorderPane borderPaneCenter = new BorderPane();
        borderPaneCenter.getStyleClass().add("borderPaneCenter");
        borderPaneCenter.setCenter(areaToDisplayImage);
        borderPaneCenter.setBottom(imageIcons);

        setLeft(categoryTree);
        setCenter(borderPaneCenter);
        setRight(userPanel);
    }
}
