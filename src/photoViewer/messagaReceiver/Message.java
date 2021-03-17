package photoViewer.messagaReceiver;

import java.io.File;

public class Message {
    private RequestType requestType;
    private String[] message;
    private File file;

    public Message() {
    }

    public Message(RequestType requestType, String... message) {
        this.requestType = requestType;
        this.message = message;

    }

    public Message(RequestType requestType, File file, String... message) {
        this.requestType = requestType;
        this.file = file;
        this.message = message;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public String[] getMessage() {
        return message;
    }

    public void setMessage(String... message) {
        this.message = message;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
