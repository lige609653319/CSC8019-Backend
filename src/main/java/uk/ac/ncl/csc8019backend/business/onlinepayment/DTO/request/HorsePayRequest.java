package uk.ac.ncl.csc8019backend.business.onlinepayment.DTO.request;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * HorsePay API request parameters (internally assembled)
 */
@Data
@Builder
public class HorsePayRequest {

    private String storeId;        // Team99
    private String customerId;     // User phone number
    private BigDecimal amount;     // Transaction amount
    private Boolean forceStatus;   // For testing: force specific result
}