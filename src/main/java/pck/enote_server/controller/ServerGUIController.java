package pck.enote_server.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import pck.enote_server.ServerGUI;
import pck.enote_server.be.server.Client;
import pck.enote_server.be.server.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

public class ServerGUIController implements Initializable {
    public TextField tfIP;
    public Label lblConnectingClient;
    public TextField tfPort;
    public VBox vbClientList;
    public VBox vbClientReqList;

    public LinkedHashMap<Integer, AnchorPane> clientItemList = new LinkedHashMap<>();

    public void addNewReqToList(String req) {
        try {
            FXMLLoader fxmlLoaderReq = new FXMLLoader();
            fxmlLoaderReq.setLocation(ServerGUI.class.getResource("ClientReqItem.fxml"));
            AnchorPane reqPane = fxmlLoaderReq.load();
            ClientReqItemController ctrlReq = fxmlLoaderReq.getController();
            ctrlReq.reqContent.setText(req);
            vbClientReqList.getChildren().add(0, reqPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (Integer key : Server.getClients().keySet()) {
            Client client = Server.getClients().get(key);
            addNewClient(client);
        }

        lblConnectingClient.setText(String.valueOf(clientItemList.size()));

        try {
            InetAddress ip = InetAddress.getLocalHost();
            System.out.println(ip);
            tfIP.setText(String.valueOf(ip.getHostAddress()));
            tfPort.setText(String.valueOf(Server.SERVER_PORT));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        tfPort.setText(String.valueOf(Server.getServerSocket().getLocalPort()));

        //todo: a thread to update client list
    }

    public void addNewClient(Client client) {
        // add new client to client list
        try {
            FXMLLoader fxmlLoaderClient = new FXMLLoader();
            fxmlLoaderClient.setLocation(ServerGUI.class.getResource("ClientPane.fxml"));
            AnchorPane clientPane = fxmlLoaderClient.load();
            ClientPaneController ctrl = fxmlLoaderClient.getController();
            ctrl.username.setText(client.getUsername());

            clientItemList.put(client.getSocket().getPort(), clientPane);

            vbClientList.getChildren().addAll(clientItemList.values());
        } catch (IOException e) {
            e.printStackTrace();
        }
        lblConnectingClient.setText(String.valueOf(clientItemList.size()));
    }

    public void removeClient(Client client) {
        System.out.println("bf rm, size = " + clientItemList.size());
        clientItemList.remove(client.getSocket().getPort());
        System.out.println("af rm, size = " + clientItemList.size());
        vbClientList.getChildren().clear();
        vbClientList.getChildren().addAll(clientItemList.values());
        lblConnectingClient.setText(String.valueOf(clientItemList.size()));
    }
}