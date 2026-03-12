package uk.ac.ncl.csc8019backend.business.onlinepayment.controller;


import uk.ac.ncl.csc8019backend.business.onlinepayment.DTO.request.PaymentExecuteRequest;
import uk.ac.ncl.csc8019backend.business.onlinepayment.DTO.response.PaymentResult;
import uk.ac.ncl.csc8019backend.business.onlinepayment.DTO.response.PaymentStatusDTO;
import uk.ac.ncl.csc8019backend.business.onlinepayment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Payment API controller - exposes HTTP endpoints for payment operations
 */
@RestController
@RequestMapping("/api/payment")
@Tag(name = "Payment API", description = "HorsePay online payment related interfaces")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Execute payment (calls HorsePay)
     * Normal flow: customer places order → clicks pay → calls this interface → returns result
     * Test mode: add forceStatus parameter to force specific result
     */
    @Operation(
            summary = "Execute payment",
            description = "Call HorsePay to process payment, supports normal payment and test mode",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Payment successful"),
                    @ApiResponse(responseCode = "404", description = "Order not found"),
                    @ApiResponse(responseCode = "409", description = "Order not payable"),
                    @ApiResponse(responseCode = "402", description = "Payment failed")
            }
    )
    @PostMapping("/execute/{orderId}")
    public ResponseEntity<PaymentResult> executePayment(
            @Parameter(description = "Order ID", required = true, example = "123")
            @PathVariable @NotNull Long orderId,

            @Parameter(description = "Test mode: true for forced success, false for forced failure", example = "true")
            @RequestParam(required = false) Boolean forceStatus) {

        log.info("Payment execute request: orderId={}, forceStatus={}", orderId, forceStatus);

        PaymentResult result = paymentService.executePayment(orderId, forceStatus);

        HttpStatus status = result.isSuccess() ? HttpStatus.OK :
                "ORDER_NOT_FOUND".equals(result.getCode()) ? HttpStatus.NOT_FOUND :
                        "ORDER_NOT_PAYABLE".equals(result.getCode()) ? HttpStatus.CONFLICT :
                                HttpStatus.PAYMENT_REQUIRED;

        return ResponseEntity.status(status).body(result);
    }

    /**
     * Query payment status (for frontend polling)
     */
    @Operation(summary = "Query payment status", description = "For frontend to poll payment result")
    @GetMapping("/status/{orderId}")
    public ResponseEntity<PaymentStatusDTO> getStatus(
            @Parameter(description = "Order ID") @PathVariable Long orderId) {

        PaymentStatusDTO status = paymentService.getPaymentStatus(orderId);
        if (status == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(status);
    }

    /**
     * Health check (for service registry)
     */
    @Operation(summary = "Health check", hidden = true)
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "payment"));
    }
}
