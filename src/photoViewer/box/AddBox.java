package photoViewer.box;

import photoViewer.Main;
import photoViewer.box.information.AlertBox;
import photoViewer.box.information.InfoBox;
import photoViewer.messagaReceiver.Error;
import photoViewer.messagaReceiver.Message;
import photoViewer.messagaReceiver.RequestType;
import photoViewer.messagaReceiver.Response;
import photoViewer.panel.Panel;

public class AddBox extends Box {

    public AddBox() {
        super("Dodaj", false);
    }

    void checkTheCorrectness(String category, String name, String path) {
        if (!category.equals("")) {
            if (path.equals("")) {
                if (!Panel.categoryTree.categoryExist(selectedItem.getParent(), category)) {
                    InfoBox.display(category, name, "", "Czy chcesz dodać kategorie?");
                    if (InfoBox.answer) {
                        Response response = new Response();
                        response.setErrorMessage(Error.EXIST, "Kategoria istnieje");
                        response.setErrorMessage(Error.NOT_CREATED, "Kategoria nie została dodana");
                        response.setHandlingResponse((r) -> Panel.categoryTree.refreshCatalog(Panel.categoryTree.getParentIndex(selectedItem), r));
                        Main.messageReceiver.sendMessage(new Message(RequestType.CREATE, Panel.categoryTree.getParentCatalogName(selectedItem) + "/" + category), response);
                        window.close();
                    }
                } else AlertBox.display("Kategoria o podanej nazwie istnieje!");
            } else setFileNameToSend(category, name);
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
                response.setErrorMessage(Error.EXIST, "Zdjęcie istnieje");
                response.setErrorMessage(Error.NOT_CREATED, "Zdjęcie nie zostało dodane");
                response.setHandlingResponse((r) -> Panel.imageIcons.showIcons(path));
                Main.messageReceiver.sendMessage(new Message(RequestType.CREATE, selectedFile, path + "/" + name), response);
                window.close();
            }
        } else AlertBox.display("Zdjęcie o podanej nazwie istnieje!");
    }
}
