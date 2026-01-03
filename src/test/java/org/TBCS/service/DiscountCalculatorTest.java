package org.TBCS.service;

import org.TBCS.model. Customer;
import org. junit.jupiter.api. BeforeEach;
import org.junit.jupiter.api. DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api. Assertions.*;

/**
 * Unit tests for DiscountCalculator.
 * Tests discount calculation logic following SRP.
 */
public class DiscountCalculatorTest {

    private DiscountCalculator discountCalculator;

    @BeforeEach
    void setUp() {
        discountCalculator = new DiscountCalculator();
    }

    @Test
    @DisplayName("Should return zero discount for unregistered customer")
    void testNoDiscountForUnregistered() {
        Customer unregisteredCustomer = new Customer("Guest", "0123456789", "guest@email.com", false);
        BigDecimal totalAmount = new BigDecimal("1000");

        BigDecimal discount = discountCalculator.calculateDiscount(unregisteredCustomer, totalAmount);

        assertEquals(BigDecimal.ZERO, discount);
    }

    @Test
    @DisplayName("Should calculate 2% discount per monthly order")
    void testDiscountPerOrder() {
        // Customer with 3 monthly orders = 6% discount
        Customer customer = new Customer("custId", "Regular", "01712345678",
                "reg@email.com", true, 3, 5);
        BigDecimal totalAmount = new BigDecimal("1000");

        BigDecimal discount = discountCalculator.calculateDiscount(customer, totalAmount);

        // 1000 * 0.06 = 60
        assertEquals(new BigDecimal("60.00"), discount.setScale(2));
    }

    @Test
    @DisplayName("Should cap discount at 15%")
    void testMaxDiscountCap() {
        // Customer with 10 monthly orders = would be 20%, but capped at 15%
        Customer loyalCustomer = new Customer("custId", "Loyal", "01798765432",
                "loyal@email.com", true, 10, 50);
        BigDecimal totalAmount = new BigDecimal("1000");

        BigDecimal discount = discountCalculator.calculateDiscount(loyalCustomer, totalAmount);

        // 1000 * 0.15 = 150 (not 200)
        assertEquals(new BigDecimal("150.00"), discount.setScale(2));
    }

    @Test
    @DisplayName("Should handle zero monthly orders for registered customer")
    void testZeroOrdersRegisteredCustomer() {
        Customer newCustomer = new Customer("custId", "New Registered", "01712345678",
                "new@email. com", true, 0, 0);
        BigDecimal totalAmount = new BigDecimal("500");

        BigDecimal discount = discountCalculator. calculateDiscount(newCustomer, totalAmount);

        assertEquals(new BigDecimal("0.00"), discount.setScale(2));
    }

    @Test
    @DisplayName("Should calculate discount for exactly 7 orders (14%)")
    void testExactDiscountCalculation() {
        Customer customer = new Customer("custId", "Regular", "01712345678",
                "reg@email. com", true, 7, 10);
        BigDecimal totalAmount = new BigDecimal("500");

        BigDecimal discount = discountCalculator. calculateDiscount(customer, totalAmount);

        // 500 * 0.14 = 70
        assertEquals(new BigDecimal("70.00"), discount.setScale(2));
    }
}