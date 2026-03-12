package uk.ac.ncl.csc8019backend.business.onlinepayment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * HorsePay configuration properties
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "payment.horsepay")
public class HorsePayConfig {

    /**
     * HorsePay API URL
     */
    private String apiUrl;

    /**
     * Store ID (Team number)
     */
    private String storeId;

    /**
     * Connection timeout in milliseconds
     */
    private int connectTimeout;

    /**
     * Read timeout in milliseconds
     */
    private int readTimeout;
}