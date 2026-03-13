package uk.ac.ncl.csc8019backend.business.onlinepayment.DTO.response;

import lombok.Data;
import java.math.BigDecimal;

/**
 * Order item (obtained from order module)
 */
@Data
public class OrderItemDTO {

    private Long productId;
    private String productName;    // Product name (redundant for display)
    private Integer size;          // 0=Regular, 1=Large
    private Integer quantity;
    private BigDecimal unitPrice;  // Unit price at order time
    private BigDecimal subtotal;   // Subtotal
}