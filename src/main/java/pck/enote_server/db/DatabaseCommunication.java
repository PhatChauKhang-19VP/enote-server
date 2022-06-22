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
        System.out.println(signIn("phatnguqqq1111", "12345"));

        System.out.println(signUp("phatnguqqq1111", "12345"));
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

    public static DbQueryResult.SignIn signIn(String username, String password) {
        try (Connection conn = connect();
             CallableStatement cstmt = conn.prepareCall("call usp_signIn(?, ?, ?, ?)");
        ) {
            cstmt.setString(1, username);
            cstmt.setString(2, password);
            cstmt.registerOutParameter(3, Types.VARCHAR);
            cstmt.registerOutParameter(4, Types.VARCHAR);
            cstmt.executeUpdate();

            String status = cstmt.getString(3);
            String msg = cstmt.getString(4);

            return new DbQueryResult.SignIn(status, msg);
        } catch (SQLException e) {
            e.printStackTrace();
            return new DbQueryResult.SignIn(DbQueryResult.failed, "Lỗi khi giao tiếp với db");
        }
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(getURL(), user, password);
    }

    private static String getURL() {
        String urlStringFormat = "jdbc:postgresql://%s:%s/%s?sslmode=require";
        return String.format(urlStringFormat, host, port, dbName);
    }

    public static DbQueryResult.SignUp signUp(String username, String password) {
        try (Connection conn = connect();
             CallableStatement cstmt = conn.prepareCall("call usp_signIn(?, ?, ?, ?)");
        ) {
            cstmt.setString(1, username);
            cstmt.setString(2, password);
            cstmt.registerOutParameter(3, Types.VARCHAR);
            cstmt.registerOutParameter(4, Types.VARCHAR);
            cstmt.executeUpdate();

            String status = cstmt.getString(3);
            String msg = cstmt.getString(4);

            return new DbQueryResult.SignUp(status, msg);
        } catch (SQLException e) {
            e.printStackTrace();
            return new DbQueryResult.SignUp(DbQueryResult.failed, "Lỗi khi giao tiếp với db");
        }
    }

    public static boolean addNewNote(String username, String type, String noteUri) {
        try (Connection conn = connect();
             CallableStatement cstmt = conn.prepareCall("call usp_save_note(?, ?, ?)");
        ) {
            cstmt.setString(1, username);
            cstmt.setString(2, type);
            cstmt.setString(3, noteUri);

            return cstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
