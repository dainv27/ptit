package vn.dainv.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class HotelTcpServer {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final DatabaseManager databaseManager = new DatabaseManager();
    private final HotelService service = new HotelService(databaseManager);
    private final ExecutorService clientPool = Executors.newCachedThreadPool();
    private final Consumer<String> logger;

    private ServerSocket serverSocket;
    private volatile boolean running;

    public HotelTcpServer(Consumer<String> logger) {
        this.logger = logger;
    }

    public synchronized void start(int port) throws IOException {
        if (running) {
            throw new IllegalStateException("Server đã đang chạy");
        }
        DatabaseConfig databaseConfig = DatabaseConfig.fromEnv();
        try {
            databaseManager.connect(databaseConfig);
            databaseManager.initializeSchema();
            log("Kết nối database thành công: " + databaseConfig.host() + ":" + databaseConfig.port() + "/" + databaseConfig.database());
        } catch (Exception ex) {
            throw new IOException("Không thể kết nối database: " + ex.getMessage(), ex);
        }
        serverSocket = new ServerSocket(port);
        running = true;
        Thread acceptThread = new Thread(this::acceptLoop, "hotel-server-accept");
        acceptThread.setDaemon(true);
        acceptThread.start();
        log("Server đang lắng nghe tại cổng " + port);
    }

    public synchronized void stop() {
        running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException ex) {
                log("Lỗi đóng server: " + ex.getMessage());
            }
        }
        clientPool.shutdownNow();
        databaseManager.disconnect();
        log("Server đã dừng");
    }

    private void acceptLoop() {
        while (running) {
            try {
                Socket client = serverSocket.accept();
                log("Client kết nối: " + client.getRemoteSocketAddress());
                clientPool.submit(() -> handleClient(client));
            } catch (IOException ex) {
                if (running) {
                    log("Lỗi chấp nhận kết nối: " + ex.getMessage());
                }
            }
        }
    }

    private void handleClient(Socket socket) {
        try (Socket client = socket;
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
             BufferedWriter writer = new BufferedWriter(
                     new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String response = processRequest(line);
                writer.write(response);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException ex) {
            log("Lỗi xử lý client: " + ex.getMessage());
        } finally {
            log("Client đã ngắt kết nối");
        }
    }

    private String processRequest(String line) {
        try {
            JsonNode request = MAPPER.readTree(line);
            String command = getTextFromJson(request, "command");
            JsonNode data = request.path("data");
            if (command.isBlank()) {
                return error("Lệnh không được để trống");
            }
            return switch (command) {
                case "ADD_HOTEL" -> addHotel(data);
                case "UPDATE_HOTEL" -> updateHotel(data);
                case "DELETE_HOTEL" -> deleteHotel(data);
                case "LIST_HOTELS" -> listHotels();
                case "ADD_ROOM" -> addRoom(data);
                case "UPDATE_ROOM" -> updateRoom(data);
                case "DELETE_ROOM" -> deleteRoom(data);
                case "LIST_ROOMS" -> listRooms(data);
                case "SEARCH_ROOMS" -> searchRooms(data);
                default -> error("Lệnh không hợp lệ");
            };
        } catch (IOException ex) {
            return error("Yêu cầu JSON không hợp lệ: " + ex.getMessage());
        } catch (Exception ex) {
            return error("Xử lý thất bại: " + ex.getMessage());
        }
    }

    private String addHotel(JsonNode data) {
        String id = getTextFromJson(data, "id");
        String name = getTextFromJson(data, "name");
        int stars = intValue(data, "stars");
        String desc = getTextFromJson(data, "description");
        String error = service.addHotel(id, name, stars, desc);
        return error == null ? ok("Thêm khách sạn thành công", MAPPER.createObjectNode()) : error(error);
    }

    private String updateHotel(JsonNode data) {
        String id = getTextFromJson(data, "id");
        String name = getTextFromJson(data, "name");
        int stars = intValue(data, "stars");
        String desc = getTextFromJson(data, "description");
        String error = service.updateHotel(id, name, stars, desc);
        return error == null ? ok("Sửa khách sạn thành công", MAPPER.createObjectNode()) : error(error);
    }

    private String deleteHotel(JsonNode data) {
        String id = getTextFromJson(data, "id");
        String error = service.deleteHotel(id);
        return error == null ? ok("Xóa khách sạn thành công", MAPPER.createObjectNode()) : error(error);
    }

    private String listHotels() {
        ArrayNode payload = MAPPER.createArrayNode();
        for (Hotel hotel : service.listHotels()) {
            ObjectNode item = payload.addObject();
            item.put("id", hotel.getId());
            item.put("name", hotel.getName());
            item.put("stars", hotel.getStars());
            item.put("description", hotel.getDescription());
        }
        return ok("Lấy danh sách khách sạn thành công", payload);
    }

    private String addRoom(JsonNode data) {
        String hotelId = getTextFromJson(data, "hotelId");
        String roomId = getTextFromJson(data, "roomId");
        String type = getTextFromJson(data, "type");
        double price = doubleValue(data, "price");
        String error = service.addRoom(hotelId, roomId, type, price);
        return error == null ? ok("Thêm phòng thành công", MAPPER.createObjectNode()) : error(error);
    }

    private String updateRoom(JsonNode data) {
        String hotelId = getTextFromJson(data, "hotelId");
        String roomId = getTextFromJson(data, "roomId");
        String type = getTextFromJson(data, "type");
        double price = doubleValue(data, "price");
        String error = service.updateRoom(hotelId, roomId, type, price);
        return error == null ? ok("Sửa phòng thành công", MAPPER.createObjectNode()) : error(error);
    }

    private String deleteRoom(JsonNode data) {
        String hotelId = getTextFromJson(data, "hotelId");
        String roomId = getTextFromJson(data, "roomId");
        String error = service.deleteRoom(hotelId, roomId);
        return error == null ? ok("Xóa phòng thành công", MAPPER.createObjectNode()) : error(error);
    }

    private String listRooms(JsonNode data) {
        String hotelId = getTextFromJson(data, "hotelId");
        ArrayNode payload = MAPPER.createArrayNode();
        for (Room room : service.listRooms(hotelId)) {
            ObjectNode item = payload.addObject();
            item.put("hotelId", room.getHotelId());
            item.put("roomId", room.getRoomId());
            item.put("type", room.getType());
            item.put("price", room.getPrice());
        }
        return ok("Lấy danh sách phòng thành công", payload);
    }

    private String searchRooms(JsonNode data) {
        String keyword = getTextFromJson(data, "keyword");
        double maxPrice = doubleValue(data, "maxPrice");
        ArrayNode payload = MAPPER.createArrayNode();
        for (Room room : service.searchRooms(keyword, maxPrice)) {
            ObjectNode item = payload.addObject();
            item.put("hotelId", room.getHotelId());
            item.put("roomId", room.getRoomId());
            item.put("type", room.getType());
            item.put("price", room.getPrice());
        }
        return ok("Tìm kiếm phòng thành công", payload);
    }

    private String ok(String message, JsonNode data) {
        return response(true, message, data);
    }

    private String error(String message) {
        return response(false, message, MAPPER.createObjectNode());
    }

    private String response(boolean ok, String message, JsonNode data) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("ok", ok);
        node.put("message", message);
        node.set("data", data == null ? MAPPER.createObjectNode() : data);
        return node.toString();
    }

    private String getTextFromJson(JsonNode data, String field) {
        JsonNode value = data.path(field);
        return value.isMissingNode() || value.isNull() ? "" : value.asText("");
    }

    private int intValue(JsonNode data, String field) {
        JsonNode value = data.path(field);
        if (value.isInt() || value.isLong()) {
            return value.asInt();
        }
        String text = value.asText("");
        if (text.isBlank()) {
            return 0;
        }
        return Integer.parseInt(text);
    }

    private double doubleValue(JsonNode data, String field) {
        JsonNode value = data.path(field);
        if (value.isNumber()) {
            return value.asDouble();
        }
        String text = value.asText("");
        if (text.isBlank()) {
            return 0;
        }
        return Double.parseDouble(text);
    }

    private void log(String message) {
        if (logger != null) {
            logger.accept(message);
        }
    }
}
