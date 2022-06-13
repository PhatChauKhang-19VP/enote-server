package pck.enote_server;

import pck.enote_server.db.DatabaseCommunication;

public class Main {
    public static void main(String[] args) {
        DatabaseCommunication dc = new DatabaseCommunication();
        Server.main(args);
    }
}
