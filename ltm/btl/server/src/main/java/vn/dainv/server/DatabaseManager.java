package vn.dainv.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private Connection connection;

    public synchronized void connect(DatabaseConfig config) throws SQLException {
        if (isConnected()) {
            return;
        }
        connection = DriverManager.getConnection(config.jdbcUrl(), config.username(), config.password());
    }

    public synchronized void disconnect() {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
        } catch (SQLException ignored) {
            // ignored
        } finally {
            connection = null;
        }
    }

    public synchronized Connection getConnection() {
        if (!isConnected()) {
            throw new IllegalStateException("Database chưa được kết nối");
        }
        return connection;
    }

    public synchronized void initializeSchema() throws SQLException {
        String createHotelsTable = """
                CREATE TABLE IF NOT EXISTS hotels (
                    id VARCHAR(64) PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    stars INT NOT NULL,
                    description TEXT
                )
                """;
        String createRoomsTable = """
                CREATE TABLE IF NOT EXISTS rooms (
                    hotel_id VARCHAR(64) NOT NULL,
                    room_id VARCHAR(64) NOT NULL,
                    type VARCHAR(255) NOT NULL,
                    price DOUBLE NOT NULL,
                    PRIMARY KEY (hotel_id, room_id),
                    CONSTRAINT fk_room_hotel
                        FOREIGN KEY (hotel_id) REFERENCES hotels(id)
                        ON DELETE CASCADE
                )
                """;
        try (Statement statement = getConnection().createStatement()) {
            statement.executeUpdate(createHotelsTable);
            statement.executeUpdate(createRoomsTable);
        }
    }

    public synchronized boolean isConnected() {
        if (connection == null) {
            return false;
        }
        try {
            return !connection.isClosed();
        } catch (SQLException ex) {
            return false;
        }
    }
}
