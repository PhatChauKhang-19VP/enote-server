package pck.enote_server.be.server;

import javafx.application.Platform;
import pck.enote_server.ServerGUI;
import pck.enote_server.api.API;
import pck.enote_server.api.req.*;
import pck.enote_server.api.res.*;
import pck.enote_server.cloudinary.CloudAPI;
import pck.enote_server.db.DatabaseCommunication;
import pck.enote_server.db.DbQueryResult;
import pck.enote_server.helper.FileHelper;
import pck.enote_server.model.Note;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Worker extends Thread {
    private final Client client;

    public Worker(Client client) {
        this.client = client;
        setDaemon(true);
    }

    //Handle client request and send response to client
    public void run() {
        System.out.println("Processing: " + client.getSocket());

        while (!Server.getServerSocket().isClosed() && client.getSocket() != null && !client.getSocket().isClosed()){
            BaseRes res =  handleClientRequest();
            if (res == null) {
                break;
            }
            System.out.println("Worker.print: " + res);

            //Send response to client
            API.sendRes(client, res);

            System.out.println("worker sent RES and keep looping");
        }
        Server.clients.remove(client.getSocket().getPort());
        Platform.runLater(() -> {
            ServerGUI.addNewReqToList(client, client.toString() + " disconnected");
        });
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
            ServerGUI.addNewReqToList(client, req.toString());
        });

        REQUEST_TYPE reqType = req.getType();

        switch (reqType) {
            case TEST_CONNECTION -> {
                TestConnectionReq testConnReq = (TestConnectionReq) req;
                return new TestConnectionRes(RESPONSE_STATUS.SUCCESS, "Server is now working");
            }

            case SIGN_IN -> {
                SignInReq signInReq = (SignInReq) req;
                DbQueryResult.SignIn signInResult = DatabaseCommunication.signIn(signInReq.getUsername(), signInReq.getPassword());
                if (signInResult.getStatus().equals(DbQueryResult.success)) {
                    //update username in display list
                    Platform.runLater(() -> {
                        client.setUsername(signInReq.getUsername());
                    });

                    return new SignInRes(
                            RESPONSE_STATUS.SUCCESS,
                            signInResult.getMsg()
                    );
                }

                return new SignInRes(
                        RESPONSE_STATUS.FAILED,
                        signInResult.getMsg()
                );
            }
            case SIGN_UP -> {
                SignUpReq signUpReq = (SignUpReq) req;
                DbQueryResult.SignUp signUpResult = DatabaseCommunication.signUp(signUpReq.getUsername(), signUpReq.getPassword());
                if (signUpResult.getStatus().equals(DbQueryResult.success)) {
                    return new SignUpRes(
                            RESPONSE_STATUS.SUCCESS,
                            signUpResult.getMsg()
                    );
                }

                return new SignUpRes(
                        RESPONSE_STATUS.FAILED,
                        signUpResult.getMsg()
                );
            }

            case UPLOAD -> {
                SendFileReq sendFileReq = (SendFileReq) req;
                Map result = CloudAPI.uploadFile(sendFileReq.getFilename(), sendFileReq.getBuffer());


                if (result == null) {
                    return API.getErrorRes();
                }

                new Thread(() -> {
                    String username = client.getUsername();

                    String type = FileHelper.getMimeTypeFromURL((String) result.get("secure_url"));

                    DatabaseCommunication.addNewNote(username, type, (String) result.get("secure_url"));
                }).start();

                return new SendFileRes(
                        RESPONSE_STATUS.SUCCESS,
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