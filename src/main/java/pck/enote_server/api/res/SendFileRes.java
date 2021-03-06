package pck.enote_server.api.res;

import pck.enote_server.api.req.REQUEST_TYPE;

public class SendFileRes extends BaseRes {
    private final String fileUrl;

    private SendFileRes(RESPONSE_STATUS status, String msg) {
        super(status, msg, REQUEST_TYPE.UPLOAD);
        fileUrl = null;
    }

    public SendFileRes(RESPONSE_STATUS status, String msg, String fileUrl) {
        super(status, msg, REQUEST_TYPE.UPLOAD);
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    @Override
    public String toString() {
        return "SendFileRes{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", type=" + type +
                ", fileUrl='" + fileUrl + '\'' +
                '}';
    }
}