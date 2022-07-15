package pck.enote_server.api;

import pck.enote_server.api.req.*;
import pck.enote_server.api.res.*;
import pck.enote_server.be.server.Client;
import pck.enote_server.helper.FileHelper;
import pck.enote_server.model.Note;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

public class API {

    public static void main(String[] args) {
    }

    public static BaseReq getClientReq(Client client) {
        try {
            DataInputStream dataIn = client.getDataIn();

            // read type
            REQUEST_TYPE reqType = REQUEST_TYPE.valueOf(dataIn.readUTF());
            System.out.println(reqType);

            switch (reqType) {
                case TEST_CONNECTION -> {
                    return new TestConnectionReq();
                }

                case SIGN_IN -> {
                    // read username & password
                    String username = dataIn.readUTF();
                    String password = dataIn.readUTF();

                    return new SignInReq(username, password);
                }

                case SIGN_UP -> {
                    // read username & password
                    String username = dataIn.readUTF();
                    String password = dataIn.readUTF();

                    return new SignUpReq(username, password);
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

                case GET_NOTE_LIST -> {
                    String username = dataIn.readUTF();
                    return new GetNoteListReq(username);
                }

                case GET_NOTE -> {
                    String username = dataIn.readUTF();
                    Integer noteId = dataIn.readInt();
                    return new GetNoteReq(username, noteId);
                }

                default -> {
                    return null;
                }
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean sendRes(Client client, BaseRes res) {
        try {
            DataOutputStream dataOut = client.getDataOut();
            REQUEST_TYPE reqType = res.getType();

            switch (reqType) {
                case TEST_CONNECTION -> {
                    TestConnectionRes testConnectionRes = (TestConnectionRes) res;

                    dataOut.writeUTF(testConnectionRes.getType().name());
                    dataOut.writeUTF(testConnectionRes.getStatus().name());
                    dataOut.writeUTF(testConnectionRes.getMsg());

                    return true;
                }

                case SIGN_IN -> {
                    SignInRes signInRes = (SignInRes) res;
                    dataOut.writeUTF(signInRes.getType().name());
                    dataOut.writeUTF(signInRes.getStatus().name());
                    dataOut.writeUTF(signInRes.getMsg());

                    return true;
                }

                case SIGN_UP -> {
                    SignUpRes signUpRes = (SignUpRes) res;
                    dataOut.writeUTF(signUpRes.getType().name());
                    dataOut.writeUTF(signUpRes.getStatus().name());
                    dataOut.writeUTF(signUpRes.getMsg());

                    return true;
                }

                case UPLOAD -> {
                    SendFileRes sendFileRes = (SendFileRes) res;

                    dataOut.writeUTF(sendFileRes.getType().name());
                    dataOut.writeUTF(sendFileRes.getStatus().name());
                    dataOut.writeUTF(sendFileRes.getMsg());
                    dataOut.writeUTF(sendFileRes.getFileUrl());

                    return true;
                }
                case GET_NOTE_LIST -> {
                    GetNoteListRes getNoteListRes = (GetNoteListRes) res;
                    dataOut.writeUTF(getNoteListRes.getType().name());
                    dataOut.writeUTF(getNoteListRes.getStatus().name());
                    dataOut.writeUTF(getNoteListRes.getMsg());

                    HashMap<Integer, Note> notes = getNoteListRes.getNoteList();
                    dataOut.writeInt(notes.size());
                    for (Integer key : notes.keySet()) {
                        Note note = notes.get(key);
                        dataOut.writeInt(note.getId());
                        dataOut.writeUTF(note.getType());
                        dataOut.writeUTF(note.getUri());
                        dataOut.writeUTF(note.getCreatedAt());
                    }

                    return true;
                }

                case GET_NOTE -> {
                    GetNoteRes getNoteRes = (GetNoteRes) res;
                    dataOut.writeUTF(getNoteRes.getType().name());
                    dataOut.writeUTF(getNoteRes.getStatus().name());
                    dataOut.writeUTF(getNoteRes.getMsg());

                    // write note info
                    Note note = getNoteRes.getNote();
                    dataOut.writeInt(note.getId());
                    dataOut.writeUTF(note.getType());
                    dataOut.writeUTF(note.getUri());
                    dataOut.writeUTF(note.getCreatedAt());

                    byte[] buffer = FileHelper.getFileBufferFromURL(note.getUri());
                    System.out.println(buffer.length);
                    dataOut.writeInt(buffer.length);
                    dataOut.write(buffer);

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

    public static TestConnectionRes getErrorRes() {
        return new TestConnectionRes(
                RESPONSE_STATUS.FAILED,
                "ServerGUI bị lỗi");
    }
}

