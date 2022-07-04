package pck.enote_server.api.req;

import pck.enote_server.helper.FileHelper;

import java.io.File;
import java.util.Arrays;

public class SendFileReq extends BaseReq {
    private final String filename;
    private final byte[] buffer;

    public SendFileReq(File file) {
        super(REQUEST_TYPE.UPLOAD);
        filename = file.getName();
        buffer = FileHelper.getFileBuffer(file);
    }

    public SendFileReq(REQUEST_TYPE type, String filename, byte[] buffer) {
        super(type);
        this.filename = filename;
        this.buffer = buffer;
    }

    public String getFilename() {
        return filename;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    @Override
    public String toString() {
        return "SendFileReq{" +
                "type=" + type +
                ", buffer length=" + Arrays.toString(buffer).length() +
                '}';
    }
}