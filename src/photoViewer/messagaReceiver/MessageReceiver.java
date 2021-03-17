package photoViewer.messagaReceiver;

import javafx.application.Platform;
import javafx.scene.image.Image;
import photoViewer.box.information.AlertBox;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class MessageReceiver extends Thread {
    private static final Logger LOG = Logger.getLogger(MessageReceiver.class.getName());

    private Socket socket;
    private OutputStream out;
    private InputStream in;
    private BufferedReader br;
    private PrintStream ps;

    private Message message;
    private Response response;

    public MessageReceiver() throws IOException {
        createConnection();
        start();
    }

    private void createConnection() throws IOException {
        LOG.info("Connected To Server ...");
        Socket socket = new Socket("localhost", 8000);
        this.socket = socket;
        this.out = socket.getOutputStream();
        this.in = socket.getInputStream();
        this.br = new BufferedReader(new InputStreamReader(in));
        this.ps = new PrintStream(out);
        LOG.info("Server connected");
    }

    public void run() {
        while (!isInterrupted()) {
            try {
                synchronized (this) {
                    wait();
                    LOG.info("#################################### - MESSAGE - ####################################");
                    send();
                    reply();
                }
            } catch (Exception e) {
                LOG.warning(e.getMessage());
                Platform.runLater(() -> AlertBox.display("Brak połączenia z serwerem"));
                try {
                    createConnection();
                } catch (IOException ioe) {
                    LOG.warning(e.getMessage());
                }
            }
        }
    }

    public synchronized void sendMessage(Message message, Response response) {
        this.message = message;
        this.response = response;
        notify();
    }

    private synchronized void send() {
        LOG.info("Send to server: " + message.getRequestType() + " | " + Arrays.toString(message.getMessage()));
        ps.println(message.getRequestType());
        for(String msg : message.getMessage()) {
            ps.println(msg);
        }
        if(message.getFile() != null) {
            try {
                ByteArrayOutputStream baos = getByteArrayImage(message.getFile());
                ps.println(baos.size());
                out.write(baos.toByteArray());
            } catch (IOException e) {
                ps.println(0);
            }
        }
    }

    private synchronized void reply() throws IOException{
        if (br.readLine().equals("true")) {
            int count = Integer.parseInt(br.readLine());
            if(message.getRequestType().equals(RequestType.GET_IMAGE)) {
                replyImage(count);
            } else {
                String[] result = new String[count];
                for (int i = 0; i < count; i++) {
                    result[i] = br.readLine();
                }
                LOG.info(Arrays.toString(result));
                Platform.runLater(() -> response.getHandlingResponse().accept(result));
            }
        } else {
            String r = br.readLine();
            LOG.warning(r);
            Platform.runLater(() -> response.getHandlingErrors().accept(response.getErrorMessages().get(Error.valueOf(r))));
        }
    }

    private synchronized void replyImage(int count) throws IOException{
        String[] imageNames = new String[count];
        int[] bytes = new int[count];
        LOG.info("Download images (" + count + ")");
        for (int i = 0; i < count; i++) {
            imageNames[i] = br.readLine();
            bytes[i] = Integer.parseInt(br.readLine());
            LOG.info((i + 1) + ": " + imageNames[i] + " (" + bytes[i] + ")");
        }
        List<Image> images = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            for (int j = 0; j < bytes[i]; j++) {
                bos.write(in.read());
            }
            int progress = i + 1;
            Platform.runLater(() -> response.getProgress().accept(progress, count));
            images.add(new Image(new ByteArrayInputStream(bos.toByteArray())));
        }
        Platform.runLater(() -> response.getIcons().accept(images, Arrays.asList(imageNames)));
    }

    private ByteArrayOutputStream getByteArrayImage(File file) throws IOException {
        String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1);
        BufferedImage image = ImageIO.read(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, extension, baos);
        return baos;
    }

    public void closeConnection() {
        try {
            socket.close();
            in.close();
            out.close();
            br.close();
            ps.close();
        } catch (IOException e) {
            LOG.warning(e.getMessage());
        }
    }

}
