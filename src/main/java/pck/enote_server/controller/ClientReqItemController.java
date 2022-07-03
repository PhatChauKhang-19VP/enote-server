package pck.enote_server.controller;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pck.enote_server.be.server.Client;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ClientReqItemController implements Initializable {
    public Label reqContent;
    public Label lblDate;
    public Label lblTime;
    public Label lblPort;
    public ImageView ivPort;
    public ImageView ivUser;
    public Label lblUsername;

    Property<Client> clientProperty = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LocalDateTime dateTime = LocalDateTime.now();

        lblDate.setText(dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblTime.setText(dateTime.format(DateTimeFormatter.ofPattern("hh:mm:ss")));

        ivUser.setImage(new Image("static/icons/user.png"));

        clientProperty.addListener((observableValue, client, t1) -> {
            lblUsername.setText(clientProperty.getValue().getUsername());
            lblPort.setText(String.valueOf(clientProperty.getValue().getSocket().getPort()));
        });
    }
}
