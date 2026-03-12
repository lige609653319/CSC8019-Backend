package uk.ac.ncl.csc8019backend.business.loyalty.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "loyalty_account",
        uniqueConstraints = @UniqueConstraint(name = "uk_loyalty_account_username", columnNames = "username")
)
public class LoyaltyAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String username;

    @Column(nullable = false)
    private Integer pointsBalance = 0;

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    public LoyaltyAccount() {
    }

    public LoyaltyAccount(String username) {
        this.username = username;
        this.pointsBalance = 0;
        this.updatedAt = Instant.now();
    }

    @PrePersist
    @PreUpdate
    void touch() {
        this.updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Integer getPointsBalance() {
        return pointsBalance;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPointsBalance(Integer pointsBalance) {
        this.pointsBalance = pointsBalance;
    }
}