package uk.ac.ncl.csc8019backend.business.order.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import uk.ac.ncl.csc8019backend.business.order.converter.OrderStatusConverter;
import uk.ac.ncl.csc8019backend.business.order.converter.OrderTypeConverter;
import uk.ac.ncl.csc8019backend.business.order.enums.OrderStatus;
import uk.ac.ncl.csc8019backend.business.order.enums.OrderType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Convert(converter = OrderStatusConverter.class)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Convert(converter = OrderTypeConverter.class)
    @Column(name = "order_type", nullable = false)
    private OrderType orderType;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrderItem> items;
}