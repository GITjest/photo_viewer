package photoViewer.box;

import photoViewer.Main;
import photoViewer.box.information.AlertBox;
import photoViewer.box.information.InfoBox;
import photoViewer.messagaReceiver.Error;
import photoViewer.messagaReceiver.Message;
import photoViewer.messagaReceiver.RequestType;
import photoViewer.messagaReceiver.Response;
import photoViewer.panel.Panel;

import java.io.File;


public class EditBox extends Box {

    public EditBox() {
        super("Edytuj", true);
    }

    void checkTheCorrectness(String category, String name, String path) {
        if (!category.equals("")) {
            if (!selectedImg.equals("")) {
                if (path.equals("")) {
                    if (!Panel.imageIcons.nameExist(name)) {
                        name = name + "." + selectedImg.substring(selectedImg.lastIndexOf('.') + 1);

                        InfoBox.display(category, name, "", "Czy akceptujesz podane wartości?");
                        if (InfoBox.answer) {
                            String pathImage = Panel.categoryTree.getParentCatalogName(selectedItem) + "/" + category;
                            Response response = new Response();
                            response.setErrorMessage(Error.NOT_EXIST, "Kategoria nie istnieje");
                            response.setErrorMessage(Error.NOT_UPDATED, "Kategoria nie została zmieniona");
                            response.setHandlingResponse((r) -> Panel.imageIcons.showIcons(pathImage));
                            Main.messageReceiver.sendMessage(
                                    new Message(RequestType.UPDATE, new File(""), pathImage + "/" + selectedImg, pathImage + "/" + name), response);
                            window.close();
                        }
                    } else AlertBox.display("Zdjęcie o podanej nazwie istnieje!");
                } else {
                    setFileNameToSend(category, name);
                }
            } else {
                if (!Panel.categoryTree.categoryExist(selectedItem.getParent(), category)) {
                    InfoBox.display(category, name, "", "Czy chcesz edytować kategorie?");
                    if (InfoBox.answer) {
                        Response response = new Response();
                        response.setErrorMessage(Error.NOT_EXIST, "Zdjęcie nie istnieje");
                        response.setErrorMessage(Error.NOT_UPDATED, "Zdjięcie nie zostało zmienione");
                        response.setHandlingResponse((r) -> Panel.categoryTree.refreshCatalog(Panel.categoryTree.getParentIndex(selectedItem), r));
                        String catalogParent = Panel.categoryTree.getParentCatalogName(selectedItem);
                        Main.messageReceiver.sendMessage(new Message(RequestType.UPDATE, catalogParent + "/" + selectedItem.getValue(), catalogParent + "/" + category), response);
                        window.close();
                    }
                } else AlertBox.display("Kategoria o podanej nazwie istnieje!");
            }
        } else AlertBox.display("Pole kategorii nie może być puste!");
    }

    private void setFileNameToSend(String category, String name) {
        if (!Panel.imageIcons.nameExist(name)) {
            name = name.equals("")
                    ? selectedFile.getName()
                    : name + "." + selectedFile.getName().substring(selectedFile.getName().lastIndexOf('.') + 1);

            InfoBox.display(category, name, selectedFile.getName(), "Czy akceptujesz podane wartości?");
            if (InfoBox.answer) {
                String path = Panel.categoryTree.getParentCatalogName(selectedItem) + "/" + category;
                Response response = new Response();
                response.setErrorMessage(Error.NOT_EXIST, "Zdjęcie nie istnieje");
                response.setErrorMessage(Error.NOT_UPDATED, "Zdjięcie nie zostało zmienione");
                response.setHandlingResponse((r) -> Panel.imageIcons.showIcons(path));
                Main.messageReceiver.sendMessage(new Message(RequestType.UPDATE, selectedFile, path + "/" + selectedImg, path + "/" + name), response);
            }
        } else AlertBox.display("Zdjęcie o podanej nazwie istnieje!");
    }

}

