package org.TBCS.service;

import org.TBCS.model.*;
import org.TBCS. repository.OrderRepository;

import java.util.List;

/**
 * Service class responsible for handling customer feedback operations.
 * Follows SRP - only handles feedback-related business logic.
 */
public class FeedbackService {
    private final OrderRepository orderRepository;

    public FeedbackService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Submit feedback for a delivered order.
     * @param order The order to give feedback for
     * @param rating Rating from 1-5
     * @param feedback Optional comment/suggestion
     * @throws IllegalStateException if order is not delivered
     * @throws IllegalArgumentException if rating is invalid
     */
    public void submitFeedback(Order order, int rating, String feedback) {
        if (order. getStatus() != OrderStatus.DELIVERED) {
            throw new IllegalStateException("Can only give feedback for delivered orders");
        }
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        order.setRating(rating);
        order.setFeedback(feedback);
        orderRepository.save(order);
    }

    /**
     * Calculate the average rating across all rated orders.
     * @return Average rating or 0 if no ratings exist
     */
    public double getAverageRating() {
        List<Order> orders = orderRepository. findAll();
        int count = 0;
        int total = 0;
        for (Order order : orders) {
            if (order.getRating() > 0) {
                total += order.getRating();
                count++;
            }
        }
        return count > 0 ? (double) total / count : 0.0;
    }

    /**
     * Check if an order has feedback.
     * @param order The order to check
     * @return true if order has a rating
     */
    public boolean hasFeedback(Order order) {
        return order.getRating() > 0;
    }
}