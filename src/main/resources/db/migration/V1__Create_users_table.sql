CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255)
);

CREATE TABLE bookings(
    booking_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user BIGINT NOT NULL, 
    showing_schedule BIGINT NOT NULL, 
    selected_seats JSON NOT NULL,
    booking_date_time DATETIME NOT NULL,
    total_price DOUBLE NOT NULL,
    payment_status ENUM('PENDING','AWAITING_CONFIRMATION','PAID', 'CANCELLED') NOT NULL
);

CREATE TABLE movies(
    movie_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title TEXT NOT NULL,
    genre VARCHAR(255) NOT NULL,
    duration INT(100) NOT NULL,
    rating DOUBLE NOT NULL
);

CREATE TABLE theaters(
    theater_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    theater_name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL
);

CREATE TABLE schedules(
    schedule_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie BIGINT NOT NULL,
    theater BIGINT NOT NULL,
    showing_date DATE NOT NULL,
    showing_time TIME NOT NULL,
    ticket_price DOUBLE NOT NULL
);

CREATE TABLE seats(
    seat_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seat_number VARCHAR(255) NOT NULL,
    status ENUM('AVAILABLE','RESERVED','SOLD')
);

CREATE TABLE tickets(
    ticket_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,
    uniqueCode VARCHAR(255) NOT NULL
);