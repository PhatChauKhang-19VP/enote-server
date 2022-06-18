package pck.enote_server.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static javafx.application.Platform.exit;

public class CloudAPI {
    private static String name = "pck-group";
    private static String apiKey = "471685925227765";
    private static String apiSecret = "L--pAliKsFYLbtu2pXa_mAeZQio";
    private static String uploadFolder = "MMT/enote/assets";

    public static void main(String[] args) {
        init();
    }

    private static Map<String, String> getConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", name);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        config.put("secure", "true");

        return config;
    }

    private static Map getUploadConfig(String filename) {
        return ObjectUtils.asMap(
                "public_id", filename,
                "folder", uploadFolder,
                "overwrite", true,
                "resource_type", "auto");
    }

    public static Map uploadFile(File file) {
        try {
            Cloudinary cld = new Cloudinary(getConfig());
            Map uploadResult = cld.uploader().upload(file, getUploadConfig(file.getName()));
            System.out.println(uploadResult);
            return uploadResult;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map uploadFile(String filename, byte[] bytes) {
        try {
            Cloudinary cld = new Cloudinary(getConfig());
            Map uploadResult = cld.uploader().upload(bytes, getUploadConfig(filename));
            System.out.println(uploadResult);
            return uploadResult;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void init() {
        InputStream inputStream = CloudAPI.class.getResourceAsStream("/config/cloudinary.properties");
        Properties props = new Properties();
        try {
            props.load(inputStream);
            name = props.getProperty("CLOUD_NAME", name);
            apiKey = props.getProperty("CLOUD_API_KEY", apiKey);
            apiSecret = props.getProperty("CLOUD_API_SECRET", apiSecret);
            uploadFolder = props.getProperty("CLOUD_UPLOAD_FOLDER", uploadFolder);
        } catch (IOException e) {
            System.out.println("CANNOT LOAD CONFIG FILE. PROGRAM WILL SHUT DOWN !!!");
            exit();
        }
    }
}
