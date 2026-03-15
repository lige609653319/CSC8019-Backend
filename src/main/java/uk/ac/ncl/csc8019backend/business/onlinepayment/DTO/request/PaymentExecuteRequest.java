package uk.ac.ncl.csc8019backend.business.onlinepayment.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Payment execution request (Frontend → Payment Module)
 */
@Data
@Schema(description = "Payment execution request parameters")
public class PaymentExecuteRequest {

    @Schema(description = "Order ID", example = "123", required = true)
    private Long orderId;

    @Schema(description = "Test mode: true for forced success, false for forced failure", example = "true")
    private Boolean forceStatus;
}