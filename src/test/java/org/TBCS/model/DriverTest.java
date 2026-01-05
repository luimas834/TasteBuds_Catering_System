package org. TBCS.model;

import org. junit.jupiter.api.BeforeEach;
import org. junit.jupiter.api.DisplayName;
import org.junit. jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter. api.Assertions.*;

public class DriverTest {

    private Driver driver;
    private DrivingLicense validLicense;
    private DrivingLicense expiredLicense;

    @BeforeEach
    void setUp() {
        validLicense = new DrivingLicense("DL-2024-001",
                LocalDate.now().minusYears(1),
                LocalDate.now().plusYears(4),
                "Commercial");

        expiredLicense = new DrivingLicense("DL-2020-001",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2023, 1, 1),
                "Commercial");

        driver = new Driver("Salam", "01898765432", validLicense);
    }

    @Test
    @DisplayName("Should create driver with correct initial values")
    void testDriverCreation() {
        assertNotNull(driver. getDriverId());
        assertEquals("Salam", driver.getName());
        assertEquals("01898765432", driver.getPhone());
        assertEquals(validLicense, driver. getDrivingLicense());
        assertTrue(driver.isAvailable());
    }

    @Test
    @DisplayName("Should validate driver has valid license")
    void testHasValidLicense() {
        assertTrue(driver.hasValidLicense());

        Driver expiredDriver = new Driver("Test", "0000", expiredLicense);
        assertFalse(expiredDriver.hasValidLicense());
    }

    @Test
    @DisplayName("Should update driver availability")
    void testDriverAvailability() {
        assertTrue(driver.isAvailable());

        driver.setAvailable(false);
        assertFalse(driver.isAvailable());

        driver.setAvailable(true);
        assertTrue(driver.isAvailable());
    }

    @Test
    @DisplayName("Should return false for null license")
    void testNullLicense() {
        Driver driverWithoutLicense = new Driver("NoLicense", "01712345678", null);
        assertFalse(driverWithoutLicense.hasValidLicense());
    }
}