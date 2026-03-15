CREATE TABLE IF NOT EXISTS `loyalty_account` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(255) NOT NULL,
    `points_balance` INT NOT NULL DEFAULT 0,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_loyalty_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `loyalty_transaction` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(255) NOT NULL,
    `order_id` BIGINT NULL,
    `type` VARCHAR(20) NOT NULL,
    `points` INT NOT NULL,
    `note` VARCHAR(255),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `IDX_loyalty_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;