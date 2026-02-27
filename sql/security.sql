-- Create users table
CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `role` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Sample data (password is '123456' hashed with BCrypt)
-- INSERT INTO `users` (username, password, role) VALUES ('admin', '$2a$10$XlV.q.7.q.q.q.q.q.q.q.q.q.q.q.q.q.q.q.q.q.q.q.q.q.', 'STAFF');
-- INSERT INTO `users` (username, password, role) VALUES ('user', '$2a$10$XlV.q.7.q.q.q.q.q.q.q.q.q.q.q.q.q.q.q.q.q.q.q.q.q.q.', 'CLIENT');
