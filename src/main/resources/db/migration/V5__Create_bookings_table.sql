CREATE TABLE bookings(
    booking_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    payment_status ENUM('PENDING','AWAITING_CONFIRMATION','PAID', 'CANCELLED') NOT NULL,
    user_id BIGINT NOT NULL, 
    booking_date_time DATETIME NOT NULL,
    total_price DOUBLE NOT NULL,
    showing_schedule_id BIGINT NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (showing_schedule_id) REFERENCES schedules(schedule_id)
);