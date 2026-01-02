package org.TBCS.model;

import java. math.BigDecimal;

public class MenuItem {
    private String itemId;
    private String name;
    private String category;
    private BigDecimal price;
    private boolean available;

    public MenuItem(String itemId, String name, String category, BigDecimal price) {
        this.itemId = itemId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.available = true;
    }

    public MenuItem(String itemId, String name, String category, BigDecimal price, boolean available) {
        this.itemId = itemId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.available = available;
    }

    public String getItemId() { return itemId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public BigDecimal getPrice() { return price; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s - Tk%s%s",
                itemId, name, category, price, available ? "" : " [Unavailable]");
    }
}