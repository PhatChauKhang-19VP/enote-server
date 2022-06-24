package pck.enote_server.be.server;

import javafx.application.Platform;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static final int NUM_OF_THREAD = 64;
    public final static int SERVER_PORT = 7;
    public static HashMap<String, Client> clients = new HashMap<>();

    public static void main() throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_OF_THREAD);
        ServerSocket serverSocket = null;

        try {
            System.out.println("Binding to port " + SERVER_PORT + ", please wait …");
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("ServerGUI started: " + serverSocket);
            System.out.println("Waiting for a client …");

            while (!Platform.isImplicitExit()) {
                try {
                    //Listen to client's connection request
                    Socket socket = serverSocket.accept();
                    Client client = new Client(socket);
                    System.out.println("Number of client: " + clients.size());
                    System.out.println("Client accepted: " + socket);
                    // Handle request from client
                    Worker handler = new Worker(client);
                    executor.execute(handler);
                } catch (IOException e) {
                    System.err.println("Connection Error: " + e);
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }

}