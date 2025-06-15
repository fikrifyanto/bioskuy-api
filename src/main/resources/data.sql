TRUNCATE bookings;
TRUNCATE movies;
TRUNCATE schedules;
TRUNCATE seats;
TRUNCATE theaters;
TRUNCATE tickets;
TRUNCATE users;

INSERT INTO `users` (`id`, `name`, `email`, `password`, `phone_number`) VALUES (NULL, 'John Doe', 'johndoe1@example.com', 'securePassword123', '+1234567890');
INSERT INTO `movies` (`id`, `title`, `genre`, `duration`, `rating`) VALUES (NULL, 'The Lion King', 'Animation, Fairytale', '120', '4.5');
INSERT INTO `movies` (`id`, `title`, `genre`, `duration`, `rating`) VALUES (NULL, 'Star Wars', 'Action', '120', '4.9');
INSERT INTO `movies` (`id`, `title`, `genre`, `duration`, `rating`) VALUES (NULL, 'Mission Impossible', 'Action', '154', '4.8');
INSERT INTO `theaters` (`theater_id`, `theater_name`, `location`) VALUES (NULL, 'Kansas Theater', 'Kansas, USA');
INSERT INTO `theaters` (`theater_id`, `theater_name`, `location`) VALUES (NULL, 'Dublin Theater', 'Dublin, Ireland');
INSERT INTO `schedules`(`schedule_id`, `movie_id`, `theater_id`, `showing_date`, `showing_time`, `ticket_price`) VALUES (NULL,'2','1','2025-06-13','16:04:49','8');
INSERT INTO `schedules`(`schedule_id`, `movie_id`, `theater_id`, `showing_date`, `showing_time`, `ticket_price`) VALUES (NULL,'3','2','2025-06-13','16:04:49','10');

