package pck.enote_server.api.req;

public class TestConnectionReq extends BaseReq {
    public TestConnectionReq() {
        super(REQUEST_TYPE.TEST_CONNECTION);
    }

    @Override
    public String toString() {
        return "TestConnectionReq{" +
                "type=" + type +
                '}';
    }
}
