package uk.ac.ncl.csc8019backend.business.loyalty.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "loyalty_transaction",
        indexes = {
                @Index(name = "idx_loyalty_tx_username", columnList = "username"),
                @Index(name = "idx_loyalty_tx_createdAt", columnList = "createdAt")
        }
)
public class LoyaltyTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String username;

    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LoyaltyTransactionType type;

    @Column(nullable = false)
    private Integer points;

    @Column(length = 255)
    private String note;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    public LoyaltyTransaction() {
    }

    public LoyaltyTransaction(String username, Long orderId, LoyaltyTransactionType type, Integer points, String note) {
        this.username = username;
        this.orderId = orderId;
        this.type = type;
        this.points = points;
        this.note = note;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Long getOrderId() {
        return orderId;
    }

    public LoyaltyTransactionType getType() {
        return type;
    }

    public Integer getPoints() {
        return points;
    }

    public String getNote() {
        return note;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}