package pck.enote_server.api.res;

import pck.enote_server.api.req.REQUEST_TYPE;
import pck.enote_server.model.Note;

import java.util.HashMap;

public class GetNoteListRes extends BaseRes {
    private final HashMap<Integer, Note> noteList;

    private GetNoteListRes(RESPONSE_STATUS status, String msg) {
        super(status, msg, REQUEST_TYPE.GET_NOTE_LIST);
        noteList = null;
    }

    public GetNoteListRes(RESPONSE_STATUS status, String msg, HashMap<Integer, Note> noteList) {
        super(status, msg, REQUEST_TYPE.GET_NOTE_LIST);
        this.noteList = noteList;
    }

    public HashMap<Integer, Note> getNoteList() {
        return noteList;
    }

    @Override
    public String toString() {
        return "GetNoteListRes{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", type=" + type +
                ", noteList=" + noteList +
                '}';
    }
}

