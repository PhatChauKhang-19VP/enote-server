package pck.enote_server.api.res;

import pck.enote_server.api.req.REQUEST_TYPE;

public class SignUpRes extends BaseRes {
    public SignUpRes(RESPONSE_STATUS status, String msg) {
        super(status, msg, REQUEST_TYPE.SIGN_UP);
    }

    @Override
    public String toString() {
        return "SignInQueryRes{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", type=" + type +
                '}';
    }
}

