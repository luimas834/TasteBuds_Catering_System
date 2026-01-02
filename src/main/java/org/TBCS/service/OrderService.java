package org. TBCS.service;

import org.TBCS.model.*;
import org.TBCS. repository.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util. List;

public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    // Discount:  2% per order, max 15%
    private static final BigDecimal DISCOUNT_PER_ORDER = new BigDecimal("0.02");
    private static final BigDecimal MAX_DISCOUNT = new BigDecimal("0.15");

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    public Order createOrder(Customer customer) {
        int queueNumber = orderRepository.getNextQueueNumber();
        Order order = new Order(queueNumber, customer);
        order.setStatus(OrderStatus.IN_QUEUE);
        return order;
    }

    public void addItemToOrder(Order order, MenuItem item, int quantity) {
        order.addItem(item, quantity);
    }

    public void finalizeOrder(Order order) {
        Customer customer = order.getCustomer();

        // Calculate discount for registered customers based on monthly orders
        if (customer.isRegistered() && customer.getMonthlyOrderCount() > 0) {
            BigDecimal discountRate = DISCOUNT_PER_ORDER. multiply(
                    BigDecimal.valueOf(customer.getMonthlyOrderCount()));

            if (discountRate.compareTo(MAX_DISCOUNT) > 0) {
                discountRate = MAX_DISCOUNT;
            }

            BigDecimal discountAmount = order.getTotalAmount()
                    .multiply(discountRate)
                    .setScale(2, RoundingMode. HALF_UP);
            order.applyDiscount(discountAmount);
        }

        // Increment customer order count and save
        customer.incrementOrderCount();
        customerRepository.save(customer);

        orderRepository.save(order);
    }

    public BigDecimal calculateDiscountRate(Customer customer) {
        if (!customer.isRegistered() || customer.getMonthlyOrderCount() == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal rate = DISCOUNT_PER_ORDER.multiply(BigDecimal.valueOf(customer.getMonthlyOrderCount()));
        return rate.compareTo(MAX_DISCOUNT) > 0 ? MAX_DISCOUNT : rate;
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderByQueueNumber(int queueNumber) {
        return orderRepository. findByQueueNumber(queueNumber);
    }

    public int getCurrentServingNumber() {
        return orderRepository.getCurrentServingNumber();
    }
}