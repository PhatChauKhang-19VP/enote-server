package pck.enote_server;

import pck.enote_server.cloudinary.CloudAPI;
import pck.enote_server.db.DatabaseCommunication;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        DatabaseCommunication.init();
        CloudAPI.init();
        pck.enote_server.be.server.Server.main(args);
        Server.main(args);
    }
}
