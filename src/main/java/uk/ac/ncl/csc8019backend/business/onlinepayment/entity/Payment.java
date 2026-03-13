package uk.ac.ncl.csc8019backend.business.onlinepayment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Payment entity - stores payment records
 */
@Entity
@Table(name = "payments", indexes = {
        @Index(name = "idx_order_id", columnList = "order_id"),
        @Index(name = "idx_status", columnList = "status")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    /**
     * Primary key - auto increment
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Associated order ID
     */
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    /**
     * Payment serial number (unique)
     */
    @Column(name = "payment_no", unique = true, length = 32)
    private String paymentNo;

    /**
     * Actual payment amount
     */
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    /**
     * Payment status: 0=pending, 1=success, 2=failed, 3=cancelled
     */
    @Column(name = "status", nullable = false)
    private Integer status;

    /**
     * Failure reason (when failed)
     */
    @Column(name = "failure_reason", length = 255)
    private String failureReason;

    /**
     * Complete HorsePay response JSON (for debugging)
     */
    @Column(name = "horse_pay_response", columnDefinition = "TEXT")
    private String horsePayResponse;

    /**
     * Payment success time
     */
    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    /**
     * Record creation time (not updatable)
     */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Auto set creation time before insert
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}