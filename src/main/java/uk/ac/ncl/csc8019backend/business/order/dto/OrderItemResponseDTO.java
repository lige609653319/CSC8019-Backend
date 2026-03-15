package uk.ac.ncl.csc8019backend.business.order.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class OrderItemResponseDTO {
    private String menuName;
    private Integer quantity;
    private BigDecimal priceAtPurchase;
}
