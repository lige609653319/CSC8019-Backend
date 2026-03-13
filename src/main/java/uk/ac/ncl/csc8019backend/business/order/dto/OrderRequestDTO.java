package uk.ac.ncl.csc8019backend.business.order.dto;

import lombok.Data;
//import org.antlr.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import uk.ac.ncl.csc8019backend.business.order.enums.OrderType;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequestDTO {
    private Long customerId;
    private BigDecimal totalPrice;
    private OrderType orderType;
    private List<OrderItemRequestDTO> items;
}
