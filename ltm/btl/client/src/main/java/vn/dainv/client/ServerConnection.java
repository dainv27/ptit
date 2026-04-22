package vn.dainv.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ServerConnection {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public void connect(String host, int port) throws IOException {
        close();
        socket = new Socket(host, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
    }

    public void close() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    public ServerResponse send(String command) throws IOException {
        return send(command, Map.of());
    }

    public ServerResponse send(String command, Map<String, Object> data) throws IOException {
        if (socket == null || socket.isClosed()) {
            throw new IOException("Chưa kết nối đến server");
        }
        ObjectNode request = MAPPER.createObjectNode();
        request.put("command", command);
        request.set("data", MAPPER.valueToTree(data == null ? Map.of() : data));

        writer.write(request.toString());
        writer.newLine();
        writer.flush();

        String response = reader.readLine();
        if (response == null) {
            throw new IOException("Server đóng kết nối");
        }
        JsonNode payload = MAPPER.readTree(response);
        boolean ok = payload.path("ok").asBoolean(false);
        String message = payload.path("message").asText("Lỗi không rõ");
        JsonNode dataNode = payload.path("data");
        return new ServerResponse(ok, message, dataNode);
    }
}
