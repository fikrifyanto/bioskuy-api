CREATE TABLE seats(
    seat_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seat_number VARCHAR(255) NOT NULL,
    booking_id BIGINT NOT NULL,
    schedule_id BIGINT NOT NULL,
    ticket_id BIGINT NOT NULL,
    status ENUM('AVAILABLE','RESERVED','SOLD')
);