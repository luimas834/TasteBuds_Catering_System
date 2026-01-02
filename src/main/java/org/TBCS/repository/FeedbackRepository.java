package org. TBCS.repository;

import org.TBCS.model. Feedback;

import java.util.*;
import java.util.stream. Collectors;

/**
 * Repository for managing Feedback entities.
 * Note: Currently feedback is stored in Order objects directly.
 * This repository is for potential future use with separate Feedback entities.
 */
public class FeedbackRepository {
    private final Map<String, Feedback> feedbacks = new HashMap<>();

    public void save(Feedback feedback) {
        feedbacks.put(feedback. getFeedbackId(), feedback);
    }

    public Feedback findById(String id) {
        return feedbacks. get(id);
    }

    public List<Feedback> findAll() {
        return new ArrayList<>(feedbacks.values());
    }

    public void delete(String id) {
        feedbacks. remove(id);
    }

    public List<Feedback> findByOrderId(String orderId) {
        return feedbacks.values().stream()
                .filter(f -> f.getOrder().getOrderId().equals(orderId))
                .collect(Collectors.toList());
    }
}