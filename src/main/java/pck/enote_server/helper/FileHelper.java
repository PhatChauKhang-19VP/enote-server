package pck.enote_server.helper;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

public class FileHelper {
    public static byte[] getFileBuffer(File file) {
        byte[] buffer = new byte[(int) file.length()];
        try {
            return Files.readAllBytes(file.toPath());
        } catch (Exception e) {
            e.printStackTrace();
            return buffer;
        }
    }

    public static byte[] getFileBufferFromURL(String url_string) {
        try {
            URL url = new URL(url_string);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.connect();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(conn.getInputStream(), baos);

            return baos.toByteArray();
        } catch (IOException e) {
            // Log error and return null, some default or throw a runtime exception
            return null;
        }
    }

    public static String getMimetype(File file) {
        return URLConnection.guessContentTypeFromName(file.getName());
    }

    public static String getMimeTypeFromURL(String url_string){
        try {
            String type = null;
            if (URLConnection.guessContentTypeFromName(url_string) == null ){

                URL u = new URL(url_string);
                URLConnection uc = null;
                uc = u.openConnection();
                type = uc.getContentType();
                System.out.println(type);

                return type;
            }

            return URLConnection.guessContentTypeFromName(url_string);
        } catch (IOException e) {
            e.printStackTrace();
            return "unknown";
        }
    }
}
