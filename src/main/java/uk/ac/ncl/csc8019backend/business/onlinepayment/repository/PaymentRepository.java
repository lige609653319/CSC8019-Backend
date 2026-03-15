package uk.ac.ncl.csc8019backend.business.onlinepayment.repository;

import uk.ac.ncl.csc8019backend.business.onlinepayment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Payment data access layer
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Find latest payment record by order ID
     */
    Optional<Payment> findTopByOrderIdOrderByCreatedAtDesc(Long orderId);

    /**
     * Find all payment attempts by order ID (for history)
     */
    List<Payment> findByOrderIdOrderByCreatedAtDesc(Long orderId);

    /**
     * Find payments by status and creation time before (for timeout check)
     */
    List<Payment> findByStatusAndCreatedAtBefore(Integer status, LocalDateTime before);

    /**
     * Check if successful payment exists (prevent duplicate payment)
     */
    boolean existsByOrderIdAndStatus(Long orderId, Integer status);

    /**
     * Find payment by payment serial number
     */
    Optional<Payment> findByPaymentNo(String paymentNo);
}