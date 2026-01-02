package org.TBCS.model;

import java. math.BigDecimal;

public class OrderItem {
    private String itemId;
    private String itemName;
    private int quantity;
    private BigDecimal unitPrice;

    public OrderItem(String itemId, String itemName, int quantity, BigDecimal unitPrice) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }

    public BigDecimal getTotalPrice() {
        return unitPrice.multiply(BigDecimal. valueOf(quantity));
    }

    @Override
    public String toString() {
        return itemName + " x" + quantity + " @ Tk" + unitPrice + " = Tk" + getTotalPrice();
    }
}