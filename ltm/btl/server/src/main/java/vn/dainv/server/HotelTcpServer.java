package vn.dainv.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class HotelTcpServer {
    private final HotelService service = new HotelService();
    private final ExecutorService clientPool = Executors.newCachedThreadPool();
    private final Consumer<String> logger;

    private ServerSocket serverSocket;
    private Thread acceptThread;
    private volatile boolean running;

    public HotelTcpServer(Consumer<String> logger) {
        this.logger = logger;
    }

    public synchronized void start(int port) throws IOException {
        if (running) {
            throw new IllegalStateException("Server da dang chay");
        }
        serverSocket = new ServerSocket(port);
        running = true;
        acceptThread = new Thread(this::acceptLoop, "hotel-server-accept");
        acceptThread.setDaemon(true);
        acceptThread.start();
        log("Server dang lang nghe tai cong " + port);
    }

    public synchronized void stop() {
        running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException ex) {
                log("Loi dong server: " + ex.getMessage());
            }
        }
        clientPool.shutdownNow();
        log("Server da dung");
    }

    private void acceptLoop() {
        while (running) {
            try {
                Socket client = serverSocket.accept();
                log("Client ket noi: " + client.getRemoteSocketAddress());
                clientPool.submit(() -> handleClient(client));
            } catch (IOException ex) {
                if (running) {
                    log("Loi chap nhan ket noi: " + ex.getMessage());
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
            log("Loi xu ly client: " + ex.getMessage());
        } finally {
            log("Client da ngat ket noi");
        }
    }

    private String processRequest(String line) {
        List<String> parts = ProtocolUtils.split(line, "\\|");
        if (parts.isEmpty()) {
            return error("Yeu cau rong");
        }
        String command = parts.get(0);
        try {
            return switch (command) {
                case "ADD_HOTEL" -> addHotel(parts);
                case "UPDATE_HOTEL" -> updateHotel(parts);
                case "DELETE_HOTEL" -> deleteHotel(parts);
                case "LIST_HOTELS" -> listHotels();
                case "ADD_ROOM" -> addRoom(parts);
                case "UPDATE_ROOM" -> updateRoom(parts);
                case "DELETE_ROOM" -> deleteRoom(parts);
                case "LIST_ROOMS" -> listRooms(parts);
                case "SEARCH_ROOMS" -> searchRooms(parts);
                default -> error("Lenh khong hop le");
            };
        } catch (Exception ex) {
            return error("Xu ly that bai: " + ex.getMessage());
        }
    }

    private String addHotel(List<String> p) {
        String id = ProtocolUtils.decode(p.get(1));
        String name = ProtocolUtils.decode(p.get(2));
        int stars = Integer.parseInt(ProtocolUtils.decode(p.get(3)));
        String desc = ProtocolUtils.decode(p.get(4));
        String error = service.addHotel(id, name, stars, desc);
        return error == null ? ok("Them khach san thanh cong", "") : error(error);
    }

    private String updateHotel(List<String> p) {
        String id = ProtocolUtils.decode(p.get(1));
        String name = ProtocolUtils.decode(p.get(2));
        int stars = Integer.parseInt(ProtocolUtils.decode(p.get(3)));
        String desc = ProtocolUtils.decode(p.get(4));
        String error = service.updateHotel(id, name, stars, desc);
        return error == null ? ok("Sua khach san thanh cong", "") : error(error);
    }

    private String deleteHotel(List<String> p) {
        String id = ProtocolUtils.decode(p.get(1));
        String error = service.deleteHotel(id);
        return error == null ? ok("Xoa khach san thanh cong", "") : error(error);
    }

    private String listHotels() {
        String payload = service.listHotels().stream()
                .map(h -> String.join(",",
                        ProtocolUtils.encode(h.getId()),
                        ProtocolUtils.encode(h.getName()),
                        ProtocolUtils.encode(String.valueOf(h.getStars())),
                        ProtocolUtils.encode(h.getDescription())))
                .collect(Collectors.joining(";"));
        return ok("Lay danh sach khach san thanh cong", payload);
    }

    private String addRoom(List<String> p) {
        String hotelId = ProtocolUtils.decode(p.get(1));
        String roomId = ProtocolUtils.decode(p.get(2));
        String type = ProtocolUtils.decode(p.get(3));
        double price = Double.parseDouble(ProtocolUtils.decode(p.get(4)));
        String error = service.addRoom(hotelId, roomId, type, price);
        return error == null ? ok("Them phong thanh cong", "") : error(error);
    }

    private String updateRoom(List<String> p) {
        String hotelId = ProtocolUtils.decode(p.get(1));
        String roomId = ProtocolUtils.decode(p.get(2));
        String type = ProtocolUtils.decode(p.get(3));
        double price = Double.parseDouble(ProtocolUtils.decode(p.get(4)));
        String error = service.updateRoom(hotelId, roomId, type, price);
        return error == null ? ok("Sua phong thanh cong", "") : error(error);
    }

    private String deleteRoom(List<String> p) {
        String hotelId = ProtocolUtils.decode(p.get(1));
        String roomId = ProtocolUtils.decode(p.get(2));
        String error = service.deleteRoom(hotelId, roomId);
        return error == null ? ok("Xoa phong thanh cong", "") : error(error);
    }

    private String listRooms(List<String> p) {
        String hotelId = ProtocolUtils.decode(p.get(1));
        String payload = service.listRooms(hotelId).stream()
                .map(r -> String.join(",",
                        ProtocolUtils.encode(r.getHotelId()),
                        ProtocolUtils.encode(r.getRoomId()),
                        ProtocolUtils.encode(r.getType()),
                        ProtocolUtils.encode(String.valueOf(r.getPrice()))))
                .collect(Collectors.joining(";"));
        return ok("Lay danh sach phong thanh cong", payload);
    }

    private String searchRooms(List<String> p) {
        String keyword = ProtocolUtils.decode(p.get(1));
        double maxPrice = Double.parseDouble(ProtocolUtils.decode(p.get(2)));
        String payload = service.searchRooms(keyword, maxPrice).stream()
                .map(r -> String.join(",",
                        ProtocolUtils.encode(r.getHotelId()),
                        ProtocolUtils.encode(r.getRoomId()),
                        ProtocolUtils.encode(r.getType()),
                        ProtocolUtils.encode(String.valueOf(r.getPrice()))))
                .collect(Collectors.joining(";"));
        return ok("Tim kiem phong thanh cong", payload);
    }

    private String ok(String message, String payload) {
        return "OK|" + ProtocolUtils.encode(message) + "|" + payload;
    }

    private String error(String message) {
        return "ERROR|" + ProtocolUtils.encode(message) + "|";
    }

    private void log(String message) {
        if (logger != null) {
            logger.accept(message);
        }
    }
}
