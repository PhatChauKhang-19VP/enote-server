package pck.enote_server.helper;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static javafx.application.Platform.exit;

public class ScreenConfig {
    private static String width = "800";
    private static String height = "600";
    private static String title = "Enote server by PCK group";
    private static String logoPath = "static/images/PCK-logo.png";

    private static boolean isInit = false;

    public static String getLogoPath() {
        return logoPath;
    }

    private static void init(){
        InputStream inputStream = ScreenConfig.class.getResourceAsStream("/config/screen.properties");
        try {
            Properties props = new Properties();
            props.load(inputStream);

            width = props.getProperty("SCREEN_WIDTH", width);
            height = props.getProperty("SCREEN_HEIGHT", height);
            title = props.getProperty("TITLE", title);
            logoPath = props.getProperty("LOGO_PATH");

        } catch (IOException e) {
            System.out.println("CANNOT LOAD CONFIG FILE. PROGRAM WILL SHUT DOWN !!!");
            exit();
        }
        isInit = true;
    }
    public static int getWidth(){
        if (!isInit) {
            init();
        }
        return Integer.parseInt(width);
    }

    public static int getHeight(){
        if (!isInit) {
            init();
        }
        return Integer.parseInt(height);
    }

    public static String getTitle() {
        return title;
    }
}
