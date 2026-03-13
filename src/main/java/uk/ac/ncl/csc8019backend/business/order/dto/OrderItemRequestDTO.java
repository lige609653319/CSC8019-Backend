package uk.ac.ncl.csc8019backend.business.order.dto;

import lombok.Data;

@Data
public class OrderItemRequestDTO {
    private Long id;
    private Integer quantity;
}
