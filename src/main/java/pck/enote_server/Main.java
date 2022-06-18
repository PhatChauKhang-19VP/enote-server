package pck.enote_server;

import pck.enote_server.cloudinary.CloudAPI;
import pck.enote_server.db.DatabaseCommunication;

import java.io.IOException;

import static javafx.application.Platform.exit;

public class Main {
    public static void main(String[] args) throws IOException {
        DatabaseCommunication.init();
        CloudAPI.init();
        Thread t = new Thread(() -> {
            try {
                pck.enote_server.be.server.Server.main(args);
            } catch (IOException e) {
                e.printStackTrace();
                exit();
            }
        });
        t.start();
        Server.main(args);
    }
}
