package org.TBCS.model;

public enum OrderPriority {
    NORMAL("Normal Order"),
    PRIORITY("Priority Order");

    private final String displayName;

    OrderPriority(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}