package pck.enote_server.controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import pck.enote_server.be.server.Client;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientPaneController implements Initializable {
    public Label lblPort;
    public Label lblUsername;
    Client client = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
