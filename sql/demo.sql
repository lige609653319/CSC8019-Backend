-- DDL for demo table
CREATE TABLE IF NOT EXISTS `demo` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `description` TEXT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `orders` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `customer_id` BIGINT,
    `total_price` DECIMAL(10, 2) NOT NULL,
    `order_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    -- Mapping to your Java Enum values (0, 1, 2, 3)
    `status` TINYINT NOT NULL,
    `order_type` TINYINT NOT NULL,
    CONSTRAINT `fk_order_customer` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Sample data
INSERT INTO `demo` (`name`, `description`) VALUES ('Demo Item 1', 'Selection from the demo table');
INSERT INTO `demo` (`name`, `description`) VALUES ('Demo Item 2', 'Another example record');
