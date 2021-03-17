package photoViewer.categoryTree;

import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import photoViewer.Main;
import photoViewer.box.information.AlertBox;
import photoViewer.box.information.InfoBox;
import photoViewer.messagaReceiver.Error;
import photoViewer.messagaReceiver.Message;
import photoViewer.messagaReceiver.RequestType;
import photoViewer.messagaReceiver.Response;
import photoViewer.panel.Panel;

public class CategoryTree extends HBox {
    private final TreeView<String> tree;
    private final TreeItem<String> root;
    private TreeItem<String> selectedCatalog;
    private boolean show = true;

    public CategoryTree() {
        root = new TreeItem<>();
        tree = new TreeView<>(root);
        tree.setShowRoot(false);
        tree.managedProperty().bind(tree.visibleProperty());
        tree.getSelectionModel().selectedItemProperty().addListener(
                (v, oldValue, newValue) -> selectCatalog(newValue));

        Main.messageReceiver.sendMessage(
                new Message(RequestType.GET, "Ogolnodostepne"),
                new Response((r) -> addCategories("Ogolnodostepne", r), Error.NOT_EXIST, "Kategoria nie istnieje")
        );

        Button hide = new Button("⯇");
        hide.setMaxHeight(Double.MAX_VALUE);
        hide.getStyleClass().add("hide");
        hide.setOnAction(e -> {
            if (show) {
                Panel.areaToDisplayImage.refreshArea((int)-tree.getWidth(),0);
                hide.setText("⯈");
            } else {
                Panel.areaToDisplayImage.refreshArea((int)tree.getWidth(),0);
                hide.setText("⯇");
            }
            show = !show;
            tree.setVisible(show);
        });

        getChildren().addAll(tree, hide);
    }

    private void selectCatalog(TreeItem<String> newValue) {
        if(newValue != null) {
            if (newValue.getParent() != root) {
                selectedCatalog = newValue;
                String path = newValue.getParent().getValue() + "/" + newValue.getValue();
                Panel.imageIcons.showIcons(path);
                Panel.userPanel.setDisableButtons(false);
            } else {
                selectedCatalog = null;
                Panel.userPanel.setDisableButtons(true);
            }
            Panel.userPanel.getSave().setDisable(true);
        }
    }

    public void addCategories(String parentTitle, String[] categories) {
        TreeItem<String> parent = addCategory(parentTitle, root);
        makeTree(categories, parent);
    }

    public TreeItem<String> addCategory(String title, TreeItem<String> parent) {
        TreeItem<String> item = new TreeItem<>(title);
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }

    private void makeTree(String[] categories, TreeItem<String> parent){
        for (String category : categories) addCategory(category, parent);
    }

    public void confirmDeletion() {
        String selectedIcon = Panel.imageIcons.getSelectedImageName();
        if(selectedCatalog.getParent().getChildren().size() > 1) {
            InfoBox.display(getSelectedCatalogName(), selectedIcon, "", "Czy chcesz usunąć ten katalog?");
            if(InfoBox.answer) {
                String path = getSelectedParentCatalogName() + "/" + getSelectedCatalogName();
                Response response = new Response();
                response.setErrorMessage(Error.NOT_EXIST, "Kategoria nie istnieje");
                if(selectedIcon.equals("")) {
                    response.setHandlingResponse((r) -> refreshCatalog(getParentIndex(selectedCatalog), r));
                } else {
                    response.setHandlingResponse((r) -> Panel.imageIcons.showIcons(path));
                }
                Main.messageReceiver.sendMessage(new Message(RequestType.DELETE, path + "/" + selectedIcon), response);
            }
        } else {
            AlertBox.display("Nie można usunąć katalogu");
        }
    }

    public void refreshCatalog(int index, String[] categories) {
        String name = root.getChildren().get(index).getValue();
        removeTree(index);
        addCategories(name, categories);
    }

    public void removeTree(int index) {
        if (index <= root.getChildren().size()) {
            tree.getSelectionModel().select(0);
            root.getChildren().remove(index);
        }
    }

    public Boolean categoryExist(TreeItem<String> selectedParent, String name) {
        return selectedParent.getChildren().stream()
                .anyMatch((c) -> c.getValue().equals(name));
    }

    public int getParentIndex(TreeItem<String> catalog) {
        return root.getChildren().indexOf(catalog.getParent());
    }

    public TreeItem<String> getSelectedCatalog() {
        return selectedCatalog;
    }

    public String getSelectedCatalogName() {
        return selectedCatalog.getValue();
    }

    public String getSelectedParentCatalogName() {
        return selectedCatalog.getParent().getValue();
    }

    public String getParentCatalogName(TreeItem<String> catalog) {
        return catalog.getParent().getValue();
    }

    public void setCatalogName(String name, int index) {
        root.getChildren().get(index).setValue(name);
    }
}
