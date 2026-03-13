package uk.ac.ncl.csc8019backend.business.onlinepayment.DTO.internal;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

/**
 * HorsePay raw response (for parsing and storage)
 */
@Data
public class HorsePayResponse {

    private String storeID;
    private String customerID;
    private String date;
    private String time;
    private String timeZone;
    private BigDecimal transactionAmount;
    private String currencyCode;
    private Map<String, Object> paymetSuccess;  // Note spelling error, keep as is

    /**
     * Get payment status
     */
    public boolean isSuccess() {
        if (paymetSuccess == null) return false;
        Object status = paymetSuccess.get("Status");
        return status instanceof Boolean ? (Boolean) status : false;
    }

    /**
     * Get reason
     */
    public String getReason() {
        if (paymetSuccess == null) return null;
        return (String) paymetSuccess.get("reason");
    }
}
