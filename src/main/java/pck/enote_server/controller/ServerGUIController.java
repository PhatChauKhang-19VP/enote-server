package pck.enote_server.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import pck.enote_server.ServerGUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerGUIController implements Initializable {
    public TextField tfIP;
    public Label lblConnectingClient;
    public TextField tfPort;
    public VBox vbClientList;
    public VBox vbClientReqList;

    public void addNewReqToList(String req) {
        try {
            FXMLLoader fxmlLoaderReq = new FXMLLoader();
            fxmlLoaderReq.setLocation(ServerGUI.class.getResource("ClientReqItem.fxml"));
            AnchorPane reqPane = fxmlLoaderReq.load();
            ClientReqItemController ctrlReq = fxmlLoaderReq.getController();
            ctrlReq.reqContent.setText(req);
            System.out.println("size bf: " +  vbClientReqList.getChildren().size());
            vbClientReqList.getChildren().add(0, reqPane);
            System.out.println("size af: " +  vbClientReqList.getChildren().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            for (int i = 0; i < 100; i++) {
                FXMLLoader fxmlLoaderClient = new FXMLLoader();
                fxmlLoaderClient.setLocation(ServerGUI.class.getResource("ClientPane.fxml"));
                AnchorPane clientPane = fxmlLoaderClient.load();
                ClientPaneController ctrl = fxmlLoaderClient.getController();
                ctrl.username.setText("phatdeptrai" + i);
                vbClientList.getChildren().add(0, clientPane);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}