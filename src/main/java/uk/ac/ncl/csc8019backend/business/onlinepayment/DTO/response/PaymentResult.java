package uk.ac.ncl.csc8019backend.business.onlinepayment.DTO.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Payment result response (Payment Module → Frontend)
 */
@Data
@Builder
@Schema(description = "Payment execution result")
public class PaymentResult {

    @Schema(description = "Whether successful", example = "true")
    private boolean success;

    @Schema(description = "Business error code", example = "SUCCESS",
            allowableValues = {"SUCCESS", "ORDER_NOT_FOUND", "ORDER_NOT_PAYABLE", "PAYMENT_FAILED", "SYSTEM_ERROR"})
    private String code;

    @Schema(description = "Message", example = "Payment successful")
    private String message;

    @Schema(description = "Order ID", example = "123")
    private Long orderId;

    @Schema(description = "Order number", example = "WS202403051430001234")
    private String orderNo;

    @Schema(description = "Actual payment amount", example = "5.50")
    private BigDecimal amount;

    @Schema(description = "Redirect URL after success", example = "/order/success/123")
    private String redirectUrl;

    @Schema(description = "Response timestamp", example = "2024-03-05T14:30:00")
    private LocalDateTime timestamp;
}