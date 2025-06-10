CREATE TABLE bookings(
    booking_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL, 
    showing_schedule_id BIGINT NOT NULL, 
    selected_seats JSON NOT NULL,
    booking_date_time DATETIME NOT NULL,
    total_price DOUBLE NOT NULL,
    payment_status ENUM('PENDING','AWAITING_CONFIRMATION','PAID', 'CANCELLED') NOT NULL
);