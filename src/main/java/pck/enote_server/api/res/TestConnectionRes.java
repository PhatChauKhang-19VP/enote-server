package pck.enote_server.api.res;


import pck.enote_server.api.req.REQUEST_TYPE;

public class TestConnectionRes extends BaseRes {
    public TestConnectionRes(RESPONSE_STATUS status, String msg) {
        super(status, msg, REQUEST_TYPE.TEST_CONNECTION);
    }
}
