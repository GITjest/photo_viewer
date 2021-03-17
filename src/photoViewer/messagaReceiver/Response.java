package photoViewer.messagaReceiver;

import javafx.scene.image.Image;
import photoViewer.box.information.AlertBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Response {
    private Map<Error, String> errorMessages = new HashMap<>();
    private Consumer<String[]> handlingResponse;
    private Consumer<String> handlingErrors;
    private BiConsumer<Integer, Integer> progress;
    private BiConsumer<List<Image>, List<String>> icons;

    public Response() {
        setDefault();
    }

    public Response(Consumer<String[]> handlingResponse) {
        setDefault();
        setDefaultErrorMessages();
        this.handlingResponse = handlingResponse;
    }

    public Response(Consumer<String[]> handlingResponse, Map<Error, String> errorMessages) {
        setDefault();
        this.handlingResponse = handlingResponse;
        this.errorMessages = errorMessages;
    }

    public Response(Consumer<String[]> handlingResponse, Error error, String errorMessage) {
        setDefault();
        setDefaultErrorMessages();
        this.handlingResponse = handlingResponse;
        setErrorMessage(error, errorMessage);
    }

    private void setDefault() {
        setHandlingResponse((r) -> {});
        setHandlingErrors(AlertBox::display);
        setProgress((p, end) -> {});
        setIcons((i, n) -> {});
    }

    private void setDefaultErrorMessages() {
        errorMessages.put(Error.NOT_EXIST, "Nie istnieje");
        errorMessages.put(Error.NOT_REMOVED, "Nie udało sie usunąć");
        errorMessages.put(Error.EXIST, "Istnieje");
        errorMessages.put(Error.NOT_CREATED, "Nie udało sie utworzyć");
        errorMessages.put(Error.NOT_UPDATED, "Nie udało sie edytowć");
    }

    public Map<Error, String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(Map<Error, String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public void setErrorMessage(Error error, String errorMessage) {
        errorMessages.put(error, errorMessage);
    }

    public Consumer<String[]> getHandlingResponse() {
        return handlingResponse;
    }

    public void setHandlingResponse(Consumer<String[]> handlingResponse) {
        this.handlingResponse = handlingResponse;
    }

    public Consumer<String> getHandlingErrors() {
        return handlingErrors;
    }

    public void setHandlingErrors(Consumer<String> handlingErrors) {
        this.handlingErrors = handlingErrors;
    }

    public BiConsumer<Integer, Integer> getProgress() {
        return progress;
    }

    public void setProgress(BiConsumer<Integer, Integer> progress) {
        this.progress = progress;
    }

    public BiConsumer<List<Image>, List<String>> getIcons() {
        return icons;
    }

    public void setIcons(BiConsumer<List<Image>, List<String>> icons) {
        this.icons = icons;
    }
}
