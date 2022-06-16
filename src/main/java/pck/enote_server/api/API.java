package pck.enote_server.api;

import pck.enote_server.api.req.BaseReq;
import pck.enote_server.api.req.REQUEST_TYPE;
import pck.enote_server.api.req.SendFileReq;
import pck.enote_server.api.req.TestConnectionReq;
import pck.enote_server.api.res.BaseRes;
import pck.enote_server.api.res.RESPONSE_STATUS;
import pck.enote_server.api.res.SendFileRes;
import pck.enote_server.api.res.TestConnectionRes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class API {

    public static void main(String[] args) {
    }

    public static BaseReq getClientReq(Socket socket) {
        try {
            DataInputStream dataIn = new DataInputStream(socket.getInputStream());

            // read type
            REQUEST_TYPE reqType = REQUEST_TYPE.valueOf(dataIn.readUTF());
            System.out.println(reqType);

            switch (reqType){
                case TEST_CONNECTION -> {
                    return new TestConnectionReq();
                }
                case UPLOAD -> {
                    // read filename
                    String filename = dataIn.readUTF();
                    System.out.println(filename);

                    //read buffer
                    int length = dataIn.readInt();
                    byte[] buffer = null;
                    if (length > 0) {
                        buffer = new byte[length];
                        dataIn.readFully(buffer, 0, buffer.length);
                    }

                    return new SendFileReq(reqType, filename, buffer);
                }
                default -> {
                    return null;
                }
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean sendRes(Socket socket, BaseRes res){
        try(DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream())) {
            REQUEST_TYPE reqType = res.getType();

            switch (reqType){
                case TEST_CONNECTION -> {
                    TestConnectionRes testConnectionRes = (TestConnectionRes)res;

                    dataOut.writeUTF(testConnectionRes.getType().name());
                    dataOut.writeUTF(testConnectionRes.getStatus().name());
                    dataOut.writeUTF(testConnectionRes.getMsg());

                    return true;
                }
                case UPLOAD -> {
                    SendFileRes sendFileRes = (SendFileRes)res;

                    dataOut.writeUTF(sendFileRes.getType().name());
                    dataOut.writeUTF(sendFileRes.getStatus().name());
                    dataOut.writeUTF(sendFileRes.getMsg());
                    dataOut.writeUTF(sendFileRes.getFileUrl());

                    return true;
                }
                default -> {
                    BaseRes errRes = getErrorRes();

                    dataOut.writeUTF(errRes.getType().name());
                    dataOut.writeUTF(errRes.getStatus().name());
                    dataOut.writeUTF(errRes.getMsg());

                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    public static TestConnectionRes getErrorRes(){
        return new TestConnectionRes(
                RESPONSE_STATUS.FAILED,
                "Server bị lỗi");
    }
}
