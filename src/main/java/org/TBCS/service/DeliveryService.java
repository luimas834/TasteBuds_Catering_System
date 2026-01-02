package org. TBCS.service;

import org.TBCS.model.*;
import org.TBCS. repository.*;

import java.util. List;

public class DeliveryService {
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final OrderRepository orderRepository;

    public DeliveryService(DriverRepository driverRepository, VehicleRepository vehicleRepository,
                           OrderRepository orderRepository) {
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
        this.orderRepository = orderRepository;
    }

    public Delivery createDelivery(Order order) {
        Delivery delivery = new Delivery(order);
        orderRepository.saveDelivery(delivery);
        return delivery;
    }

    public boolean assignDelivery(Delivery delivery, Driver driver, Vehicle vehicle) {
        if (! driver.isAvailable() || !driver.hasValidLicense()) {
            return false;
        }
        if (!vehicle.isAvailable()) {
            return false;
        }

        // Mark as unavailable
        driver.setAvailable(false);
        vehicle.setAvailable(false);
        driverRepository.save(driver);
        vehicleRepository.save(vehicle);

        // Assign to delivery
        delivery.assignDriverAndVehicle(driver, vehicle);

        return true;
    }

    public boolean driverCheckout(Delivery delivery, String licenseNumber) {
        boolean success = delivery.checkoutWithLicense(licenseNumber);
        if (success) {
            orderRepository.save(delivery.getOrder());
        }
        return success;
    }

    public void markDelivered(Delivery delivery) {
        delivery.markDelivered();

        // Release driver and vehicle
        Driver driver = delivery.getDriver();
        Vehicle vehicle = delivery.getVehicle();

        if (driver != null) {
            driver.setAvailable(true);
            driverRepository.save(driver);
        }
        if (vehicle != null) {
            vehicle.setAvailable(true);
            vehicleRepository.save(vehicle);
        }

        orderRepository.save(delivery.getOrder());
    }

    public Delivery getDeliveryByQueueNumber(int queueNumber) {
        return orderRepository.findDeliveryByQueueNumber(queueNumber);
    }

    public List<Driver> getAvailableDrivers() {
        return driverRepository. findAvailable();
    }

    public List<Vehicle> getAvailableVehicles() {
        return vehicleRepository.findAvailable();
    }
}