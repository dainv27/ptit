package vn.dainv.server;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerController {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    @FXML
    private TextField portField;
    @FXML
    private TextArea logArea;

    private HotelTcpServer tcpServer;

    @FXML
    public void initialize() {
        tcpServer = new HotelTcpServer(this::appendLog);
        portField.setText("5555");
        appendLog("Sẵn sàng khởi động server");
    }

    @FXML
    protected void onStartServer() {
        try {
            int port = Integer.parseInt(portField.getText().trim());
            tcpServer.start(port);
        } catch (NumberFormatException ex) {
            appendLog("Port không hợp lệ");
        } catch (IllegalStateException | IOException ex) {
            appendLog(ex.getMessage());
        }
    }

    @FXML
    protected void onStopServer() {
        tcpServer.stop();
    }

    private void appendLog(String message) {
        Platform.runLater(() -> logArea.appendText(
                "[" + LocalDateTime.now().format(TIME_FORMAT) + "] " + message + System.lineSeparator()));
    }
}
