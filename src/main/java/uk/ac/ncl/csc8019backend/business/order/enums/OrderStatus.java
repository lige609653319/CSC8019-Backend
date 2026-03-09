package uk.ac.ncl.csc8019backend.business.order.enums;

import lombok.Getter;

// OrderStatus.java
@Getter
public enum OrderStatus {
    PENDING(1), PREPARING(2), COMPLETED(3), DELIVERED(4), CANCELLED(5);
    private final int value;
    OrderStatus(int value) { this.value = value; }

    public static OrderStatus fromValue(int v) {
        for (OrderStatus s : values()) if (s.value == v) return s;
        throw new IllegalArgumentException("Invalid status: " + v);
    }
}

