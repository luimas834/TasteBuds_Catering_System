package org. TBCS.model;

import org.junit.jupiter.api. BeforeEach;
import org.junit.jupiter.api. DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter. api.Assertions.*;

/**
 * Unit tests for Delivery model class.
 * Tests delivery assignment, checkout, and delivery completion.
 */
public class DeliveryTest {

    private Customer customer;
    private Order normalOrder;
    private Order priorityOrder;
    private Delivery delivery;
    private Driver driver;
    private Vehicle vehicle;
    private DrivingLicense validLicense;

    @BeforeEach
    void setUp() {
        customer = new Customer("Test Customer", "01712345678", "test@email.com", true);
        normalOrder = new Order(1, customer);
        priorityOrder = new Order(2, customer);
        priorityOrder.setPriority(OrderPriority.PRIORITY);

        validLicense = new DrivingLicense("DL-2024-001",
                LocalDate.now().minusYears(1),
                LocalDate.now().plusYears(4),
                "Commercial");

        driver = new Driver("Salam", "01898765432", validLicense);
        vehicle = new Vehicle("DHK-1234", "Motorcycle");

        delivery = new Delivery(normalOrder);
    }

    @Test
    @DisplayName("Should create delivery with correct initial values")
    void testDeliveryCreation() {
        assertNotNull(delivery.getDeliveryId());
        assertEquals(normalOrder, delivery. getOrder());
        assertEquals(DeliveryStatus. PENDING, delivery.getStatus());
        assertFalse(delivery. isPriorityDelivery());
        assertNull(delivery.getDriver());
        assertNull(delivery.getVehicle());
    }

    @Test
    @DisplayName("Should identify priority delivery correctly")
    void testPriorityDelivery() {
        Delivery priorityDelivery = new Delivery(priorityOrder);
        assertTrue(priorityDelivery. isPriorityDelivery());
    }

    @Test
    @DisplayName("Should assign driver and vehicle correctly")
    void testAssignDriverAndVehicle() {
        delivery.assignDriverAndVehicle(driver, vehicle);

        assertEquals(driver, delivery.getDriver());
        assertEquals(vehicle, delivery.getVehicle());
        assertEquals(DeliveryStatus.ASSIGNED, delivery.getStatus());
        assertNotNull(delivery.getAssignedTime());

        // Verify order is updated
        assertEquals(driver.getDriverId(), normalOrder.getAssignedDriverId());
        assertEquals(vehicle. getVehicleId(), normalOrder.getAssignedVehicleId());
    }

    @Test
    @DisplayName("Should checkout with valid license")
    void testCheckoutWithValidLicense() {
        delivery. assignDriverAndVehicle(driver, vehicle);

        boolean result = delivery.checkoutWithLicense("DL-2024-001");

        assertTrue(result);
        assertEquals(DeliveryStatus. CHECKED_OUT, delivery.getStatus());
        assertNotNull(delivery.getCheckoutTime());
        assertEquals(OrderStatus.OUT_FOR_DELIVERY, normalOrder.getStatus());
    }

    @Test
    @DisplayName("Should fail checkout with invalid license")
    void testCheckoutWithInvalidLicense() {
        delivery.assignDriverAndVehicle(driver, vehicle);

        boolean result = delivery.checkoutWithLicense("WRONG-LICENSE");

        assertFalse(result);
        assertEquals(DeliveryStatus.ASSIGNED, delivery.getStatus());
    }

    @Test
    @DisplayName("Should mark delivery as delivered")
    void testMarkDelivered() {
        delivery.assignDriverAndVehicle(driver, vehicle);
        delivery.checkoutWithLicense("DL-2024-001");

        delivery.markDelivered();

        assertEquals(DeliveryStatus. DELIVERED, delivery. getStatus());
        assertNotNull(delivery. getDeliveredTime());
        assertEquals(OrderStatus.DELIVERED, normalOrder.getStatus());
    }
}