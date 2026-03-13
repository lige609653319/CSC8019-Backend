package uk.ac.ncl.csc8019backend.business.onlinepayment.DTO.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Payment status query response
 */
@Data
@Builder
@Schema(description = "Payment status information")
public class PaymentStatusDTO {

    @Schema(description = "Order ID", example = "123")
    private Long orderId;

    @Schema(description = "Order number", example = "WS202403051430001234")
    private String orderNo;

    @Schema(description = "Payment status: 0=unpaid, 1=paid, 2=failed, 3=refunded", example = "1")
    private Integer paymentStatus;

    @Schema(description = "Order status: 0=pending, 1=accepted, 2=in progress, 3=ready, 4=collected, 5=cancelled", example = "1")
    private Integer orderStatus;

    @Schema(description = "Payment amount", example = "5.50")
    private BigDecimal amount;

    @Schema(description = "Payment success time", example = "2024-03-05T14:30:00")
    private LocalDateTime paidAt;

    @Schema(description = "Failure reason (when failed)", example = "forcePaymentSatusReturnType set")
    private String failureReason;
}