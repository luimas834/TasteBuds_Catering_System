package org.TBCS.service;

import org. TBCS.model.Chef;
import org.TBCS.model.Order;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Value object for displaying kitchen preparation details.
 * Follows SRP - only responsible for formatting preparation info.
 */
public class OrderPreparationDetails {
    private final Order order;
    private final List<Chef> assignedChefs;

    public OrderPreparationDetails(Order order, List<Chef> assignedChefs) {
        this.order = order;
        this.assignedChefs = assignedChefs;
    }

    public Order getOrder() {
        return order;
    }

    public List<Chef> getAssignedChefs() {
        return assignedChefs;
    }

    @Override
    public String toString() {
        String chefNames = assignedChefs.stream()
                .map(Chef::getName)
                .collect(Collectors.joining(", "));

        return String.format(
                "=== Kitchen Preparation Details ===\n" +
                        "Order #%d\n" +
                        "Priority: %s\n" +
                        "Assigned Chefs: %s\n" +
                        "Estimated Time:  %d minutes\n" +
                        "Status: %s",
                order.getQueueNumber(),
                order.getPriority(),
                chefNames. isEmpty() ? "None assigned" :  chefNames,
                order. getEstimatedMinutes(),
                order.getStatus()
        );
    }
}