package vn.dainv.client;

public class ServerResponse {
    private final boolean ok;
    private final String message;
    private final String payload;

    public ServerResponse(boolean ok, String message, String payload) {
        this.ok = ok;
        this.message = message;
        this.payload = payload;
    }

    public boolean isOk() {
        return ok;
    }

    public String getMessage() {
        return message;
    }

    public String getPayload() {
        return payload;
    }
}
