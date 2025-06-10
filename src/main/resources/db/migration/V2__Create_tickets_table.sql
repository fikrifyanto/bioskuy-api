CREATE TABLE tickets(
    ticket_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,
    unique_code VARCHAR(255) NOT NULL
);