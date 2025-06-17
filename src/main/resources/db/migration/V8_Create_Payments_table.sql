CREATE TABLE payments(
    payment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    payment_method  VARCHAR(255) NOT NULL,
    payment_date TIME NOT NULL,
    total_price DOUBLE NOT NULL,
    payment_status ENUM(' PENDING,PROCESSED,VERIFIED, FAILED') NOT NULL
);