package uk.ac.ncl.csc8019backend.business.onlinepayment.DTO.response;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;

import java.math.BigDecimal;

/**
 * HorsePay API return result (internal parsing)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class HorsePayResult {

    private boolean success;           // Status: true/false
    private String message;            // reason
    private String storeId;            // storeID
    private String customerId;         // customerID
    private BigDecimal transactionAmount;
    private String transactionId;      // Generated transaction identifier (not returned by HorsePay, used for tracking)
}