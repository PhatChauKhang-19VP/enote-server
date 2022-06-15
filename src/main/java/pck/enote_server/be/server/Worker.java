package pck.enote_server.be.server;

import pck.enote_server.api.helper.StructClass;
import pck.enote_server.api.req.BaseReq;
import pck.enote_server.api.req.REQUEST_TYPE;
import pck.enote_server.api.req.SendFileReq;
import pck.enote_server.api.req.TestConnectionReq;
import pck.enote_server.api.res.RESPONSE_STATUS;
import pck.enote_server.api.res.SendFileRes;
import pck.enote_server.api.res.TestConnectionRes;
import pck.enote_server.cloudinary.CloudAPI;
import pck.enote_server.helper.FileHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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
        REQUEST_TYPE reqType = BaseReq.getReqTypeFromPackedReq(clientReq);
        System.out.println(clientReq);
        switch (reqType) {
            case TEST_CONNECTION -> {
                TestConnectionReq testConnReq = new TestConnectionReq();
                if (testConnReq.initFromHashMap(clientReq)) {
                    return new TestConnectionRes(RESPONSE_STATUS.SUCCESS, "Server is now working", reqType).getPackedRes();
                }
                return new TestConnectionRes(RESPONSE_STATUS.FAILED, "Data not valid", reqType).getPackedRes();
            }
            case LOG_IN -> {
                //handle login
                System.out.println("login");
                return "";
            }
            case UPLOAD -> {
                SendFileReq sendFileReq = new SendFileReq();
                if (sendFileReq.initFromHashMap(clientReq)) {
                    try {
                        File file = FileHelper.getFileFromBuffer(sendFileReq.getBuffer());
                        CloudAPI cloudAPI = new CloudAPI();
                        Map result = cloudAPI.uploadFile(file);

                        System.out.println(result);
                        SendFileRes sendFileRes = new SendFileRes(RESPONSE_STATUS.FAILED, "upload successfully", reqType, "url test");

                        return sendFileRes.getPackedRes();
                    } catch (IOException e) {
                        System.out.println("UPLOAD -> getFileFromBuffer: err");
                        return new TestConnectionRes(RESPONSE_STATUS.FAILED, "Data not valid", reqType).getPackedRes();
                    }
                }
                return new TestConnectionRes(RESPONSE_STATUS.FAILED, "Data not valid", reqType).getPackedRes();
            }
            default -> {
                TestConnectionRes res = new TestConnectionRes(
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