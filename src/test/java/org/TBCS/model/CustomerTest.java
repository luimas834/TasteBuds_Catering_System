package org.TBCS.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter. api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class CustomerTest {

    private Customer registeredCustomer;
    private Customer unregisteredCustomer;

    @BeforeEach
    void setUp() {
        registeredCustomer = new Customer("John Doe", "01712345678", "john@email.com", true);
        unregisteredCustomer = new Customer("Jane Doe", "01898765432", "jane@email.com", false);
    }

    @Test
    @DisplayName("Should create customer with correct initial values")
    void testCustomerCreation() {
        assertNotNull(registeredCustomer. getCustomerId());
        assertEquals("John Doe", registeredCustomer. getName());
        assertEquals("01712345678", registeredCustomer. getPhone());
        assertEquals("john@email.com", registeredCustomer.getEmail());
        assertTrue(registeredCustomer.isRegistered());
        assertEquals(0, registeredCustomer.getMonthlyOrderCount());
        assertEquals(0, registeredCustomer.getTotalOrderCount());
    }

    @Test
    @DisplayName("Should distinguish between registered and unregistered customers")
    void testRegisteredStatus() {
        assertTrue(registeredCustomer.isRegistered());
        assertFalse(unregisteredCustomer.isRegistered());
    }

    @Test
    @DisplayName("Should increment order count correctly")
    void testIncrementOrderCount() {
        assertEquals(0, registeredCustomer.getMonthlyOrderCount());
        assertEquals(0, registeredCustomer.getTotalOrderCount());

        registeredCustomer. incrementOrderCount();

        assertEquals(1, registeredCustomer.getMonthlyOrderCount());
        assertEquals(1, registeredCustomer. getTotalOrderCount());

        registeredCustomer.incrementOrderCount();

        assertEquals(2, registeredCustomer.getMonthlyOrderCount());
        assertEquals(2, registeredCustomer.getTotalOrderCount());
    }

    @Test
    @DisplayName("Should generate unique customer IDs")
    void testUniqueCustomerIds() {
        Customer customer1 = new Customer("Test1", "111", "test1@email.com", true);
        Customer customer2 = new Customer("Test2", "222", "test2@email.com", true);

        assertNotEquals(customer1.getCustomerId(), customer2.getCustomerId());
    }

    @Test
    @DisplayName("Should correctly construct customer with all parameters")
    void testFullConstructor() {
        Customer customer = new Customer("cust123", "Full Name", "0123456789",
                "full@email.com", true, 5, 10);

        assertEquals("cust123", customer.getCustomerId());
        assertEquals("Full Name", customer. getName());
        assertEquals("0123456789", customer.getPhone());
        assertEquals("full@email.com", customer.getEmail());
        assertTrue(customer.isRegistered());
        assertEquals(5, customer. getMonthlyOrderCount());
        assertEquals(10, customer. getTotalOrderCount());
    }
}