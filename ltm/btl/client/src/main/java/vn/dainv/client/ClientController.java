package vn.dainv.client;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;

public class ClientController {
    @FXML
    private TextField hostField;
    @FXML
    private TextField portField;
    @FXML
    private TextArea outputArea;

    @FXML
    private TextField hotelIdField;
    @FXML
    private TextField hotelNameField;
    @FXML
    private TextField hotelStarsField;
    @FXML
    private TextField hotelDescField;

    @FXML
    private TextField roomHotelIdField;
    @FXML
    private TextField roomIdField;
    @FXML
    private TextField roomTypeField;
    @FXML
    private TextField roomPriceField;

    @FXML
    private TextField searchTypeField;
    @FXML
    private TextField searchMaxPriceField;

    private final ServerConnection connection = new ServerConnection();

    @FXML
    public void initialize() {
        hostField.setText("127.0.0.1");
        portField.setText("5555");
        outputArea.setText("Chua ket noi server");
    }

    @FXML
    protected void onConnect() {
        try {
            connection.connect(hostField.getText().trim(), Integer.parseInt(portField.getText().trim()));
            print("Da ket noi server");
        } catch (IOException | NumberFormatException ex) {
            print("Ket noi that bai: " + ex.getMessage());
        }
    }

    @FXML
    protected void onAddHotel() {
        sendAndPrint("ADD_HOTEL",
                ProtocolUtils.encode(hotelIdField.getText().trim()),
                ProtocolUtils.encode(hotelNameField.getText().trim()),
                ProtocolUtils.encode(hotelStarsField.getText().trim()),
                ProtocolUtils.encode(hotelDescField.getText().trim()));
    }

    @FXML
    protected void onUpdateHotel() {
        sendAndPrint("UPDATE_HOTEL",
                ProtocolUtils.encode(hotelIdField.getText().trim()),
                ProtocolUtils.encode(hotelNameField.getText().trim()),
                ProtocolUtils.encode(hotelStarsField.getText().trim()),
                ProtocolUtils.encode(hotelDescField.getText().trim()));
    }

    @FXML
    protected void onDeleteHotel() {
        sendAndPrint("DELETE_HOTEL", ProtocolUtils.encode(hotelIdField.getText().trim()));
    }

    @FXML
    protected void onListHotels() {
        ServerResponse response = send("LIST_HOTELS");
        if (response == null) {
            return;
        }
        print(response.getMessage());
        if (!response.isOk()) {
            return;
        }
        if (response.getPayload().isBlank()) {
            print("Khong co khach san");
            return;
        }
        for (String record : ProtocolUtils.split(response.getPayload(), ";")) {
            List<String> fields = ProtocolUtils.split(record, ",");
            print("- KS: id=" + ProtocolUtils.decode(fields.get(0))
                    + ", ten=" + ProtocolUtils.decode(fields.get(1))
                    + ", sao=" + ProtocolUtils.decode(fields.get(2))
                    + ", mo ta=" + ProtocolUtils.decode(fields.get(3)));
        }
    }

    @FXML
    protected void onAddRoom() {
        sendAndPrint("ADD_ROOM",
                ProtocolUtils.encode(roomHotelIdField.getText().trim()),
                ProtocolUtils.encode(roomIdField.getText().trim()),
                ProtocolUtils.encode(roomTypeField.getText().trim()),
                ProtocolUtils.encode(roomPriceField.getText().trim()));
    }

    @FXML
    protected void onUpdateRoom() {
        sendAndPrint("UPDATE_ROOM",
                ProtocolUtils.encode(roomHotelIdField.getText().trim()),
                ProtocolUtils.encode(roomIdField.getText().trim()),
                ProtocolUtils.encode(roomTypeField.getText().trim()),
                ProtocolUtils.encode(roomPriceField.getText().trim()));
    }

    @FXML
    protected void onDeleteRoom() {
        sendAndPrint("DELETE_ROOM",
                ProtocolUtils.encode(roomHotelIdField.getText().trim()),
                ProtocolUtils.encode(roomIdField.getText().trim()));
    }

    @FXML
    protected void onListRooms() {
        ServerResponse response = send("LIST_ROOMS", ProtocolUtils.encode(roomHotelIdField.getText().trim()));
        if (response == null) {
            return;
        }
        print(response.getMessage());
        if (!response.isOk()) {
            return;
        }
        if (response.getPayload().isBlank()) {
            print("Khong co phong");
            return;
        }
        printRooms(response.getPayload());
    }

    @FXML
    protected void onSearchRooms() {
        String maxPriceText = searchMaxPriceField.getText().trim();
        if (maxPriceText.isBlank()) {
            maxPriceText = "-1";
        }
        ServerResponse response = send("SEARCH_ROOMS",
                ProtocolUtils.encode(searchTypeField.getText().trim()),
                ProtocolUtils.encode(maxPriceText));
        if (response == null) {
            return;
        }
        print(response.getMessage());
        if (!response.isOk()) {
            return;
        }
        if (response.getPayload().isBlank()) {
            print("Khong tim thay phong phu hop");
            return;
        }
        printRooms(response.getPayload());
    }

    private void printRooms(String payload) {
        for (String record : ProtocolUtils.split(payload, ";")) {
            List<String> fields = ProtocolUtils.split(record, ",");
            print("- Phong: khachSan=" + ProtocolUtils.decode(fields.get(0))
                    + ", maPhong=" + ProtocolUtils.decode(fields.get(1))
                    + ", loai=" + ProtocolUtils.decode(fields.get(2))
                    + ", gia=" + ProtocolUtils.decode(fields.get(3)));
        }
    }

    private void sendAndPrint(String command, String... args) {
        ServerResponse response = send(command, args);
        if (response != null) {
            print(response.getMessage());
        }
    }

    private ServerResponse send(String command, String... args) {
        try {
            return connection.send(command, args);
        } catch (IOException ex) {
            print("Loi giao tiep: " + ex.getMessage());
            return null;
        }
    }

    private void print(String text) {
        outputArea.appendText(text + System.lineSeparator());
    }
}
