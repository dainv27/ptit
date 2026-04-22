CREATE DATABASE IF NOT EXISTS lap_trinh_mang
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE lap_trinh_mang;

CREATE TABLE IF NOT EXISTS hotels (
    id VARCHAR(64) NOT NULL,
    name VARCHAR(255) NOT NULL,
    stars INT NOT NULL,
    description TEXT,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS rooms (
    hotel_id VARCHAR(64) NOT NULL,
    room_id VARCHAR(64) NOT NULL,
    type VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    PRIMARY KEY (hotel_id, room_id),
    CONSTRAINT fk_rooms_hotels
        FOREIGN KEY (hotel_id) REFERENCES hotels(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
