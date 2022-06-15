package pck.enote_server.api.res.req;

import pck.enote_server.api.helper.StructClass;
import pck.enote_server.helper.FileHelper;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class SendFileReq extends BaseReq {
    private byte[] buffer;

    public SendFileReq() {
        super(REQUEST_TYPE.UPLOAD);
    }

    public SendFileReq(File file) {
        super(REQUEST_TYPE.UPLOAD);
        buffer = FileHelper.getFileBuffer(file);
    }

    public SendFileReq(String packedReq) {
        super(REQUEST_TYPE.UPLOAD);
        HashMap<String, String> reqData = StructClass.unpack(packedReq);
        buffer = reqData.get("buffer").getBytes();
    }

    @Override
    public boolean initFromPackedReq(String packedReq) {
        type = REQUEST_TYPE.UPLOAD;
        HashMap<String, String> reqData = StructClass.unpack(packedReq);
        try {
            buffer = reqData.get("buffer").getBytes();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean initFromHashMap(HashMap<String, String> reqData) {
        type = REQUEST_TYPE.UPLOAD;
        try {
            buffer = reqData.get("buffer").getBytes();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public String getPackedReq() {
        HashMap<String, String> reqData = new HashMap<>();
        reqData.put("type", type.name());
        reqData.put("buffer", new String(buffer, StandardCharsets.UTF_8));

        String packedReq = StructClass.pack(reqData);

        return packedReq;
    }

    public byte[] getBuffer() {
        return buffer;
    }
}
