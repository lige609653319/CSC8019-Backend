package uk.ac.ncl.csc8019backend.business.onlinepayment.DTO.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Order information (obtained from order module)
 */
@Data
public class OrderDTO {

    private Long id;
    private String orderNo;
    private Long customerId;
    private BigDecimal totalPrice;       // Original total price
    private BigDecimal discountAmount;   // Discount amount
    private LocalDateTime pickupTime;    // Pickup time
    private Integer paymentStatus;       // 0=unpaid, 1=paid, 2=failed, 3=refunded
    private Integer orderStatus;         // Staff-side status
    private List<OrderItemDTO> items;    // Order items

    /**
     * Calculate payable amount
     */
    public BigDecimal getPayableAmount() {
        BigDecimal discount = discountAmount != null ? discountAmount : BigDecimal.ZERO;
        return totalPrice.subtract(discount);
    }

    /**
     * Whether payable
     */
    public boolean isPayable() {
        return paymentStatus != null && paymentStatus == 0;
    }
}