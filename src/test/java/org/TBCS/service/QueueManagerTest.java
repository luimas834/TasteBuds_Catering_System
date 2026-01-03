package org.TBCS.service;

import org.TBCS.model. Customer;
import org. TBCS.model.Order;
import org.TBCS.model.OrderStatus;
import org. junit.jupiter.api. BeforeEach;
import org.junit.jupiter.api. DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for QueueManager.
 * Tests order queue management functionality.
 */
public class QueueManagerTest {

    private QueueManager queueManager;
    private Customer customer;

    @BeforeEach
    void setUp() {
        queueManager = new QueueManager();
        customer = new Customer("Test Customer", "01712345678", "test@email.com", true);
    }

    @Test
    @DisplayName("Should add order to queue and return queue number")
    void testAddToQueue() {
        Order order = new Order(0, customer);

        int queueNumber = queueManager. addToQueue(order);

        assertEquals(1, queueNumber);
        assertEquals(1, order.getQueueNumber());
        assertEquals(OrderStatus.IN_QUEUE, order.getStatus());
    }

    @Test
    @DisplayName("Should assign sequential queue numbers")
    void testSequentialQueueNumbers() {
        Order order1 = new Order(0, customer);
        Order order2 = new Order(0, customer);
        Order order3 = new Order(0, customer);

        int num1 = queueManager.addToQueue(order1);
        int num2 = queueManager. addToQueue(order2);
        int num3 = queueManager.addToQueue(order3);

        assertEquals(1, num1);
        assertEquals(2, num2);
        assertEquals(3, num3);
    }

    @Test
    @DisplayName("Should get next order from queue (FIFO)")
    void testGetNextOrderFIFO() {
        Order order1 = new Order(0, customer);
        Order order2 = new Order(0, customer);

        queueManager. addToQueue(order1);
        queueManager.addToQueue(order2);

        Order nextOrder = queueManager.getNextOrder();

        assertEquals(order1, nextOrder);
        assertEquals(1, queueManager.getCurrentServingNumber());
    }

    @Test
    @DisplayName("Should update current serving number when getting next order")
    void testCurrentServingNumberUpdate() {
        Order order1 = new Order(0, customer);
        Order order2 = new Order(0, customer);

        queueManager.addToQueue(order1);
        queueManager. addToQueue(order2);

        assertEquals(0, queueManager. getCurrentServingNumber());

        queueManager.getNextOrder();
        assertEquals(1, queueManager.getCurrentServingNumber());

        queueManager.getNextOrder();
        assertEquals(2, queueManager. getCurrentServingNumber());
    }

    @Test
    @DisplayName("Should return correct queue size")
    void testQueueSize() {
        assertEquals(0, queueManager.getQueueSize());

        Order order1 = new Order(0, customer);
        Order order2 = new Order(0, customer);

        queueManager. addToQueue(order1);
        assertEquals(1, queueManager.getQueueSize());

        queueManager. addToQueue(order2);
        assertEquals(2, queueManager.getQueueSize());

        queueManager.getNextOrder();
        assertEquals(1, queueManager.getQueueSize());
    }

    @Test
    @DisplayName("Should check if queue has orders")
    void testHasOrdersInQueue() {
        assertFalse(queueManager.hasOrdersInQueue());

        Order order = new Order(0, customer);
        queueManager.addToQueue(order);

        assertTrue(queueManager.hasOrdersInQueue());

        queueManager.getNextOrder();
        assertFalse(queueManager.hasOrdersInQueue());
    }

    @Test
    @DisplayName("Should return null when queue is empty")
    void testGetNextOrderEmptyQueue() {
        Order order = queueManager.getNextOrder();
        assertNull(order);
    }
}