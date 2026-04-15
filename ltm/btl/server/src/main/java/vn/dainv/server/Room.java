package vn.dainv.server;

public class Room {
    private final String hotelId;
    private final String roomId;
    private String type;
    private double price;

    public Room(String hotelId, String roomId, String type, double price) {
        this.hotelId = hotelId;
        this.roomId = roomId;
        this.type = type;
        this.price = price;
    }

    public String getHotelId() {
        return hotelId;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
