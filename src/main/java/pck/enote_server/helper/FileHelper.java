package pck.enote_server.helper;

import java.io.*;

public class FileHelper {
    public static byte[] getFileBuffer(File file) {
        FileInputStream fileInputStream = null;
        byte[] buffer = new byte[(int) file.length()];

        try {
            //Read bytes with InputStream
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(buffer);
            fileInputStream.close();

            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
            return buffer;
        }
    }

    public static File getFileFromBuffer(byte[] buffer) throws IOException {
        File file = new File("tempFile");
        OutputStream os = new FileOutputStream(file);

        os.write(buffer);
        os.close();
        return file;
    }
}
