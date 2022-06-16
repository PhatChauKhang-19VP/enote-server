package pck.enote_server;

import pck.enote_server.cloudinary.CloudAPI;
import pck.enote_server.db.DatabaseCommunication;

public class Main {
    public static void main(String[] args) {
        DatabaseCommunication.init();
        CloudAPI.init();
        Server.main(args);
    }
}
