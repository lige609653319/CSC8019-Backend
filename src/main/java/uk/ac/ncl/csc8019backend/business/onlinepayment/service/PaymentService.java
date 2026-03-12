package uk.ac.ncl.csc8019backend.business.onlinepayment.service;

import uk.ac.ncl.csc8019backend.business.onlinepayment.DTO.request.HorsePayRequest;
import uk.ac.ncl.csc8019backend.business.onlinepayment.DTO.response.*;
import uk.ac.ncl.csc8019backend.business.onlinepayment.entity.Payment;
import uk.ac.ncl.csc8019backend.business.onlinepayment.repository.PaymentRepository;
import uk.ac.ncl.csc8019backend.business.onlinepayment.service.client.OrderServiceClient;
import uk.ac.ncl.csc8019backend.business.onlinepayment.service.client.UserServiceClient;
import uk.ac.ncl.csc8019backend.business.onlinepayment.service.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

/**
 * Core payment service - handles payment flow
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final HorsePayService horsePayService;
    private final OrderServiceClient orderServiceClient;
    private final UserServiceClient userServiceClient;

    /**
     * Execute payment (main entry point)
     *
     * @param orderId Order ID to pay
     * @param forceStatus Test mode: true=force success, false=force failure
     * @return Payment result
     */
    @Transactional
    public PaymentResult executePayment(Long orderId, Boolean forceStatus) {
        log.info("Executing payment for order: {}, forceStatus: {}", orderId, forceStatus);

        // Step 1: Get order info (via API)
        OrderDTO order = orderServiceClient.getOrder(orderId);
        if (order == null) {
            return buildErrorResult(orderId, null, "ORDER_NOT_FOUND", "Order not found: " + orderId);
        }

        // Step 2: Check if payable
        if (!order.isPayable()) {
            return buildErrorResult(orderId, order.getOrderNo(), "ORDER_NOT_PAYABLE",
                    "Order already paid or cancelled, current status: " + order.getPaymentStatus());
        }

        // Step 3: Get user info
        UserDTO user = userServiceClient.getUser(order.getCustomerId());
        String customerId = (user != null && user.getPhone() != null)
                ? user.getPhone()
                : order.getCustomerId().toString();

        // Step 4: Check 10th order free
        BigDecimal payableAmount = order.getPayableAmount();
        boolean isFreeOrder = user != null && user.getLoyaltyCount() != null && user.getLoyaltyCount() >= 9;

        if (isFreeOrder) {
            log.info("Applying 10th order free for user: {}", order.getCustomerId());
            payableAmount = BigDecimal.ZERO;
            orderServiceClient.applyDiscount(orderId, order.getTotalPrice());
        }

        // Step 5: Create payment record
        Payment payment = Payment.builder()
                .orderId(orderId)
                .amount(payableAmount)
                .status(0) // Pending
                .build();
        payment = paymentRepository.save(payment);

        // Step 6: Call HorsePay
        HorsePayResult horsePayResult;
        if (payableAmount.compareTo(BigDecimal.ZERO) == 0) {
            // Free order - simulate success
            horsePayResult = HorsePayResult.builder()
                    .success(true)
                    .message("Free order (10th loyalty)")
                    .transactionId("FREE-" + UUID.randomUUID())
                    .build();
        } else {
            HorsePayRequest request = HorsePayRequest.builder()
                    .storeId("Team99")
                    .customerId(customerId)
                    .amount(payableAmount)
                    .forceStatus(forceStatus)
                    .forceStatus(forceStatus)
                    .build();
            horsePayResult = horsePayService.processPayment(request);
        }

        // Step 7: Process result
        return handlePaymentResult(order, payment, horsePayResult, isFreeOrder);
    }

    /**
     * Handle payment result (success or failure)
     */
    private PaymentResult handlePaymentResult(OrderDTO order, Payment payment,
                                              HorsePayResult horsePayResult, boolean isFreeOrder) {
        payment.setHorsePayResponse(JsonUtils.toJson(horsePayResult));

        if (horsePayResult.isSuccess()) {
            // Success
            payment.setStatus(1); // Success
            payment.setPaidAt(LocalDateTime.now());
            payment.setPaymentNo(generatePaymentNo());
            paymentRepository.save(payment);

            // Notify order module
            boolean updated = orderServiceClient.updatePaymentStatus(
                    order.getId(), 1, payment.getPaymentNo());
            if (!updated) {
                log.error("Failed to update order payment status: {}", order.getId());
            }

            // Update loyalty
            if (!isFreeOrder) {
                userServiceClient.incrementLoyalty(order.getCustomerId());
            } else {
                userServiceClient.resetLoyalty(order.getCustomerId());
            }

            notifyStaffNewOrder(order);

            return PaymentResult.builder()
                    .success(true)
                    .code("SUCCESS")
                    .message(isFreeOrder ? "Free order (10th loyalty)" : "Payment successful")
                    .orderId(order.getId())
                    .orderNo(order.getOrderNo())
                    .amount(payment.getAmount())
                    .redirectUrl("/order/success/" + order.getId())
                    .timestamp(LocalDateTime.now())
                    .build();
        } else {
            // Failure
            payment.setStatus(2); // Failed
            payment.setFailureReason(horsePayResult.getMessage());
            paymentRepository.save(payment);

            orderServiceClient.updatePaymentStatus(order.getId(), 2, null);

            return PaymentResult.builder()
                    .success(false)
                    .code("PAYMENT_FAILED")
                    .message(horsePayResult.getMessage())
                    .orderId(order.getId())
                    .orderNo(order.getOrderNo())
                    .amount(payment.getAmount())
                    .redirectUrl("/order/payment-failed/" + order.getId())
                    .timestamp(LocalDateTime.now())
                    .build();
        }
    }

    /**
     * Query payment status (for frontend polling)
     */
    public PaymentStatusDTO getPaymentStatus(Long orderId) {
        Payment payment = paymentRepository.findTopByOrderIdOrderByCreatedAtDesc(orderId).orElse(null);

        if (payment != null) {
            return PaymentStatusDTO.builder()
                    .orderId(orderId)
                    .paymentStatus(payment.getStatus())
                    .amount(payment.getAmount())
                    .paidAt(payment.getPaidAt())
                    .failureReason(payment.getFailureReason())
                    .build();
        }

        OrderDTO order = orderServiceClient.getOrder(orderId);
        if (order == null) return null;

        return PaymentStatusDTO.builder()
                .orderId(orderId)
                .orderNo(order.getOrderNo())
                .paymentStatus(order.getPaymentStatus())
                .orderStatus(order.getOrderStatus())
                .build();
    }

    /**
     * Build error result
     */
    private PaymentResult buildErrorResult(Long orderId, String orderNo, String code, String message) {
        return PaymentResult.builder()
                .success(false)
                .code(code)
                .message(message)
                .orderId(orderId)
                .orderNo(orderNo)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Generate unique payment number
     */
    private String generatePaymentNo() {
        return "PAY" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + String.format("%04d", new Random().nextInt(10000));
    }

    /**
     * Notify staff about new order (TODO: implement WebSocket or message queue)
     */
    private void notifyStaffNewOrder(OrderDTO order) {
        log.info("Notifying staff: New paid order {} for pickup at {}",
                order.getOrderNo(), order.getPickupTime());
    }
}
