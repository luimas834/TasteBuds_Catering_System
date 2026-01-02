package org.TBCS.service;

import org.TBCS.model.Order;
import org.TBCS.model.OrderStatus;

import java.util. LinkedList;
import java.util.Queue;


public class QueueManager {
    private final Queue<Order> orderQueue;
    private int currentQueueNumber;
    private int currentServingNumber;

    public QueueManager() {
        this.orderQueue = new LinkedList<>();
        this.currentQueueNumber = 0;
        this. currentServingNumber = 0;
    }

    public int addToQueue(Order order) {
        currentQueueNumber++;
        order.setQueueNumber(currentQueueNumber);
        order.setStatus(OrderStatus.IN_QUEUE);
        orderQueue.add(order);
        return currentQueueNumber;
    }

    public Order getNextOrder() {
        Order order = orderQueue. poll();
        if (order != null) {
            currentServingNumber = order.getQueueNumber();
        }
        return order;
    }

    public int getCurrentServingNumber() {
        return currentServingNumber;
    }

    public int getQueueSize() {
        return orderQueue.size();
    }

    public boolean hasOrdersInQueue() {
        return ! orderQueue.isEmpty();
    }
}
