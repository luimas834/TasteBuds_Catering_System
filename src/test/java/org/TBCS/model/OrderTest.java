package org. TBCS.model;

import org.junit.jupiter.api. BeforeEach;
import org.junit.jupiter.api. DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api. Assertions.*;

/**
 * Unit tests for Order model class.
 * Tests order creation, item management, and discount application.
 */
public class OrderTest {

    private Customer customer;
    private Order order;
    private MenuItem chickenBiryani;
    private MenuItem naan;

    @BeforeEach
    void setUp() {
        customer = new Customer("Test Customer", "01712345678", "test@email.com", true);
        order = new Order(1, customer);
        chickenBiryani = new MenuItem("M001", "Chicken Biryani", "Main Course", new BigDecimal("350"));
        naan = new MenuItem("M005", "Naan", "Bread", new BigDecimal("30"));
    }

    @Test
    @DisplayName("Should create order with correct initial values")
    void testOrderCreation() {
        assertNotNull(order. getOrderId());
        assertTrue(order.getOrderId().startsWith("ORD-"));
        assertEquals(1, order.getQueueNumber());
        assertEquals(customer, order.getCustomer());
        assertEquals(OrderStatus. PLACED, order.getStatus());
        assertEquals(OrderPriority. NORMAL, order. getPriority());
        assertEquals(BigDecimal.ZERO, order.getTotalAmount());
        assertEquals(0, order.getRating());
    }

    @Test
    @DisplayName("Should add items and calculate total correctly")
    void testAddItems() {
        order.addItem(chickenBiryani, 2); // 350 * 2 = 700
        order.addItem(naan, 3); // 30 * 3 = 90

        assertEquals(2, order.getItems().size());
        assertEquals(new BigDecimal("790"), order.getTotalAmount());
    }

    @Test
    @DisplayName("Should apply discount correctly")
    void testApplyDiscount() {
        order. addItem(chickenBiryani, 2); // 700
        BigDecimal discount = new BigDecimal("70"); // 10% discount

        order.applyDiscount(discount);

        assertEquals(new BigDecimal("700"), order.getTotalAmount());
        assertEquals(new BigDecimal("70"), order.getDiscount());
        assertEquals(new BigDecimal("630"), order.getFinalAmount());
    }

    @Test
    @DisplayName("Should add chef to order")
    void testAddChef() {
        assertTrue(order.getAssignedChefIds().isEmpty());

        order.addChef("chef001");
        order.addChef("chef002");

        assertEquals(2, order.getAssignedChefIds().size());
        assertTrue(order.getAssignedChefIds().contains("chef001"));
        assertTrue(order.getAssignedChefIds().contains("chef002"));
    }

    @Test
    @DisplayName("Should not add duplicate chef")
    void testNoDuplicateChef() {
        order.addChef("chef001");
        order.addChef("chef001"); // Try adding same chef

        assertEquals(1, order.getAssignedChefIds().size());
    }

    @Test
    @DisplayName("Should update order status correctly")
    void testOrderStatusUpdate() {
        order.setStatus(OrderStatus. IN_QUEUE);
        assertEquals(OrderStatus.IN_QUEUE, order. getStatus());

        order.setStatus(OrderStatus. PREPARING);
        assertEquals(OrderStatus.PREPARING, order.getStatus());

        order.setStatus(OrderStatus. READY);
        assertEquals(OrderStatus. READY, order. getStatus());

        order.setStatus(OrderStatus. DELIVERED);
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
    }

    @Test
    @DisplayName("Should set priority correctly")
    void testOrderPriority() {
        assertEquals(OrderPriority. NORMAL, order.getPriority());

        order.setPriority(OrderPriority. PRIORITY);
        assertEquals(OrderPriority. PRIORITY, order.getPriority());
    }

    @Test
    @DisplayName("Should set rating and feedback")
    void testRatingAndFeedback() {
        order.setRating(5);
        order.setFeedback("Excellent food!");

        assertEquals(5, order.getRating());
        assertEquals("Excellent food!", order.getFeedback());
    }
}