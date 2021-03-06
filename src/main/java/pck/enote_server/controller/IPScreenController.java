package pck.enote_server.controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import pck.enote_server.ServerGUI;
import pck.enote_server.be.server.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.ResourceBundle;

public class IPScreenController implements Initializable {
    public TextField ipField;
    public Label ipWarningField;

    public TextField portField;
    public Label portWarningField;

    public Button connectButton;
    public Label statusAlert;

    protected
    String successMessage = String.format("-fx-text-fill: GREEN;");
    String errorMessage = String.format("-fx-text-fill: RED;");
    String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
    String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // init localhost
        try {
            ipField.setText(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            ipField.setText("localhost");
        }
        ipField.setEditable(false);
        ipField.setDisable(true);

        // init port field value
        portField.setText(String.valueOf(Server.SERVER_PORT));

        ipField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches(".*\\s")) {
                newValue = oldValue;
                ipField.setText(newValue);
            } else {
                checkIPValid(newValue);
                statusAlert.setVisible(false);
            }
        });

        portField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches(".*\\s")) {
                newValue = oldValue;
                portField.setText(newValue);
            } else {
                checkPortValid(newValue);
                statusAlert.setVisible(false);
            }
        });
    }

    public boolean checkIPValid(String ip) {
        if (ip.isBlank()) {
            ipField.setStyle(errorStyle);
            ipWarningField.setText("Vui l??ng nh???p ?????a ch??? IP!");
            ipWarningField.setStyle(errorMessage);

            return false;
        }

        String regex = "(\\b25[0-5]|\\b2[0-4][0-9]|\\b[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}";

        if (!ip.matches(regex)) {
            ipField.setStyle(errorStyle);
            ipWarningField.setText("?????a ch??? IP c?? d???ng: (0-255).(0-255).(0.255).(0-255)!");
            ipWarningField.setStyle(errorMessage);

            return false;
        }

        ipField.setStyle(successStyle);
        ipWarningField.setText("");
        ipWarningField.setStyle(successMessage);

        return true;
    }

    public boolean checkPortValid(String port) {
        if (port.isBlank()) {
            portField.setStyle(errorStyle);
            portWarningField.setText("Vui l??ng nh???p s??? c???ng!");
            portWarningField.setStyle(errorMessage);

            return false;
        }

        String regex = "^((6553[0-5])|(655[0-2][0-9])|(65[0-4][0-9]{2})|(6[0-4][0-9]{3})|([1-5][0-9]{4})|([0-5]{0,5})|([0-9]{1,4}))$";

        if (!port.matches(regex)) {
            portField.setStyle(errorStyle);
            portWarningField.setText("S??? c???ng ch??? bao g???m c??c k?? t??? t??? 0-9 v?? n???m trong kho???ng 0 - 65535!");
            portWarningField.setStyle(errorMessage);

            return false;
        }

        portField.setStyle(successStyle);
        portWarningField.setText("");
        portWarningField.setStyle(successMessage);

        return true;
    }

    public void onConnectButtonClicked(ActionEvent ae) {
        if (ae.getSource() == connectButton) {
            if (/*checkIPValid(ipField.getText()) && */ checkPortValid(portField.getText())) {
                Server.SERVER_PORT = Integer.parseInt(portField.getText());

                if (Server.getServerSocket() != null) {
                    try {
                        Server.getServerSocket().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (Server.create()) {
                    ButtonType btnGotoHome = new ButtonType("??i ?????n ServerGUI", ButtonBar.ButtonData.YES);
                    InetAddress ip = null;
                    try {
                        ip = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    assert ip != null;
                    Alert alert = new Alert(
                            Alert.AlertType.INFORMATION,
                            "IP/Port: " + ip.getHostAddress() + "/" + Server.SERVER_PORT,
                            btnGotoHome);
                    alert.setTitle("Th??ng b??o");
                    alert.setHeaderText("T???o socket server th??nh c??ng !");
                    // option != null.
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.isPresent() && option.get().getButtonData() == ButtonBar.ButtonData.YES) {
                        Server.start();
                        ServerGUI.gotoMainScreen();
                    }
                } else {
                    System.out.println("Failed to create server !");
                }
            }
        }

        checkPortValid(portField.getText());
    }
}
