package org.TBCS.model;

import java. time.LocalDateTime;
import java.util. UUID;

public class Feedback {
    private final String feedbackId;
    private final Order order;
    private final Customer customer;
    private final int rating; // 1-5
    private final String comments;
    private final LocalDateTime feedbackTime;

    public Feedback(Order order, Customer customer, int rating, String comments) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.feedbackId = UUID.randomUUID().toString();
        this.order = order;
        this.customer = customer;
        this.rating = rating;
        this.comments = comments;
        this. feedbackTime = LocalDateTime.now();
    }

    public String getFeedbackId() { return feedbackId; }
    public Order getOrder() { return order; }
    public Customer getCustomer() { return customer; }
    public int getRating() { return rating; }
    public String getComments() { return comments; }
    public LocalDateTime getFeedbackTime() { return feedbackTime; }

    @Override
    public String toString() {
        return String.format("Rating: %d/5\nComments: %s", rating, comments);
    }
}