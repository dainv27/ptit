package vn.dainv.server;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HotelService {
    private final DatabaseManager databaseManager;

    public HotelService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public synchronized String addHotel(String id, String name, int stars, String description) {
        if (id == null || id.isBlank()) {
            return "Mã khách sạn không được để trống";
        }
        String sql = "INSERT INTO hotels(id, name, stars, description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, name);
            ps.setInt(3, stars);
            ps.setString(4, description);
            ps.executeUpdate();
            return null;
        } catch (SQLException ex) {
            if (isDuplicate(ex)) {
                return "Khách sạn đã tồn tại";
            }
            return "Lỗi database: " + ex.getMessage();
        }
    }

    public synchronized String updateHotel(String id, String name, int stars, String description) {
        String sql = "UPDATE hotels SET name = ?, stars = ?, description = ? WHERE id = ?";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, stars);
            ps.setString(3, description);
            ps.setString(4, id);
            int updated = ps.executeUpdate();
            return updated == 0 ? "Không tìm thấy khách sạn" : null;
        } catch (SQLException ex) {
            return "Lỗi database: " + ex.getMessage();
        }
    }

    public synchronized String deleteHotel(String id) {
        String countRoomsSql = "SELECT COUNT(*) FROM rooms WHERE hotel_id = ?";
        try (PreparedStatement ps = connection().prepareStatement(countRoomsSql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return "Chỉ được xóa khách sạn khi đã xóa hết phòng";
                }
            }
        } catch (SQLException ex) {
            return "Lỗi database: " + ex.getMessage();
        }
        String sql = "DELETE FROM hotels WHERE id = ?";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            ps.setString(1, id);
            int deleted = ps.executeUpdate();
            return deleted == 0 ? "Không tìm thấy khách sạn" : null;
        } catch (SQLException ex) {
            return "Lỗi database: " + ex.getMessage();
        }
    }

    public synchronized String addRoom(String hotelId, String roomId, String type, double price) {
        if (!hotelExists(hotelId)) {
            return "Không tìm thấy khách sạn";
        }
        String sql = "INSERT INTO rooms(hotel_id, room_id, type, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            ps.setString(1, hotelId);
            ps.setString(2, roomId);
            ps.setString(3, type);
            ps.setDouble(4, price);
            ps.executeUpdate();
            return null;
        } catch (SQLException ex) {
            if (isDuplicate(ex)) {
                return "Phòng đã tồn tại";
            }
            return "Lỗi database: " + ex.getMessage();
        }
    }

    public synchronized String updateRoom(String hotelId, String roomId, String type, double price) {
        String sql = "UPDATE rooms SET type = ?, price = ? WHERE hotel_id = ? AND room_id = ?";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            ps.setString(1, type);
            ps.setDouble(2, price);
            ps.setString(3, hotelId);
            ps.setString(4, roomId);
            int updated = ps.executeUpdate();
            if (updated > 0) {
                return null;
            }
            if (!hotelExists(hotelId)) {
                return "Không tìm thấy khách sạn";
            }
            return "Không tìm thấy phòng";
        } catch (SQLException ex) {
            return "Lỗi database: " + ex.getMessage();
        }
    }

    public synchronized String deleteRoom(String hotelId, String roomId) {
        String sql = "DELETE FROM rooms WHERE hotel_id = ? AND room_id = ?";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            ps.setString(1, hotelId);
            ps.setString(2, roomId);
            int deleted = ps.executeUpdate();
            if (deleted > 0) {
                return null;
            }
            if (!hotelExists(hotelId)) {
                return "Không tìm thấy khách sạn";
            }
            return "Không tìm thấy phòng";
        } catch (SQLException ex) {
            return "Lỗi database: " + ex.getMessage();
        }
    }

    public synchronized List<Hotel> listHotels() {
        List<Hotel> items = new ArrayList<>();
        String sql = "SELECT id, name, stars, description FROM hotels ORDER BY id";
        try (PreparedStatement ps = connection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                items.add(new Hotel(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getInt("stars"),
                        rs.getString("description")));
            }
        } catch (SQLException ignored) {
            return new ArrayList<>();
        }
        return items;
    }

    public synchronized List<Room> listRooms(String hotelId) {
        List<Room> items = new ArrayList<>();
        String sql = "SELECT hotel_id, room_id, type, price FROM rooms WHERE hotel_id = ? ORDER BY room_id";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            ps.setString(1, hotelId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new Room(
                            rs.getString("hotel_id"),
                            rs.getString("room_id"),
                            rs.getString("type"),
                            rs.getDouble("price")));
                }
            }
        } catch (SQLException ignored) {
            return new ArrayList<>();
        }
        return items;
    }

    public synchronized List<Room> searchRooms(String typeKeyword, double maxPrice) {
        List<Room> output = new ArrayList<>();
        String normalized = typeKeyword == null ? "" : typeKeyword.trim().toLowerCase();
        StringBuilder sql = new StringBuilder("SELECT hotel_id, room_id, type, price FROM rooms WHERE 1=1");
        if (!normalized.isEmpty()) {
            sql.append(" AND LOWER(type) LIKE ?");
        }
        if (maxPrice >= 0) {
            sql.append(" AND price <= ?");
        }
        sql.append(" ORDER BY hotel_id, room_id");
        try (PreparedStatement ps = connection().prepareStatement(sql.toString())) {
            int index = 1;
            if (!normalized.isEmpty()) {
                ps.setString(index++, "%" + normalized + "%");
            }
            if (maxPrice >= 0) {
                ps.setDouble(index, maxPrice);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    output.add(new Room(
                            rs.getString("hotel_id"),
                            rs.getString("room_id"),
                            rs.getString("type"),
                            rs.getDouble("price")));
                }
            }
        } catch (SQLException ignored) {
            return new ArrayList<>();
        }
        return output;
    }

    private Connection connection() {
        return databaseManager.getConnection();
    }

    private boolean hotelExists(String hotelId) {
        String sql = "SELECT 1 FROM hotels WHERE id = ?";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            ps.setString(1, hotelId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            return false;
        }
    }

    private boolean isDuplicate(SQLException ex) {
        return "23000".equals(ex.getSQLState()) || ex.getErrorCode() == 1062;
    }
}
