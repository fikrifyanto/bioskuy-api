CREATE TABLE schedules(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_id BIGINT NOT NULL,
    theater_id BIGINT NOT NULL,
    showing_date DATE NOT NULL,
    showing_time TIME NOT NULL,
    ticket_price DOUBLE NOT NULL
);