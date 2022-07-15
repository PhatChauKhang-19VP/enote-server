package pck.enote_server.api.res;

import pck.enote_server.api.req.REQUEST_TYPE;

public abstract class BaseRes {
    protected RESPONSE_STATUS status;
    protected String msg;
    protected REQUEST_TYPE type;

    public BaseRes(RESPONSE_STATUS status, String msg, REQUEST_TYPE type) {
        this.status = status;
        this.msg = msg;
        this.type = type;
    }

    public RESPONSE_STATUS getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public REQUEST_TYPE getType() {
        return type;
    }

    @Override
    public String toString() {
        return "BaseRes{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", type=" + type +
                '}';
    }
}
