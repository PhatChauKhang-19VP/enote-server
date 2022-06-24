package pck.enote_server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pck.enote_server.controller.ServerGUIController;
import pck.enote_server.helper.ScreenConfig;

import java.io.IOException;

public class ServerGUI extends Application {
    public static Stage stage = null;
    public static FXMLLoader fxmlLoader = null;

    public static void main(String[] args) {
        launch(args);
    }

    public static void addNewReqToList(String req) {
        //get ServerGUI controller
        ServerGUIController serverGUICtrl = fxmlLoader.getController();
        serverGUICtrl.addNewReqToList(req);

    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        fxmlLoader = new FXMLLoader(ServerGUI.class.getResource("ServerGUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), ScreenConfig.getWidth(), ScreenConfig.getHeight());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}