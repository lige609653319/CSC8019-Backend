package uk.ac.ncl.csc8019backend.business.order.enums;

public enum OrderType {
    DINE_IN(1), TAKEAWAY(2);
    private final int value;
    OrderType(int value) { this.value = value; }
    public int getValue() { return value; }

    public static OrderType fromValue(int v) {
        for (OrderType t : values()) if (t.value == v) return t;
        throw new IllegalArgumentException("Invalid type: " + v);
    }
}
