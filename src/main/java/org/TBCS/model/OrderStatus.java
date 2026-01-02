package org.TBCS.model;

public enum OrderStatus {
    PLACED("Order Placed"),
    IN_QUEUE("In Queue"),
    PREPARING("Being Prepared"),
    READY("Ready for Delivery"),
    OUT_FOR_DELIVERY("Out for Delivery"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}