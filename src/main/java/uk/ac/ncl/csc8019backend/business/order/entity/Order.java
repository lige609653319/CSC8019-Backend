package uk.ac.ncl.csc8019backend.business.order.entity;

import jakarta.persistence.*;
import lombok.Data;
import uk.ac.ncl.csc8019backend.business.order.converter.OrderStatusConverter;
import uk.ac.ncl.csc8019backend.business.order.converter.OrderTypeConverter;
import uk.ac.ncl.csc8019backend.business.order.enums.OrderStatus;
import uk.ac.ncl.csc8019backend.business.order.enums.OrderType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    // This field is automatically managed by the database (current timestamp)
    @Column(name = "order_date", insertable = false, updatable = false)
    private LocalDateTime orderDate;

    // Uses the Converter to map the Enum to an Integer in the DB
    @Convert(converter = OrderStatusConverter.class)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    // Uses the Converter to map the Enum to an Integer in the DB
    @Convert(converter = OrderTypeConverter.class)
    @Column(name = "order_type", nullable = false)
    private OrderType orderType;

    // Maps the relationship to the items inside this order
    // cascade = CascadeType.ALL ensures that deleting an order deletes its items
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> items;
}