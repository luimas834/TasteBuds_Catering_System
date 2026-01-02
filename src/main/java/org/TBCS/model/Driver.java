package org.TBCS.model;

import java. util.UUID;

public class Driver {
    private String driverId;
    private String name;
    private String phone;
    private DrivingLicense drivingLicense;
    private boolean available;

    public Driver(String name, String phone, DrivingLicense drivingLicense) {
        this.driverId = UUID.randomUUID().toString().substring(0, 8);
        this.name = name;
        this.phone = phone;
        this.drivingLicense = drivingLicense;
        this.available = true;
    }

    public Driver(String driverId, String name, String phone,
                  DrivingLicense drivingLicense, boolean available) {
        this.driverId = driverId;
        this.name = name;
        this.phone = phone;
        this.drivingLicense = drivingLicense;
        this.available = available;
    }

    public boolean hasValidLicense() {
        return drivingLicense != null && drivingLicense. isValid();
    }

    public String getDriverId() { return driverId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public DrivingLicense getDrivingLicense() { return drivingLicense; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s%s",
                driverId, name, drivingLicense. getLicenseNumber(),
                available ? "" : " [On Delivery]");
    }
}