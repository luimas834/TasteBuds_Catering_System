package org.TBCS.model;

import java. time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Delivery {
    private String deliveryId;
    private Order order;
    private Driver driver;
    private Vehicle vehicle;
    private DeliveryStatus status;
    private LocalDateTime assignedTime;
    private LocalDateTime checkoutTime;
    private LocalDateTime deliveredTime;
    private boolean isPriorityDelivery;

    public Delivery(Order order) {
        this.deliveryId = UUID.randomUUID().toString().substring(0, 8);
        this.order = order;
        this.status = DeliveryStatus.PENDING;
        this.isPriorityDelivery = (order.getPriority() == OrderPriority.PRIORITY);
    }

    public void assignDriverAndVehicle(Driver driver, Vehicle vehicle) {
        this.driver = driver;
        this.vehicle = vehicle;
        this.assignedTime = LocalDateTime.now();
        this.status = DeliveryStatus. ASSIGNED;

        // Update order
        order.setAssignedDriverId(driver.getDriverId());
        order.setAssignedVehicleId(vehicle.getVehicleId());
        order.setDeliveryAssignedTime(this.assignedTime);
    }

    public boolean checkoutWithLicense(String licenseNumber) {
        if (driver != null &&
                driver.getDrivingLicense().getLicenseNumber().equals(licenseNumber)) {
            this.checkoutTime = LocalDateTime.now();
            this.status = DeliveryStatus.CHECKED_OUT;
            order.setCheckoutTime(this.checkoutTime);
            order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
            return true;
        }
        return false;
    }

    public void markDelivered() {
        this.deliveredTime = LocalDateTime. now();
        this.status = DeliveryStatus.DELIVERED;
        order.setDeliveredTime(this.deliveredTime);
        order.setStatus(OrderStatus. DELIVERED);
    }

    public long getMinutesSinceReady() {
        if (order.getReadyTime() != null) {
            return ChronoUnit.MINUTES.between(order.getReadyTime(), LocalDateTime.now());
        }
        return 0;
    }

    public boolean isWithinPriorityDeadline() {
        return getMinutesSinceReady() <= 10;
    }

    // Getters
    public String getDeliveryId() { return deliveryId; }
    public Order getOrder() { return order; }
    public Driver getDriver() { return driver; }
    public Vehicle getVehicle() { return vehicle; }
    public DeliveryStatus getStatus() { return status; }
    public LocalDateTime getAssignedTime() { return assignedTime; }
    public LocalDateTime getCheckoutTime() { return checkoutTime; }
    public LocalDateTime getDeliveredTime() { return deliveredTime; }
    public boolean isPriorityDelivery() { return isPriorityDelivery; }
}