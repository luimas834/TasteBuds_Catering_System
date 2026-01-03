package org. TBCS.service;

import org. TBCS.model.*;
import org. TBCS.repository. OrderRepository;
import org.junit.jupiter. api.BeforeEach;
import org.junit.jupiter. api.DisplayName;
import org.junit.jupiter.api. Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for FeedbackService.
 * Tests the "Customer Feedback" scenario.
 */
public class FeedbackServiceTest {

    private FeedbackService feedbackService;
    private OrderRepository orderRepository;
    private Customer customer;
    private Order deliveredOrder;
    private Order preparingOrder;

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepository();
        feedbackService = new FeedbackService(orderRepository);

        customer = new Customer("Test Customer", "01712345678", "test@email.com", true);

        deliveredOrder = new Order(1, customer);
        deliveredOrder.setStatus(OrderStatus. DELIVERED);

        preparingOrder = new Order(2, customer);
        preparingOrder.setStatus(OrderStatus. PREPARING);
    }

    @Test
    @DisplayName("Scenario:  Customer Feedback - Submit rating for delivered order")
    void testSubmitRating() {
        feedbackService.submitFeedback(deliveredOrder, 5, "Excellent food and service!");

        assertEquals(5, deliveredOrder. getRating());
        assertEquals("Excellent food and service!", deliveredOrder. getFeedback());
    }

    @Test
    @DisplayName("Scenario: Customer Feedback - Rating must be between 1 and 5")
    void testRatingBoundaries() {
        // Valid ratings
        assertDoesNotThrow(() -> feedbackService. submitFeedback(deliveredOrder, 1, "Poor"));

        deliveredOrder = new Order(3, customer);
        deliveredOrder.setStatus(OrderStatus. DELIVERED);
        assertDoesNotThrow(() -> feedbackService.submitFeedback(deliveredOrder, 5, "Excellent"));

        // Invalid rating - too low
        Order order1 = new Order(4, customer);
        order1.setStatus(OrderStatus. DELIVERED);
        assertThrows(IllegalArgumentException.class,
                () -> feedbackService. submitFeedback(order1, 0, "Invalid"));

        // Invalid rating - too high
        Order order2 = new Order(5, customer);
        order2.setStatus(OrderStatus.DELIVERED);
        assertThrows(IllegalArgumentException.class,
                () -> feedbackService. submitFeedback(order2, 6, "Invalid"));
    }

    @Test
    @DisplayName("Scenario: Customer Feedback - Cannot give feedback for non-delivered order")
    void testCannotFeedbackNonDeliveredOrder() {
        assertThrows(IllegalStateException.class,
                () -> feedbackService.submitFeedback(preparingOrder, 4, "Good food"));
    }

    @Test
    @DisplayName("Scenario: Customer Feedback - Check if order has feedback")
    void testHasFeedback() {
        assertFalse(feedbackService.hasFeedback(deliveredOrder));

        feedbackService. submitFeedback(deliveredOrder, 4, "Good!");

        assertTrue(feedbackService.hasFeedback(deliveredOrder));
    }

    @Test
    @DisplayName("Scenario: Customer Feedback - Submit feedback without comment")
    void testSubmitFeedbackWithoutComment() {
        feedbackService.submitFeedback(deliveredOrder, 3, "");

        assertEquals(3, deliveredOrder.getRating());
        assertEquals("", deliveredOrder.getFeedback());
    }

    @Test
    @DisplayName("Scenario: Customer Feedback - Submit feedback with null comment")
    void testSubmitFeedbackWithNullComment() {
        feedbackService.submitFeedback(deliveredOrder, 4, null);

        assertEquals(4, deliveredOrder. getRating());
        assertNull(deliveredOrder.getFeedback());
    }

    @Test
    @DisplayName("Scenario: Customer Feedback - Mark order delivered before feedback")
    void testMarkDeliveredBeforeFeedback() {
        Order newOrder = new Order(6, customer);
        newOrder.setStatus(OrderStatus. DELIVERED);

        // Should work after marking delivered
        assertDoesNotThrow(() -> feedbackService.submitFeedback(newOrder, 5, "Great! "));
    }
}