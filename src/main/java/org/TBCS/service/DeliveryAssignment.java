package org.TBCS.service;

import org. TBCS.model.Delivery;


public class DeliveryAssignment {
    private final Delivery delivery;
    private final boolean isPriority;
    private final int assignmentDeadlineMinutes;

    public DeliveryAssignment(Delivery delivery, boolean isPriority,
                              int assignmentDeadlineMinutes) {
        this.delivery = delivery;
        this.isPriority = isPriority;
        this.assignmentDeadlineMinutes = assignmentDeadlineMinutes;
    }

    public Delivery getDelivery() { return delivery; }
    public boolean isPriority() { return isPriority; }
    public int getAssignmentDeadlineMinutes() { return assignmentDeadlineMinutes; }

    @Override
    public String toString() {
        if (delivery. getDriver() == null) {
            return "Delivery pending - waiting for available driver/vehicle";
        }

        return String.format(
                "=== Delivery Assignment ===\n" +
                        "Order #%d (%s)\n" +
                        "Driver: %s\n" +
                        "License:  %s\n" +
                        "Vehicle: %s (%s)\n" +
                        "Assignment Time: %s\n" +
                        "Priority: %s",
                delivery.getOrder().getQueueNumber(),
                delivery.getOrder().getPriority(),
                delivery.getDriver().getName(),
                delivery. getDriver().getDrivingLicense().getLicenseNumber(),
                delivery. getVehicle().getRegistrationNumber(),
                delivery.getVehicle().getVehicleType(),
                delivery.getAssignedTime(),  // FIXED: was getAssignmentTime()
                isPriority ? "Yes (within " + assignmentDeadlineMinutes + " min)" : "Normal"
        );
    }
}