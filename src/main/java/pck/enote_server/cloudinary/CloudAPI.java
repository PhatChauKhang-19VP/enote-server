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
    private static boolean isInit = false;
    private Cloudinary cld;

    public CloudAPI() {
        if (!isInit) {
            InputStream inputStream = getClass().getResourceAsStream("/config/cloudinary.properties");
            Properties props = new Properties();
            try {
                props.load(inputStream);
            } catch (IOException e) {
                System.out.println("CANNOT LOAD CONFIG FILE. PROGRAM WILL SHUT DOWN !!!");
                exit();
            }

            name = props.getProperty("CLOUD_NAME", name);
            apiKey = props.getProperty("CLOUD_API_KEY", apiKey);
            apiSecret = props.getProperty("CLOUD_API_SECRET", apiSecret);
            uploadFolder = props.getProperty("CLOUD_UPLOAD_FOLDER", uploadFolder);

            isInit = true;
        }
        cld = new Cloudinary(getConfig());

        System.out.println(cld);
    }

    public static void main(String[] args) {
        System.out.println(new CloudAPI());
        Cloudinary cld = new Cloudinary();
    }

    private Map<String, String> getConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", name);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        config.put("secure", "true");

        return config;
    }

    private Map getUploadConfig(String resourceType) {
        return ObjectUtils.asMap(
                "public_id", "anh.jpg",
                "folder", uploadFolder,
                "overwrite", true,
                "resource_type", resourceType);
    }

    public Map uploadFile(File file) {
        try {
            Map m = cld.uploader().upload(file, getUploadConfig("raw"));
            System.out.println(m);
            return m;
        } catch (IOException e) {
            e.printStackTrace();
            return ObjectUtils.emptyMap();
        }
    }
}
