package uk.ac.ncl.csc8019backend.business.order.entity;

import jakarta.persistence.*;
import lombok.Data;
import uk.ac.ncl.csc8019backend.business.menu.entity.MenuSku;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menusku_id")
    private MenuSku menuSku;

    private Integer quantity;
    private BigDecimal priceAtPurchase;
}
