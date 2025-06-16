CREATE TABLE seats(
    seat_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seat_number VARCHAR(255) NOT NULL,
    booking_id BIGINT NOT NULL,
    status ENUM('AVAILABLE','RESERVED','SOLD') NOT NULL,
    
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id)
)