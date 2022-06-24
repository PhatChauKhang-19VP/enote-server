package pck.enote_server;

import pck.enote_server.cloudinary.CloudAPI;
import pck.enote_server.db.DatabaseCommunication;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        DatabaseCommunication.init();
        CloudAPI.init();
        new Thread(() -> {
            try {
                pck.enote_server.be.server.Server.main();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        ServerGUI.main(args);
    }
}
