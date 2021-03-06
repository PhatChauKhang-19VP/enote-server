package pck.enote_server.api.req;

public class SignUpReq extends BaseReq {
    private String username;
    private String password;

    public SignUpReq(String username, String password) {
        super(REQUEST_TYPE.SIGN_UP);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "SignUpReq{" +
                "type=" + type +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

