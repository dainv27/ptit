package vn.dainv.client;

import com.fasterxml.jackson.databind.JsonNode;

public class ServerResponse {
    private final boolean ok;
    private final String message;
    private final JsonNode data;

    public ServerResponse(boolean ok, String message, JsonNode data) {
        this.ok = ok;
        this.message = message;
        this.data = data;
    }

    public boolean isOk() {
        return ok;
    }

    public String getMessage() {
        return message;
    }

    public JsonNode getData() {
        return data;
    }
}
