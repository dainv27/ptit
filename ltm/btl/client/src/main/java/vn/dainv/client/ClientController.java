package vn.dainv.client;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ClientController {
    private static final String ALL_HOTELS_OPTION = "Tất cả khách sạn";

    @FXML
    private TextField hostField;
    @FXML
    private TextField portField;
    @FXML
    private TextArea outputArea;
    @FXML
    private TableView<HotelRow> hotelTable;
    @FXML
    private TableColumn<HotelRow, String> hotelIdColumn;
    @FXML
    private TableColumn<HotelRow, String> hotelNameColumn;
    @FXML
    private TableColumn<HotelRow, String> hotelStarsColumn;
    @FXML
    private TableColumn<HotelRow, String> hotelDescColumn;
    @FXML
    private TableColumn<HotelRow, Void> hotelActionColumn;
    @FXML
    private TableView<RoomRow> roomTable;
    @FXML
    private TableColumn<RoomRow, String> roomHotelIdColumn;
    @FXML
    private TableColumn<RoomRow, String> roomIdColumn;
    @FXML
    private TableColumn<RoomRow, String> roomTypeColumn;
    @FXML
    private TableColumn<RoomRow, String> roomPriceColumn;
    @FXML
    private TableColumn<RoomRow, Void> roomActionColumn;
    @FXML
    private ComboBox<String> roomHotelFilterComboBox;

    private final ServerConnection connection = new ServerConnection();

    @FXML
    public void initialize() {
        hostField.setText("127.0.0.1");
        portField.setText("5555");
        initTables();
        print("Chưa kết nối tới server");
    }

    @FXML
    protected void onConnect() {
        try {
            connection.connect(hostField.getText().trim(), Integer.parseInt(portField.getText().trim()));
            print("Đã kết nối tới server");
            onListHotels();
            onListRooms();
        } catch (IOException | NumberFormatException ex) {
            print("Kết nối thất bại: " + ex.getMessage());
        }
    }

    @FXML
    protected void onAddHotel() {
        Optional<Map<String, String>> formData = showForm(
                "Thêm khách sạn",
                "Nhập thông tin khách sạn mới",
                List.of("Mã khách sạn", "Tên khách sạn", "Số sao", "Mô tả"));
        if (formData.isEmpty()) {
            return;
        }
        List<String> values = mapValues(formData.get());
        sendAndPrint("ADD_HOTEL", Map.of(
                "id", values.get(0),
                "name", values.get(1),
                "stars", values.get(2),
                "description", values.get(3)));
        onListHotels();
    }

    @FXML
    protected void onListHotels() {
        ServerResponse response = send("LIST_HOTELS");
        if (response == null) {
            return;
        }
        print(response.getMessage());
        if (!response.isOk()) {
            hotelTable.getItems().clear();
            updateRoomHotelFilter();
            return;
        }
        JsonNode data = response.getData();
        if (!data.isArray() || data.isEmpty()) {
            print("Không có khách sạn");
            hotelTable.getItems().clear();
            updateRoomHotelFilter();
            return;
        }
        ObservableList<HotelRow> rows = FXCollections.observableArrayList();
        for (JsonNode item : data) {
            String id = item.path("id").asText("");
            String name = item.path("name").asText("");
            String stars = item.path("stars").asText("");
            String desc = item.path("description").asText("");
            rows.add(new HotelRow(id, name, stars, desc));
            print("- KS: id=" + id + ", tên=" + name + ", sao=" + stars + ", mô tả=" + desc);
        }
        hotelTable.setItems(rows);
        updateRoomHotelFilter();
    }

    @FXML
    protected void onAddRoom() {
        Optional<Map<String, String>> formData = showRoomForm(
                "Thêm phòng",
                "Nhập thông tin phòng mới",
                null);
        if (formData.isEmpty()) {
            return;
        }
        List<String> values = mapValues(formData.get());
        sendAndPrint("ADD_ROOM", Map.of(
                "hotelId", values.get(0),
                "roomId", values.get(1),
                "type", values.get(2),
                "price", values.get(3)));
        refreshRoomTableByHotel(values.get(0));
    }

    @FXML
    protected void onListRooms() {
        String hotelId = roomHotelFilterComboBox.getValue();
        if (hotelId == null || hotelId.isBlank()) {
            print("Vui lòng chọn khách sạn để xem danh sách phòng");
            return;
        }
        ServerResponse response;
        if (ALL_HOTELS_OPTION.equals(hotelId)) {
            response = send("SEARCH_ROOMS", Map.of("keyword", "", "maxPrice", -1));
        } else {
            response = send("LIST_ROOMS", Map.of("hotelId", hotelId));
        }
        if (response == null) {
            return;
        }
        print(response.getMessage());
        if (!response.isOk()) {
            roomTable.getItems().clear();
            return;
        }
        JsonNode data = response.getData();
        if (!data.isArray() || data.isEmpty()) {
            print("Không có phòng");
            roomTable.getItems().clear();
            return;
        }
        showRooms(data);
    }

    @FXML
    protected void onSearchRooms() {
        Optional<Map<String, String>> formData = showForm(
                "Tìm phòng",
                "Nhập bộ lọc tìm kiếm phòng",
                List.of("Loại phòng", "Giá tối đa (để trống nếu không giới hạn)"));
        if (formData.isEmpty()) {
            return;
        }
        List<String> values = mapValues(formData.get());
        String maxPriceText = values.get(1);
        if (maxPriceText.isBlank()) {
            maxPriceText = "-1";
        }
        ServerResponse response = send("SEARCH_ROOMS",
                Map.of("keyword", values.get(0), "maxPrice", maxPriceText));
        if (response == null) {
            return;
        }
        print(response.getMessage());
        if (!response.isOk()) {
            roomTable.getItems().clear();
            return;
        }
        JsonNode data = response.getData();
        if (!data.isArray() || data.isEmpty()) {
            print("Không tìm thấy phòng phù hợp");
            roomTable.getItems().clear();
            return;
        }
        showRooms(data);
    }

    private void initTables() {
        hotelIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        hotelNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        hotelStarsColumn.setCellValueFactory(new PropertyValueFactory<>("stars"));
        hotelDescColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        roomHotelIdColumn.setCellValueFactory(new PropertyValueFactory<>("hotelId"));
        roomIdColumn.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        roomPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        setupHotelActionColumn();
        setupRoomActionColumn();
    }

    private void showRooms(JsonNode payload) {
        ObservableList<RoomRow> rows = FXCollections.observableArrayList();
        for (JsonNode item : payload) {
            String hotelId = item.path("hotelId").asText("");
            String roomId = item.path("roomId").asText("");
            String type = item.path("type").asText("");
            String price = item.path("price").asText("");
            rows.add(new RoomRow(hotelId, roomId, type, price));
            print("- Phòng: kháchSạn=" + hotelId
                    + ", mãPhòng=" + roomId
                    + ", loại=" + type
                    + ", giá=" + price);
        }
        roomTable.setItems(rows);
    }

    private Optional<Map<String, String>> showForm(String title, String header, List<String> fieldLabels) {
        return showForm(title, header, fieldLabels, Map.of());
    }

    private Optional<Map<String, String>> showForm(String title,
                                                   String header,
                                                   List<String> fieldLabels,
                                                   Map<String, String> initialValues) {
        Dialog<Map<String, String>> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(header);

        ButtonType submitButtonType = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);

        Map<String, TextField> fields = new LinkedHashMap<>();
        for (int i = 0; i < fieldLabels.size(); i++) {
            String fieldLabel = fieldLabels.get(i);
            Label label = new Label(fieldLabel + ":");
            TextField textField = new TextField();
            textField.setText(initialValues.getOrDefault(fieldLabel, ""));
            fields.put(fieldLabel, textField);
            grid.add(label, 0, i);
            grid.add(textField, 1, i);
        }
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(buttonType -> {
            if (buttonType != submitButtonType) {
                return null;
            }
            Map<String, String> result = new LinkedHashMap<>();
            for (Map.Entry<String, TextField> entry : fields.entrySet()) {
                result.put(entry.getKey(), entry.getValue().getText().trim());
            }
            return result;
        });
        return dialog.showAndWait();
    }

    private List<String> mapValues(Map<String, String> formData) {
        return new ArrayList<>(formData.values());
    }

    private void setupHotelActionColumn() {
        hotelActionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button editButton = new Button("Sửa");
            private final Button deleteButton = new Button("Xóa");
            private final HBox box = new HBox(6, editButton, deleteButton);

            {
                editButton.setOnAction(event -> {
                    HotelRow row = getTableView().getItems().get(getIndex());
                    onEditHotelRow(row);
                });
                deleteButton.setOnAction(event -> {
                    HotelRow row = getTableView().getItems().get(getIndex());
                    onDeleteHotelRow(row);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    private void setupRoomActionColumn() {
        roomActionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button editButton = new Button("Sửa");
            private final Button deleteButton = new Button("Xóa");
            private final HBox box = new HBox(6, editButton, deleteButton);

            {
                editButton.setOnAction(event -> {
                    RoomRow row = getTableView().getItems().get(getIndex());
                    onEditRoomRow(row);
                });
                deleteButton.setOnAction(event -> {
                    RoomRow row = getTableView().getItems().get(getIndex());
                    onDeleteRoomRow(row);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    private void onEditHotelRow(HotelRow row) {
        Map<String, String> initialValues = new LinkedHashMap<>();
        initialValues.put("Mã khách sạn", row.getId());
        initialValues.put("Tên khách sạn", row.getName());
        initialValues.put("Số sao", row.getStars());
        initialValues.put("Mô tả", row.getDescription());
        Optional<Map<String, String>> formData = showForm(
                "Sửa khách sạn",
                "Cập nhật thông tin khách sạn",
                List.of("Mã khách sạn", "Tên khách sạn", "Số sao", "Mô tả"),
                initialValues);
        if (formData.isEmpty()) {
            return;
        }
        List<String> values = mapValues(formData.get());
        sendAndPrint("UPDATE_HOTEL", Map.of(
                "id", values.get(0),
                "name", values.get(1),
                "stars", values.get(2),
                "description", values.get(3)));
        onListHotels();
    }

    private void onDeleteHotelRow(HotelRow row) {
        if (!confirmDelete("khách sạn", row.getId())) {
            return;
        }
        sendAndPrint("DELETE_HOTEL", Map.of("id", row.getId()));
        onListHotels();
    }

    private void onEditRoomRow(RoomRow row) {
        Optional<Map<String, String>> formData = showRoomForm(
                "Sửa phòng",
                "Cập nhật thông tin phòng",
                row);
        if (formData.isEmpty()) {
            return;
        }
        List<String> values = mapValues(formData.get());
        sendAndPrint("UPDATE_ROOM", Map.of(
                "hotelId", values.get(0),
                "roomId", values.get(1),
                "type", values.get(2),
                "price", values.get(3)));
        refreshRoomTableByHotel(values.get(0));
    }

    private void onDeleteRoomRow(RoomRow row) {
        if (!confirmDelete("phòng", row.getRoomId())) {
            return;
        }
        sendAndPrint("DELETE_ROOM", Map.of(
                "hotelId", row.getHotelId(),
                "roomId", row.getRoomId()));
        refreshRoomTableByHotel(row.getHotelId());
    }

    private boolean confirmDelete(String objectName, String objectId) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Bạn có chắc chắn muốn xóa " + objectName + "?");
        alert.setContentText("Đối tượng: " + objectId);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private void refreshRoomTableByHotel(String hotelId) {
        roomHotelFilterComboBox.setValue(hotelId);
        ServerResponse response = send("LIST_ROOMS", Map.of("hotelId", hotelId));
        if (response == null) {
            return;
        }
        print(response.getMessage());
        JsonNode data = response.getData();
        if (!response.isOk() || !data.isArray() || data.isEmpty()) {
            roomTable.getItems().clear();
            return;
        }
        showRooms(data);
    }

    private void updateRoomHotelFilter() {
        List<String> hotelIds = getHotelIdsFromTable();
        List<String> filterOptions = new ArrayList<>();
        filterOptions.add(ALL_HOTELS_OPTION);
        filterOptions.addAll(hotelIds);
        roomHotelFilterComboBox.getItems().setAll(filterOptions);
        if (hotelIds.isEmpty()) {
            roomHotelFilterComboBox.setValue(ALL_HOTELS_OPTION);
            roomTable.getItems().clear();
            return;
        }
        String current = roomHotelFilterComboBox.getValue();
        if (current == null || (!ALL_HOTELS_OPTION.equals(current) && !hotelIds.contains(current))) {
            roomHotelFilterComboBox.setValue(ALL_HOTELS_OPTION);
        }
    }

    private List<String> getHotelIdsFromTable() {
        List<String> hotelIds = new ArrayList<>();
        for (HotelRow row : hotelTable.getItems()) {
            hotelIds.add(row.getId());
        }
        return hotelIds;
    }

    private Optional<Map<String, String>> showRoomForm(String title, String header, RoomRow initialRow) {
        Dialog<Map<String, String>> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(header);

        ButtonType submitButtonType = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);

        Label hotelLabel = new Label("Mã khách sạn:");
        ComboBox<String> hotelComboBox = new ComboBox<>();
        hotelComboBox.getItems().setAll(getHotelIdsFromTable());
        if (initialRow != null) {
            hotelComboBox.setValue(initialRow.getHotelId());
        } else if (roomHotelFilterComboBox.getValue() != null
                && !ALL_HOTELS_OPTION.equals(roomHotelFilterComboBox.getValue())) {
            hotelComboBox.setValue(roomHotelFilterComboBox.getValue());
        } else if (!hotelComboBox.getItems().isEmpty()) {
            hotelComboBox.setValue(hotelComboBox.getItems().get(0));
        }

        Label roomIdLabel = new Label("Mã phòng:");
        TextField roomIdField = new TextField(initialRow == null ? "" : initialRow.getRoomId());
        Label roomTypeLabel = new Label("Loại phòng:");
        TextField roomTypeField = new TextField(initialRow == null ? "" : initialRow.getType());
        Label roomPriceLabel = new Label("Giá:");
        TextField roomPriceField = new TextField(initialRow == null ? "" : initialRow.getPrice());

        grid.add(hotelLabel, 0, 0);
        grid.add(hotelComboBox, 1, 0);
        grid.add(roomIdLabel, 0, 1);
        grid.add(roomIdField, 1, 1);
        grid.add(roomTypeLabel, 0, 2);
        grid.add(roomTypeField, 1, 2);
        grid.add(roomPriceLabel, 0, 3);
        grid.add(roomPriceField, 1, 3);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(buttonType -> {
            if (buttonType != submitButtonType) {
                return null;
            }
            String hotelId = hotelComboBox.getValue() == null ? "" : hotelComboBox.getValue().trim();
            Map<String, String> result = new LinkedHashMap<>();
            result.put("Mã khách sạn", hotelId);
            result.put("Mã phòng", roomIdField.getText().trim());
            result.put("Loại phòng", roomTypeField.getText().trim());
            result.put("Giá", roomPriceField.getText().trim());
            return result;
        });
        return dialog.showAndWait();
    }

    private void sendAndPrint(String command, Map<String, Object> data) {
        ServerResponse response = send(command, data);
        if (response != null) {
            print(response.getMessage());
        }
    }

    private ServerResponse send(String command) {
        return send(command, Map.of());
    }

    private ServerResponse send(String command, Map<String, Object> data) {
        try {
            return connection.send(command, data);
        } catch (IOException ex) {
            print("Lỗi giao tiếp: " + ex.getMessage());
            return null;
        }
    }

    private void print(String text) {
        outputArea.appendText(text + System.lineSeparator());
    }

    public static class HotelRow {
        private final String id;
        private final String name;
        private final String stars;
        private final String description;

        public HotelRow(String id, String name, String stars, String description) {
            this.id = id;
            this.name = name;
            this.stars = stars;
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getStars() {
            return stars;
        }

        public String getDescription() {
            return description;
        }
    }

    public static class RoomRow {
        private final String hotelId;
        private final String roomId;
        private final String type;
        private final String price;

        public RoomRow(String hotelId, String roomId, String type, String price) {
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

        public String getPrice() {
            return price;
        }
    }
}
