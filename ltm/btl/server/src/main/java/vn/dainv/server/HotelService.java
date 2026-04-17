package vn.dainv.server;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HotelService {
    private final Map<String, Hotel> hotels = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Room>> roomsByHotel = new ConcurrentHashMap<>();

    public synchronized String addHotel(String id, String name, int stars, String description) {
        if (id == null || id.isBlank()) {
            return "Mã khách sạn không được để trống";
        }
        if (hotels.containsKey(id)) {
            return "Khách sạn đã tồn tại";
        }
        hotels.put(id, new Hotel(id, name, stars, description));
        roomsByHotel.put(id, new ConcurrentHashMap<String, Room>());
        return null;
    }

    public synchronized String updateHotel(String id, String name, int stars, String description) {
        Hotel hotel = hotels.get(id);
        if (hotel == null) {
            return "Không tìm thấy khách sạn";
        }
        hotel.setName(name);
        hotel.setStars(stars);
        hotel.setDescription(description);
        return null;
    }

    public synchronized String deleteHotel(String id) {
        Map<String, Room> rooms = roomsByHotel.get(id);
        if (rooms == null || !hotels.containsKey(id)) {
            return "Không tìm thấy khách sạn";
        }
        if (!rooms.isEmpty()) {
            return "Chỉ được xóa khách sạn khi đã xóa hết phòng";
        }
        roomsByHotel.remove(id);
        hotels.remove(id);
        return null;
    }

    public synchronized String addRoom(String hotelId, String roomId, String type, double price) {
        if (!hotels.containsKey(hotelId)) {
            return "Không tìm thấy khách sạn";
        }
        Map<String, Room> rooms = roomsByHotel.get(hotelId);
        if (rooms.containsKey(roomId)) {
            return "Phòng đã tồn tại";
        }
        rooms.put(roomId, new Room(hotelId, roomId, type, price));
        return null;
    }

    public synchronized String updateRoom(String hotelId, String roomId, String type, double price) {
        Map<String, Room> rooms = roomsByHotel.get(hotelId);
        if (rooms == null) {
            return "Không tìm thấy khách sạn";
        }
        Room room = rooms.get(roomId);
        if (room == null) {
            return "Không tìm thấy phòng";
        }
        room.setType(type);
        room.setPrice(price);
        return null;
    }

    public synchronized String deleteRoom(String hotelId, String roomId) {
        Map<String, Room> rooms = roomsByHotel.get(hotelId);
        if (rooms == null) {
            return "Không tìm thấy khách sạn";
        }
        Room removed = rooms.remove(roomId);
        if (removed == null) {
            return "Không tìm thấy phòng";
        }
        return null;
    }

    public synchronized List<Hotel> listHotels() {
        List<Hotel> items = new ArrayList<>(hotels.values());
        items.sort(Comparator.comparing(Hotel::getId));
        return items;
    }

    public synchronized List<Room> listRooms(String hotelId) {
        Map<String, Room> rooms = roomsByHotel.get(hotelId);
        if (rooms == null) {
            return new ArrayList<>();
        }
        List<Room> items = new ArrayList<>(rooms.values());
        items.sort(Comparator.comparing(Room::getRoomId));
        return items;
    }

    public synchronized List<Room> searchRooms(String typeKeyword, double maxPrice) {
        List<Room> output = new ArrayList<>();
        String normalized = typeKeyword == null ? "" : typeKeyword.trim().toLowerCase();
        for (Map<String, Room> rooms : roomsByHotel.values()) {
            for (Room room : rooms.values()) {
                boolean matchType = normalized.isEmpty()
                        || room.getType().toLowerCase().contains(normalized);
                boolean matchPrice = maxPrice < 0 || room.getPrice() <= maxPrice;
                if (matchType && matchPrice) {
                    output.add(room);
                }
            }
        }
        output.sort(Comparator.comparing(Room::getHotelId).thenComparing(Room::getRoomId));
        return output;
    }
}
