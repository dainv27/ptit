package vn.dainv.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class ServerConnection {
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

    public ServerResponse send(String command, String... args) throws IOException {
        if (socket == null || socket.isClosed()) {
            throw new IOException("Chưa kết nối đến server");
        }
        String line = command;
        if (args != null && args.length > 0) {
            line += "|" + String.join("|", args);
        }
        writer.write(line);
        writer.newLine();
        writer.flush();

        String response = reader.readLine();
        if (response == null) {
            throw new IOException("Server đóng kết nối");
        }
        List<String> parts = ProtocolUtils.split(response, "\\|");
        String status = parts.size() > 0 ? parts.get(0) : "ERROR";
        String message = parts.size() > 1 ? ProtocolUtils.decode(parts.get(1)) : "Lỗi không rõ";
        String payload = parts.size() > 2
                ? parts.subList(2, parts.size()).stream().collect(Collectors.joining("|"))
                : "";
        return new ServerResponse("OK".equals(status), message, payload);
    }
}
