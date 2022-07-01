package pck.enote_server.be.server;

import pck.enote_server.ServerGUI;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static final int NUM_OF_THREAD = 32;
    public static int SERVER_PORT = 7;
    public static LinkedHashMap<Integer, Client> clients = new LinkedHashMap<>();
    public static Thread thread;

    private static ServerSocket serverSocket = null;

    public static LinkedHashMap<Integer, Client> getClients() {
        return clients;
    }

    public static ServerSocket getServerSocket() {
        return serverSocket;
    }

    public static boolean create() {
        if (serverSocket != null && !serverSocket.isClosed()){
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            System.out.println("Binding to port " + SERVER_PORT + ", please wait …");
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("ServerGUI started: " + serverSocket);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void start() {
        thread = new Thread(() -> {
            try {
                System.out.println("Start and wait for clients ...");
                ExecutorService executor = Executors.newFixedThreadPool(NUM_OF_THREAD);
                while (!serverSocket.isClosed()) {
                    try {
                        System.out.println("Listen to client's connection request");
                        //Listen to client's connection request
                        Socket socket = serverSocket.accept();
                        Client client = new Client(socket);
                        clients.put(socket.getPort(), client);

                        ServerGUI.addNewClient(client);

                        System.out.println("Number of client: " + clients.size());
                        System.out.println("Client accepted: " + socket);
                        // Handle request from client
                        Worker handler = new Worker(client);
                        executor.execute(handler);
                    } catch (IOException e) {
                        System.err.println("Connection Error: " + e);
                    }
                }
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public static void stop() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}