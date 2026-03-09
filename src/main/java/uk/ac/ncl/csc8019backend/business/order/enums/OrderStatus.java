package uk.ac.ncl.csc8019backend.business.order.enums;

import lombok.Getter;

// OrderStatus.java
@Getter
public enum OrderStatus {
    PENDING(0), PREPARING(1), COMPLETED(2), DELIVERED(3), CANCELLED(4);
    private final int value;
    OrderStatus(int value) { this.value = value; }

    public static OrderStatus fromValue(int v) {
        for (OrderStatus s : values()) if (s.value == v) return s;
        throw new IllegalArgumentException("Invalid status: " + v);
    }
}

