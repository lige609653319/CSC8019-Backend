package uk.ac.ncl.csc8019backend.business.order.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderResponseDTO {
    private Long id;
    private Long customerId;
    private BigDecimal totalPrice;
    private String orderDate;
    private String status;
    private String orderType;
    private List<OrderItemResponseDTO> items;
}
