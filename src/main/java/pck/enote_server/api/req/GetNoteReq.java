package pck.enote_server.api.req;


public class GetNoteReq extends BaseReq {
    private String username;
    private Integer noteId;

    public GetNoteReq(String username, Integer noteId) {
        super(REQUEST_TYPE.GET_NOTE);
        this.noteId = noteId;
        this.username = username;
    }

    public Integer getNoteId() {
        return noteId;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "GetNoteReq{" +
                "type=" + type +
                ", noteId=" + noteId +
                '}';
    }
}
