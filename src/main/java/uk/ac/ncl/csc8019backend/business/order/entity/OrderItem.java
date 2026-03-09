package uk.ac.ncl.csc8019backend.business.order.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menusku_id", nullable = false)
    private MenuSku menuSku;

    private Integer quantity;
    private BigDecimal priceAtPurchase;
}
