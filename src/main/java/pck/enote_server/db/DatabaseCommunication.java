package pck.enote_server.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static javafx.application.Platform.exit;

public class DatabaseCommunication {
    private static String host = "ec2-52-73-184-24.compute-1.amazonaws.com";
    private static String dbName = "dkk1h7r7mfhqu";
    private static String user = "jcbvdwlnjerxkw";
    private static String port = "5432";
    private static String password = "af0e45f32760eedb64e2ceb91f291826832f9396142eb1fb522f16ef3b46bd08";
    private static boolean isInit = false;

    private Connection conn;

    public DatabaseCommunication() {
        if (!isInit) {
            InputStream inputStream = getClass().getResourceAsStream("/config/db.properties");
            Properties props = new Properties();
            try {
                props.load(inputStream);
            } catch (IOException e) {
                System.out.println("CANNOT LOAD CONFIG FILE. PROGRAM WILL SHUT DOWN !!!");
                exit();
            }

            host = props.getProperty("DB_HOST", dbName);
            dbName = props.getProperty("DB_NAME", dbName);
            user = props.getProperty("USER", user);
            port = props.getProperty("PORT", port);
            password = props.getProperty("PASSWORD", password);

            isInit = true;
        }
    }

    public DatabaseCommunication(Connection conn) {
        this.conn = conn;
    }

    private static String getURL() {
        String urlStringFormat = "jdbc:postgresql://%s:%s/%s?sslmode=require";
        return String.format(urlStringFormat, host, port, dbName);
    }

    public static void main(String[] args) {
        DatabaseCommunication dc = new DatabaseCommunication();
        if (dc.connect()) {
            System.out.println("Connect to heroku postgresDB successfully");
        }
    }

    public boolean connect() {
        try {
            conn = DriverManager.getConnection(getURL(), user, password);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
