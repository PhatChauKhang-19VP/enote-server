package pck.enote_server.api.res;

import pck.enote_server.api.req.REQUEST_TYPE;

public class SignInRes extends BaseRes {
    public SignInRes(RESPONSE_STATUS status, String msg) {
        super(status, msg, REQUEST_TYPE.SIGN_IN);
    }

    @Override
    public String toString() {
        return "SignInRes{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", type=" + type +
                '}';
    }
}
