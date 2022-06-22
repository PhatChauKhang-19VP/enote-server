package pck.enote_server.db;

public class DbQueryResult {
    public static String success = "success";
    public static String failed = "failed";

    public static class SignIn {
        private String status;
        private String msg;

        public SignIn(String status, String msg) {
            this.status = status;
            this.msg = msg;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        @Override
        public String toString() {
            return "SignIn{" +
                    "status='" + status + '\'' +
                    ", msg='" + msg + '\'' +
                    '}';
        }
    }

    public static class SignUp {
        private String status;
        private String msg;

        public SignUp(String status, String msg) {
            this.status = status;
            this.msg = msg;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        @Override
        public String toString() {
            return "SignUp{" +
                    "status='" + status + '\'' +
                    ", msg='" + msg + '\'' +
                    '}';
        }
    }
}
