package org.TBCS.service;

import org. TBCS.model.*;
import org. TBCS.repository. CustomerRepository;
import org.TBCS. repository.OrderRepository;
import org.junit.jupiter.api. BeforeEach;
import org.junit.jupiter.api. DisplayName;
import org.junit.jupiter.api.Test;

import java.math. BigDecimal;

import static org.junit.jupiter.api. Assertions.*;

public class OrderServiceTest {

    private OrderService orderService;
    private OrderRepository orderRepository;
    private CustomerRepository customerRepository;
    private Customer registeredCustomer;
    private Customer unregisteredCustomer;
    private MenuItem chickenBiryani;
    private MenuItem naan;

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepository();
        customerRepository = new CustomerRepository();
        orderService = new OrderService(orderRepository, customerRepository);

        registeredCustomer = new Customer("Registered User", "01711111111", "reg@email.com", true);
        unregisteredCustomer = new Customer("Guest User", "01722222222", "guest@email.com", false);

        chickenBiryani = new MenuItem("M001", "Chicken Biryani", "Main Course", new BigDecimal("350"));
        naan = new MenuItem("M005", "Naan", "Bread", new BigDecimal("30"));
    }

    @Test
    @DisplayName("Scenario: Placing an Order - Create new order")
    void testCreateOrder() {
        Order order = orderService. createOrder(registeredCustomer);

        assertNotNull(order);
        assertNotNull(order. getOrderId());
        assertTrue(order.getQueueNumber() > 0);
        assertEquals(registeredCustomer, order.getCustomer());
        assertEquals(OrderStatus. IN_QUEUE, order.getStatus());
    }

    @Test
    @DisplayName("Scenario: Placing an Order - Add items to order")
    void testAddItemsToOrder() {
        Order order = orderService.createOrder(registeredCustomer);

        orderService.addItemToOrder(order, chickenBiryani, 2);
        orderService.addItemToOrder(order, naan, 4);

        assertEquals(2, order.getItems().size());
        // 350 * 2 + 30 * 4 = 820
        assertEquals(new BigDecimal("820"), order.getTotalAmount());
    }

    @Test
    @DisplayName("Scenario:  Placing an Order - Queue number assigned")
    void testQueueNumberAssignment() {
        Order order1 = orderService. createOrder(registeredCustomer);
        Order order2 = orderService.createOrder(unregisteredCustomer);

        assertTrue(order2.getQueueNumber() > order1.getQueueNumber());
    }

    @Test
    @DisplayName("Scenario: Placing an Order - Registered customer gets discount")
    void testRegisteredCustomerDiscount() {
        // Simulate customer with previous orders
        registeredCustomer = new Customer("custId", "Loyal Customer", "01733333333",
                "loyal@email.com", true, 3, 5); // 3 monthly orders

        Order order = orderService.createOrder(registeredCustomer);
        orderService.addItemToOrder(order, chickenBiryani, 2); // 700

        // Calculate expected discount rate:  2% * 3 = 6%
        BigDecimal expectedDiscountRate = new BigDecimal("0.06");
        BigDecimal calculatedRate = orderService.calculateDiscountRate(registeredCustomer);

        assertEquals(0, expectedDiscountRate.compareTo(calculatedRate));
    }

    @Test
    @DisplayName("Scenario:  Placing an Order - Unregistered customer no discount")
    void testUnregisteredCustomerNoDiscount() {
        BigDecimal discountRate = orderService.calculateDiscountRate(unregisteredCustomer);
        assertEquals(BigDecimal.ZERO, discountRate);
    }

    @Test
    @DisplayName("Scenario: Placing an Order - Max discount cap at 15%")
    void testMaxDiscountCap() {
        // Customer with many orders (10 orders would give 20%, but max is 15%)
        Customer loyalCustomer = new Customer("custId", "Super Loyal", "01744444444",
                "super@email.com", true, 10, 50);

        BigDecimal discountRate = orderService.calculateDiscountRate(loyalCustomer);

        // Should be capped at 15%
        assertEquals(0, new BigDecimal("0.15").compareTo(discountRate));
    }

    @Test
    @DisplayName("Scenario: Placing an Order - Order finalization increments customer count")
    void testFinalizeOrderIncrementsCount() {
        int initialCount = registeredCustomer.getMonthlyOrderCount();

        Order order = orderService. createOrder(registeredCustomer);
        orderService.addItemToOrder(order, chickenBiryani, 1);
        orderService.finalizeOrder(order);

        assertEquals(initialCount + 1, registeredCustomer.getMonthlyOrderCount());
    }
}