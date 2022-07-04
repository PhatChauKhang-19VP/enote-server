package pck.enote_server.db;

import pck.enote_server.model.Note;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
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
//        System.out.println(signIn("phat6", "phat6"));
//        System.out.println(signUp("phatnguqqq1111", "12345"));
//        System.out.println(addNewNote("phat1", "image/pbg", "url test"));
//        HashMap<Integer, Note> map = new HashMap<Integer, Note>();
//        getNoteList("phat1", map);
//        System.out.println(map);
        Note note = new Note();
        getNote("phat1", 0, note);
        System.out.println(note);
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
             CallableStatement cstmt = conn.prepareCall("call usp_signUp(?, ?, ?, ?)");
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
            cstmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean getNoteList(String username, HashMap<Integer, Note> noteList) {
        try (Connection conn = connect();
             CallableStatement cstmt = conn.prepareCall("select * from users_note where username = ?");
        ) {
            cstmt.setString(1, username);

            boolean result = cstmt.execute();
            if (result) {
                ResultSet rs = cstmt.getResultSet();
                while (rs.next()) {
                    noteList.put(
                            rs.getInt("id"),
                            new Note(
                                    rs.getInt("id"),
                                    rs.getString("type"),
                                    rs.getString("data_uri"),
                                    String.valueOf(rs.getTimestamp("create_at").toLocalDateTime())
                            )
                    );
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean getNote(String username, Integer noteID, Note note) {
        try (
                Connection conn = connect();
                CallableStatement cstmt = conn.prepareCall("select * from users_note where username = ? and id = ?")
        ) {
            cstmt.setString(1, username);
            cstmt.setInt(2, noteID);
            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();
            while (rs.next()) {
                note.setId(rs.getInt("id"));
                note.setType(rs.getString("type"));
                note.setUri(rs.getString("data_uri"));
                note.setCreatedAt(String.valueOf(rs.getTimestamp("create_at").toLocalDateTime()));
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
