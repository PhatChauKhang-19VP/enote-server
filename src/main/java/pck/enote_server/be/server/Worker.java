package pck.enote_server.be.server;

import pck.enote_server.api.helper.REQUEST_TYPE;
import pck.enote_server.api.helper.StructClass;
import pck.enote_server.api.res.BaseRes;
import pck.enote_server.api.res.RESPONSE_STATUS;
import pck.enote_server.api.res.TestConnectionRes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class Worker extends Thread {
    private final Socket socket;

    public Worker(Socket socket) {
        this.socket = socket;
    }

    private String receiveClientReq(Socket socket) {
        try {
            DataInputStream dataIn = new DataInputStream(socket.getInputStream());

            String packedClientReq = dataIn.readUTF();

            return packedClientReq;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String handleClientRequest(String packedClientReq) {

        HashMap<String, String> clientReq = StructClass.unpack(packedClientReq);
        System.out.println(clientReq);
        BaseRes res = new BaseRes(RESPONSE_STATUS.SUCCESS, "Server OK", REQUEST_TYPE.TEST_CONNECTION);
        switch (REQUEST_TYPE.valueOf(clientReq.get("type"))) {
            case TEST_CONNECTION -> {
                res = new TestConnectionRes(
                        RESPONSE_STATUS.SUCCESS,
                        "Kết nối thành công đến server",
                        REQUEST_TYPE.TEST_CONNECTION);
                return ((TestConnectionRes) res).getPackedRes();
            }
            case LOG_IN -> {
                //handle login
                System.out.println("login");
                return "";
            }
            default -> {
                res = new TestConnectionRes(
                        RESPONSE_STATUS.FAILED,
                        "Server bị lỗi",
                        REQUEST_TYPE.TEST_CONNECTION);
                return ((TestConnectionRes) res).getPackedRes();
            }
        }
    }

    private void sendResponse(Socket socket, String packedRes) {
        try {
            System.out.println();

            DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
            dataOut.writeUTF(packedRes);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Handle client request and send response to client
    public void run() {
        System.out.println("Processing: " + socket);

        //Receive request from client
        String requestData = receiveClientReq(socket);

        //Process request from client: tra cuu so du, thanh toan,...
        String rawResponse = handleClientRequest(requestData);

        //Send response to client
        sendResponse(socket, rawResponse);
        System.out.println("Complete processing: " + socket);
    }
}