package org.TBCS.model;

import java. math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private String orderId;
    private int queueNumber;
    private Customer customer;
    private List<OrderItem> items;
    private LocalDateTime orderTime;
    private OrderStatus status;
    private OrderPriority priority;
    private BigDecimal totalAmount;
    private BigDecimal discount;
    private BigDecimal finalAmount;

    // Kitchen related
    private List<String> assignedChefIds;
    private int estimatedMinutes;
    private LocalDateTime readyTime;

    // Delivery related
    private String assignedDriverId;
    private String assignedVehicleId;
    private LocalDateTime deliveryAssignedTime;
    private LocalDateTime checkoutTime;
    private LocalDateTime deliveredTime;

    // Feedback
    private int rating;
    private String feedback;

    public Order(int queueNumber, Customer customer) {
        this.orderId = "ORD-" + System.currentTimeMillis();
        this.queueNumber = queueNumber;
        this.customer = customer;
        this.items = new ArrayList<>();
        this.assignedChefIds = new ArrayList<>();
        this.orderTime = LocalDateTime.now();
        this.status = OrderStatus.PLACED;
        this.priority = OrderPriority.NORMAL;
        this.totalAmount = BigDecimal.ZERO;
        this. discount = BigDecimal.ZERO;
        this.finalAmount = BigDecimal.ZERO;
        this.rating = 0;
    }

    public void addItem(MenuItem menuItem, int quantity) {
        items.add(new OrderItem(menuItem.getItemId(), menuItem.getName(), quantity, menuItem.getPrice()));
        calculateTotal();
    }

    private void calculateTotal() {
        this.totalAmount = items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal. ZERO, BigDecimal::add);
        this.finalAmount = totalAmount.subtract(discount);
    }

    public void applyDiscount(BigDecimal discountAmount) {
        this.discount = discountAmount;
        this.finalAmount = totalAmount.subtract(discount);
    }

    public void addChef(String chefId) {
        if (!assignedChefIds.contains(chefId)) {
            assignedChefIds.add(chefId);
        }
    }

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public int getQueueNumber() { return queueNumber; }
    public void setQueueNumber(int queueNumber) { this.queueNumber = queueNumber; }
    public Customer getCustomer() { return customer; }
    public List<OrderItem> getItems() { return new ArrayList<>(items); }
    public LocalDateTime getOrderTime() { return orderTime; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public OrderPriority getPriority() { return priority; }
    public void setPriority(OrderPriority priority) { this.priority = priority; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public BigDecimal getDiscount() { return discount; }
    public BigDecimal getFinalAmount() { return finalAmount; }

    public List<String> getAssignedChefIds() { return new ArrayList<>(assignedChefIds); }
    public int getEstimatedMinutes() { return estimatedMinutes; }
    public void setEstimatedMinutes(int minutes) { this.estimatedMinutes = minutes; }
    public LocalDateTime getReadyTime() { return readyTime; }
    public void setReadyTime(LocalDateTime time) { this.readyTime = time; }

    public String getAssignedDriverId() { return assignedDriverId; }
    public void setAssignedDriverId(String driverId) { this.assignedDriverId = driverId; }
    public String getAssignedVehicleId() { return assignedVehicleId; }
    public void setAssignedVehicleId(String vehicleId) { this.assignedVehicleId = vehicleId; }
    public LocalDateTime getDeliveryAssignedTime() { return deliveryAssignedTime; }
    public void setDeliveryAssignedTime(LocalDateTime time) { this.deliveryAssignedTime = time; }
    public LocalDateTime getCheckoutTime() { return checkoutTime; }
    public void setCheckoutTime(LocalDateTime time) { this.checkoutTime = time; }
    public LocalDateTime getDeliveredTime() { return deliveredTime; }
    public void setDeliveredTime(LocalDateTime time) { this.deliveredTime = time; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public String getFormattedOrderTime() {
        return orderTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}