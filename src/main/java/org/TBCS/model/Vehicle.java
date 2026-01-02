package org.TBCS.model;

import java. util.UUID;

public class Vehicle {
    private String vehicleId;
    private String registrationNumber;
    private String vehicleType;
    private boolean available;

    public Vehicle(String registrationNumber, String vehicleType) {
        this.vehicleId = UUID.randomUUID().toString().substring(0, 8);
        this.registrationNumber = registrationNumber;
        this. vehicleType = vehicleType;
        this.available = true;
    }

    public Vehicle(String vehicleId, String registrationNumber,
                   String vehicleType, boolean available) {
        this.vehicleId = vehicleId;
        this.registrationNumber = registrationNumber;
        this. vehicleType = vehicleType;
        this.available = available;
    }

    public String getVehicleId() { return vehicleId; }
    public String getRegistrationNumber() { return registrationNumber; }
    public String getVehicleType() { return vehicleType; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s)%s",
                vehicleId, registrationNumber, vehicleType,
                available ? "" : " [In Use]");
    }
}