package org.TBCS.model;

public enum DeliveryStatus {
    PENDING("Pending Assignment"),
    ASSIGNED("Driver & Vehicle Assigned"),
    CHECKED_OUT("Driver Checked Out"),
    DELIVERED("Delivered to Customer");

    private final String displayName;

    DeliveryStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}