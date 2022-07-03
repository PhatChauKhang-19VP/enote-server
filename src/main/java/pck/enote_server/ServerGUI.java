package pck.enote_server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pck.enote_server.be.server.Client;
import pck.enote_server.be.server.Server;
import pck.enote_server.controller.ServerGUIController;
import pck.enote_server.helper.ScreenConfig;

import static javafx.application.Platform.exit;

public class ServerGUI extends Application {
    public static Stage stage = null;
    public static FXMLLoader fxmlLoader = null;
    public static boolean isRunning = true;

    public static void main(String[] args) {
        launch(args);
    }

    public static void addNewClient(Client client) {
        // update client number
        ServerGUIController serverGUICtrl = fxmlLoader.getController();
        serverGUICtrl.addNewClient(client);
    }

    public static void removeClient(Client client) {
        // update client number
        ServerGUIController serverGUICtrl = fxmlLoader.getController();
        serverGUICtrl.removeClient(client);
    }


    public static void addNewReqToList(Client client, String req) {
        //get ServerGUI controller
        ServerGUIController serverGUICtrl = fxmlLoader.getController();
        serverGUICtrl.addNewReqToList(client, req);
    }

    public static void gotoMainScreen() {
        try {
            replaceSceneContent("ServerGUI.fxml", 800, 600);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Parent replaceSceneContent(String fxml, int width, int height) throws Exception {
        FXMLLoader loader = new FXMLLoader(ServerGUI.class.getResource(fxml), null, new JavaFXBuilderFactory());

        Parent page = loader.load();

        ServerGUI.fxmlLoader = loader;


        Scene scene = new Scene(page, width, height);
        stage.setScene(scene);

        stage.sizeToScene();
        stage.show();
        return page;
    }

    @Override
    public void start(Stage primaryStage) {
        Platform.runLater(() -> {
            stage = primaryStage;
            try {
                stage.getIcons().clear();
                stage.getIcons().add(new Image(ScreenConfig.getLogoPath()));
                stage.setTitle(ScreenConfig.getTitle());
                stage.setResizable(false);
                stage.setFullScreen(false);

                gotoIPScreen();
            } catch (Exception e) {
                e.printStackTrace();
                exit();
            }
        });

    }

    @Override
    public void stop() {
        Server.stop();
    }

    public static void gotoIPScreen() {
        try {
            replaceSceneContent("IPScreen.fxml", 600, 600);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}