package pck.enote_server.be.server;

import pck.enote_server.api.API;
import pck.enote_server.api.req.*;
import pck.enote_server.api.res.*;
import pck.enote_server.cloudinary.CloudAPI;
import pck.enote_server.db.DatabaseCommunication;

import java.net.Socket;
import java.util.Map;

public class Worker extends Thread {
    private final Socket socket;

    public Worker(Socket socket) {
        this.socket = socket;
    }

    private BaseRes handleClientRequest() {
        BaseReq req = API.getClientReq(socket);
        if (req == null) {
            return null;
        }

        REQUEST_TYPE reqType = req.getType();

        switch (reqType) {
            case TEST_CONNECTION -> {
                TestConnectionReq testConnReq = (TestConnectionReq) req;
                return new TestConnectionRes(RESPONSE_STATUS.SUCCESS, "Server is now working");
            }

            case SIGN_IN -> {
                SignInReq signInReq = (SignInReq) req;

                if (DatabaseCommunication.login(signInReq.getUsername(), signInReq.getPassword())) {
                    return new SignInRes(
                            RESPONSE_STATUS.SUCCESS,
                            "Sign in successfully"
                    );
                }

                return new SignInRes(
                        RESPONSE_STATUS.FAILED,
                        "Sign in failed"
                );
            }

            case UPLOAD -> {
                SendFileReq sendFileReq = (SendFileReq) req;
                Map result = CloudAPI.uploadFile(sendFileReq.getFilename(), sendFileReq.getBuffer());

                if (result == null){
                    return API.getErrorRes();
                }

                return new SendFileRes(
                        RESPONSE_STATUS.FAILED,
                        "upload successfully",
                        (String) result.get("secure_url")
                );
            }
            default -> {
                return API.getErrorRes();
            }
        }
    }

    //Handle client request and send response to client
    public void run() {
        System.out.println("Processing: " + socket);

        //
        BaseRes res =  handleClientRequest();

        System.out.println(res);
        //Send response to client
        API.sendRes(socket, res);

        System.out.println("Complete processing: " + socket);
    }
}