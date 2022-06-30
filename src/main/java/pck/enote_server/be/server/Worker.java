package pck.enote_server.be.server;

import javafx.application.Platform;
import pck.enote_server.ServerGUI;
import pck.enote_server.api.API;
import pck.enote_server.api.req.*;
import pck.enote_server.api.res.*;
import pck.enote_server.cloudinary.CloudAPI;
import pck.enote_server.db.DatabaseCommunication;
import pck.enote_server.db.DbQueryResult;
import pck.enote_server.model.Note;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Worker extends Thread {
    private final Client client;

    public Worker(Client client) {
        this.client = client;
    }

    //Handle client request and send response to client
    public void run() {
        System.out.println("Processing: " + client.getSocket());

        while (!Server.getServerSocket().isClosed() && client.getSocket() != null && !client.getSocket().isClosed()){
            BaseRes res =  handleClientRequest();
            if (res == null) {
                break;
            }
            System.out.println(res);

            //Send response to client
            API.sendRes(client, res);

            System.out.println("worker sent RES and keep looping");
        }
        Server.clients.remove(client.getSocket().getPort());
        Platform.runLater(() -> {
            ServerGUI.removeClient(client);
        });
        System.out.println("worker end: " + client.getSocket());
    }

    private BaseRes handleClientRequest() {
        BaseReq req = API.getClientReq(client);

        System.out.println("Worker.getClientReq = " + req);

        if (req == null) {
            return null;
        }

        Platform.runLater(() -> {
            ServerGUI.addNewReqToList(req.toString());
        });

        REQUEST_TYPE reqType = req.getType();

        switch (reqType) {
            case TEST_CONNECTION -> {
                TestConnectionReq testConnReq = (TestConnectionReq) req;
                return new TestConnectionRes(RESPONSE_STATUS.SUCCESS, "Server is now working");
            }

            case SIGN_IN -> {
                SignInReq signInReq = (SignInReq) req;
                if (Objects.equals(DatabaseCommunication.signIn(signInReq.getUsername(), signInReq.getPassword()).getStatus(), DbQueryResult.success)) {
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
            case SIGN_UP -> {
                SignUpReq signUpReq = (SignUpReq) req;

                if (DatabaseCommunication.signUp(signUpReq.getUsername(), signUpReq.getPassword()).getStatus().equals(DbQueryResult.success)) {
                    return new SignUpRes(
                            RESPONSE_STATUS.SUCCESS,
                            "Sign up successfully"
                    );
                }

                return new SignUpRes(
                        RESPONSE_STATUS.FAILED,
                        "Sign up failed"
                );
            }

            case UPLOAD -> {
                SendFileReq sendFileReq = (SendFileReq) req;
                Map result = CloudAPI.uploadFile(sendFileReq.getFilename(), sendFileReq.getBuffer());

                if (result == null) {
                    return API.getErrorRes();
                }

                return new SendFileRes(
                        RESPONSE_STATUS.FAILED,
                        "upload successfully",
                        (String) result.get("secure_url")
                );
            }

            case GET_NOTE_LIST -> {
                GetNoteListReq getNoteListReq = (GetNoteListReq) req;
                HashMap<Integer, Note> noteList = new HashMap<>();
                DatabaseCommunication.getNoteList(getNoteListReq.getUsername(), noteList);

                return new GetNoteListRes(
                        RESPONSE_STATUS.SUCCESS,
                        "retrieve note list successfully",
                        noteList
                );
            }

            case GET_NOTE -> {
                GetNoteReq getNoteReq = (GetNoteReq) req;
                Note note = new Note();
                DatabaseCommunication.getNote(getNoteReq.getUsername(), getNoteReq.getNoteId(), note);
                return new GetNoteRes(RESPONSE_STATUS.SUCCESS, "retrieve note successfully", note);
            }

            default -> {
                return API.getErrorRes();
            }
        }
    }
}