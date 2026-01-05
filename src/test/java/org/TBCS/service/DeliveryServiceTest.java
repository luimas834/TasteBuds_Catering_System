package org.TBCS.service;

import org.TBCS. model.*;
import org.TBCS. repository.*;
import org.junit. jupiter.api.*;

import java.util. List;

import static org.junit.jupiter. api.Assertions.*;

public class DeliveryServiceTest {

    private DeliveryService deliveryService;
    private DriverRepository driverRepository;
    private VehicleRepository vehicleRepository;
    private OrderRepository orderRepository;
    private Customer customer;
    private org.TBCS.model.Order order;

    @BeforeEach
    void setUp() {
        driverRepository = new DriverRepository();
        vehicleRepository = new VehicleRepository();
        orderRepository = new OrderRepository();
        deliveryService = new DeliveryService(driverRepository, vehicleRepository, orderRepository);

        customer = new Customer("Test Customer", "01712345678", "test@email.com", true);
        order = new org. TBCS.model.Order(1, customer);
        order.setStatus(OrderStatus. READY);

        // Reset all drivers and vehicles to available before each test
        resetAllResourcesToAvailable();
    }

    @AfterEach
    void tearDown() {
        // Reset all drivers and vehicles to available after each test
        resetAllResourcesToAvailable();
    }

    private void resetAllResourcesToAvailable() {
        // Reset all drivers
        List<Driver> allDrivers = driverRepository.findAll();
        for (Driver driver : allDrivers) {
            driver.setAvailable(true);
            driverRepository.save(driver);
        }

        // Reset all vehicles
        List<Vehicle> allVehicles = vehicleRepository.findAll();
        for (Vehicle vehicle : allVehicles) {
            vehicle.setAvailable(true);
            vehicleRepository.save(vehicle);
        }
    }

    @Test
    @DisplayName("Scenario: Delivery Assignment - Create delivery for ready order")
    void testCreateDelivery() {
        Delivery delivery = deliveryService.createDelivery(order);

        assertNotNull(delivery);
        assertNotNull(delivery.getDeliveryId());
        assertEquals(order, delivery.getOrder());
        assertEquals(DeliveryStatus.PENDING, delivery.getStatus());
    }

    @Test
    @DisplayName("Scenario: Delivery Assignment - Assign available driver and vehicle")
    void testAssignDelivery() {
        Delivery delivery = deliveryService.createDelivery(order);

        List<Driver> availableDrivers = deliveryService.getAvailableDrivers();
        List<Vehicle> availableVehicles = deliveryService.getAvailableVehicles();

        assertFalse(availableDrivers.isEmpty(), "Should have available drivers");
        assertFalse(availableVehicles.isEmpty(), "Should have available vehicles");

        Driver driver = availableDrivers.get(0);
        Vehicle vehicle = availableVehicles. get(0);

        boolean assigned = deliveryService.assignDelivery(delivery, driver, vehicle);

        assertTrue(assigned);
        assertEquals(driver, delivery.getDriver());
        assertEquals(vehicle, delivery.getVehicle());
        assertEquals(DeliveryStatus. ASSIGNED, delivery.getStatus());
    }

    @Test
    @DisplayName("Scenario:  Delivery Assignment - Driver and vehicle become unavailable after assignment")
    void testResourcesUnavailableAfterAssignment() {
        Delivery delivery = deliveryService.createDelivery(order);

        List<Driver> availableDrivers = deliveryService.getAvailableDrivers();
        List<Vehicle> availableVehicles = deliveryService. getAvailableVehicles();

        assertFalse(availableDrivers.isEmpty(), "Should have available drivers");
        assertFalse(availableVehicles.isEmpty(), "Should have available vehicles");

        Driver driver = availableDrivers.get(0);
        Vehicle vehicle = availableVehicles.get(0);
        String driverId = driver.getDriverId();
        String vehicleId = vehicle. getVehicleId();

        deliveryService.assignDelivery(delivery, driver, vehicle);

        // Verify they are no longer in available lists
        List<Driver> availableDriversAfter = deliveryService.getAvailableDrivers();
        List<Vehicle> availableVehiclesAfter = deliveryService. getAvailableVehicles();

        assertFalse(availableDriversAfter.stream().anyMatch(d -> d.getDriverId().equals(driverId)));
        assertFalse(availableVehiclesAfter.stream().anyMatch(v -> v.getVehicleId().equals(vehicleId)));
    }

    @Test
    @DisplayName("Scenario: Delivery Assignment - Reject unavailable driver")
    void testRejectUnavailableDriver() {
        Delivery delivery = deliveryService.createDelivery(order);

        List<Driver> availableDrivers = deliveryService. getAvailableDrivers();
        List<Vehicle> availableVehicles = deliveryService.getAvailableVehicles();

        assertFalse(availableDrivers.isEmpty(), "Should have available drivers");
        assertFalse(availableVehicles.isEmpty(), "Should have available vehicles");

        Driver driver = availableDrivers. get(0);
        Vehicle vehicle = availableVehicles.get(0);

        // Mark driver as unavailable before assignment
        driver.setAvailable(false);

        boolean assigned = deliveryService. assignDelivery(delivery, driver, vehicle);

        assertFalse(assigned);
    }

    @Test
    @DisplayName("Scenario: Driver Checkout - Successful checkout with valid license")
    void testDriverCheckout() {
        Delivery delivery = deliveryService.createDelivery(order);

        List<Driver> availableDrivers = deliveryService.getAvailableDrivers();
        List<Vehicle> availableVehicles = deliveryService.getAvailableVehicles();

        assertFalse(availableDrivers.isEmpty(), "Should have available drivers");
        assertFalse(availableVehicles.isEmpty(), "Should have available vehicles");

        Driver driver = availableDrivers.get(0);
        Vehicle vehicle = availableVehicles.get(0);
        String licenseNumber = driver.getDrivingLicense().getLicenseNumber();

        deliveryService.assignDelivery(delivery, driver, vehicle);

        boolean checkoutSuccess = deliveryService. driverCheckout(delivery, licenseNumber);

        assertTrue(checkoutSuccess);
        assertEquals(DeliveryStatus.CHECKED_OUT, delivery.getStatus());
        assertEquals(OrderStatus.OUT_FOR_DELIVERY, order. getStatus());
    }

    @Test
    @DisplayName("Scenario: Driver Checkout - Failed checkout with wrong license")
    void testDriverCheckoutFailsWithWrongLicense() {
        Delivery delivery = deliveryService.createDelivery(order);

        List<Driver> availableDrivers = deliveryService.getAvailableDrivers();
        List<Vehicle> availableVehicles = deliveryService.getAvailableVehicles();

        assertFalse(availableDrivers.isEmpty(), "Should have available drivers");
        assertFalse(availableVehicles.isEmpty(), "Should have available vehicles");

        Driver driver = availableDrivers.get(0);
        Vehicle vehicle = availableVehicles. get(0);

        deliveryService.assignDelivery(delivery, driver, vehicle);

        boolean checkoutSuccess = deliveryService.driverCheckout(delivery, "INVALID-LICENSE");

        assertFalse(checkoutSuccess);
        assertEquals(DeliveryStatus. ASSIGNED, delivery.getStatus());
    }

    @Test
    @DisplayName("Scenario:  Delivery Assignment - Mark delivery as delivered releases resources")
    void testMarkDeliveredReleasesResources() {
        Delivery delivery = deliveryService. createDelivery(order);

        List<Driver> availableDrivers = deliveryService.getAvailableDrivers();
        List<Vehicle> availableVehicles = deliveryService.getAvailableVehicles();

        assertFalse(availableDrivers.isEmpty(), "Should have available drivers");
        assertFalse(availableVehicles.isEmpty(), "Should have available vehicles");

        Driver driver = availableDrivers. get(0);
        Vehicle vehicle = availableVehicles.get(0);
        String driverId = driver. getDriverId();
        String vehicleId = vehicle.getVehicleId();

        deliveryService.assignDelivery(delivery, driver, vehicle);
        deliveryService. driverCheckout(delivery, driver.getDrivingLicense().getLicenseNumber());

        deliveryService.markDelivered(delivery);

        assertEquals(DeliveryStatus.DELIVERED, delivery.getStatus());
        assertEquals(OrderStatus.DELIVERED, order.getStatus());

        // Driver and vehicle should be available again
        Driver updatedDriver = driverRepository.findById(driverId);
        Vehicle updatedVehicle = vehicleRepository.findById(vehicleId);

        assertTrue(updatedDriver.isAvailable());
        assertTrue(updatedVehicle.isAvailable());
    }

    @Test
    @DisplayName("Scenario:  Delivery Assignment - Priority order identification")
    void testPriorityOrderDelivery() {
        order.setPriority(OrderPriority. PRIORITY);
        Delivery delivery = deliveryService.createDelivery(order);

        assertTrue(delivery.isPriorityDelivery());
    }
}