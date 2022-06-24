package pck.enote_server.controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ClientReqItemController implements Initializable {
    public Label reqContent;
    public Label lblDate;
    public Label lblTime;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LocalDateTime dateTime = LocalDateTime.now();

        lblDate.setText(dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblTime.setText(dateTime.format(DateTimeFormatter.ofPattern("hh:mm:ss")));
    }
}
