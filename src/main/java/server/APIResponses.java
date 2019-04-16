package server;

public class APIResponses {

    public static final int successCode = 1;

    public static class MSGResponse {
        private final long status;
        private final String msg;

        public MSGResponse(long status, String msg) {
            this.status = status;
            this.msg = msg;
        }

        public long getStatus() {
            return status;
        }

        public String getMsg() {
            return msg;
        }
    }

    public static  class IDResponse {
        private final String id;

        public IDResponse(String id) {
            this.id = id;
        }

        public String getID() {
            return this.id;
        }
    }

    public static  class TableResponse {
        private final int status;
        private final String table;

        public TableResponse(int status, String table) {
            this.status = status;
            this.table = table;
        }

        public long getStatus() {
            return status;
        }

        public String getTable() {
            return table;
        }
    }

}