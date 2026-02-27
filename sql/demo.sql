-- DDL for demo table
CREATE TABLE IF NOT EXISTS `demo` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `description` TEXT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Sample data
INSERT INTO `demo` (`name`, `description`) VALUES ('Demo Item 1', 'Selection from the demo table');
INSERT INTO `demo` (`name`, `description`) VALUES ('Demo Item 2', 'Another example record');
