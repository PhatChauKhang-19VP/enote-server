package pck.enote_server.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

import static javafx.application.Platform.exit;

public class DatabaseCommunication {
    private static String host = "ec2-52-73-184-24.compute-1.amazonaws.com1";
    private static String dbName = "dkk1h7r7mfhqu1";
    private static String user = "jcbvdwlnjerxkw1";
    private static String port = "5432";
    private static String password = "af0e45f32760eedb64e2ceb91f291826832f9396142eb1fb522f16ef3b46bd081";

    public static void main(String[] args) {
        init();
        System.out.println(signUp("phat6", "phat6"));
        System.out.println(addNewNote("phat1", "image/pbg", "url test"));
    }

    private static String getURL() {
        String urlStringFormat = "jdbc:postgresql://%s:%s/%s?sslmode=require";
        return String.format(urlStringFormat, host, port, dbName);
    }

    public static void init() {
        InputStream inputStream = DatabaseCommunication.class.getResourceAsStream("/config/db.properties");
        Properties props = new Properties();
        try {
            props.load(inputStream);
            host = props.getProperty("DB_HOST", dbName);
            dbName = props.getProperty("DB_NAME", dbName);
            user = props.getProperty("USER", user);
            port = props.getProperty("PORT", port);
            password = props.getProperty("PASSWORD", password);
        } catch (IOException e) {
            System.out.println("CANNOT LOAD CONFIG FILE. PROGRAM WILL SHUT DOWN !!!");
            exit();
        }
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(getURL(), user, password);
    }

    public static boolean login(String username, String password) {
        try (Connection conn = connect();
             PreparedStatement cstmt = conn.prepareStatement("select fn_login(?, ?)");
        ) {
            cstmt.setString(1, username);
            cstmt.setString(2, password);
            cstmt.execute();

            boolean result = cstmt.execute();

            if (result) {
                ResultSet rs = cstmt.getResultSet();

                while (rs.next()) {
                    System.out.println(rs.getString(1));
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean signUp(String username, String password) {
        try (Connection conn = connect();
             CallableStatement cstmt = conn.prepareCall("call usp_signup(?, ?, ?)");
        ) {
            cstmt.setString(1, username);
            cstmt.setString(2, password);
            cstmt.registerOutParameter(3, Types.VARCHAR);

            int row = cstmt.executeUpdate();

            return cstmt.getString(3).equals("true");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addNewNote(String username, String type, String noteUri) {
        try (Connection conn = connect();
             CallableStatement cstmt = conn.prepareCall("call usp_save_note(?, ?, ?)");
        ) {
            cstmt.setString(1, username);
            cstmt.setString(2, type);
            cstmt.setString(3, noteUri);
            cstmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
