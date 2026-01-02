package org. TBCS.repository;

import org.TBCS.model.*;
import org.TBCS. util.XMLStorageUtil;
import org.w3c.dom.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * Repository for managing Order and Delivery entities with XML persistence.
 */
public class OrderRepository {
    private static final String ORDERS_FILENAME = "orders.xml";

    private final Map<String, Order> orders = new LinkedHashMap<>();
    private final Map<String, Delivery> deliveries = new HashMap<>();
    private int currentQueueNumber = 0;

    public OrderRepository() {
        loadOrdersFromXML();
    }

    // ==================== ORDER METHODS ====================

    public int getNextQueueNumber() {
        return ++currentQueueNumber;
    }

    public void save(Order order) {
        orders.put(order.getOrderId(), order);
        saveOrdersToXML();
    }

    public Order findById(String id) {
        return orders.get(id);
    }

    public Order findByQueueNumber(int queueNumber) {
        for (Order order : orders.values()) {
            if (order.getQueueNumber() == queueNumber) {
                return order;
            }
        }
        return null;
    }

    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    public List<Order> findByStatus(OrderStatus status) {
        List<Order> result = new ArrayList<>();
        for (Order order :  orders.values()) {
            if (order.getStatus() == status) {
                result.add(order);
            }
        }
        return result;
    }

    public int getCurrentServingNumber() {
        for (Order order : orders.values()) {
            if (order.getStatus() == OrderStatus.PREPARING) {
                return order.getQueueNumber();
            }
        }
        return 0;
    }

    // ==================== DELIVERY METHODS ====================

    public void saveDelivery(Delivery delivery) {
        deliveries.put(delivery.getDeliveryId(), delivery);
    }

    public Delivery findDeliveryByOrderId(String orderId) {
        for (Delivery delivery :  deliveries.values()) {
            if (delivery.getOrder().getOrderId().equals(orderId)) {
                return delivery;
            }
        }
        return null;
    }

    public Delivery findDeliveryByQueueNumber(int queueNumber) {
        for (Delivery delivery : deliveries.values()) {
            if (delivery.getOrder().getQueueNumber() == queueNumber) {
                return delivery;
            }
        }
        return null;
    }

    public List<Delivery> findAllDeliveries() {
        return new ArrayList<>(deliveries.values());
    }

    // ==================== XML PERSISTENCE ====================

    private void loadOrdersFromXML() {
        try {
            Document doc = XMLStorageUtil.loadDocument(ORDERS_FILENAME);
            if (doc == null) return;

            NodeList nodes = doc.getElementsByTagName("order");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element elem = (Element) nodes.item(i);
                Order order = parseOrderFromXML(elem);
                if (order != null) {
                    orders.put(order. getOrderId(), order);
                    if (order.getQueueNumber() >= currentQueueNumber) {
                        currentQueueNumber = order.getQueueNumber();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading orders: " + e.getMessage());
        }
    }

    private Order parseOrderFromXML(Element elem) {
        try {
            // Get customer info stored directly in order XML
            String customerId = XMLStorageUtil.getElementText(elem, "customerId");
            String customerName = XMLStorageUtil.getElementText(elem, "customerName");
            String customerPhone = XMLStorageUtil.getElementText(elem, "customerPhone");
            String customerEmail = XMLStorageUtil.getElementText(elem, "customerEmail");
            boolean customerRegistered = Boolean.parseBoolean(
                    XMLStorageUtil.getElementText(elem, "customerRegistered"));

            // Create customer object from stored data
            Customer customer = new Customer(customerId, customerName, customerPhone,
                    customerEmail, customerRegistered, 0, 0);

            int queueNumber = Integer.parseInt(XMLStorageUtil.getElementText(elem, "queueNumber"));
            Order order = new Order(queueNumber, customer);

            // Set order ID using reflection
            String orderId = XMLStorageUtil.getElementText(elem, "orderId");
            setFieldValue(order, "orderId", orderId);

            // Set status
            String statusStr = XMLStorageUtil.getElementText(elem, "status");
            if (! statusStr.isEmpty()) {
                order.setStatus(OrderStatus. valueOf(statusStr));
            }

            // Set priority
            String priorityStr = XMLStorageUtil.getElementText(elem, "priority");
            if (!priorityStr.isEmpty()) {
                order.setPriority(OrderPriority.valueOf(priorityStr));
            }

            // Set kitchen details
            String estMinutes = XMLStorageUtil. getElementText(elem, "estimatedMinutes");
            if (!estMinutes.isEmpty() && ! estMinutes.equals("0")) {
                order.setEstimatedMinutes(Integer.parseInt(estMinutes));
            }

            // Set rating and feedback
            String ratingStr = XMLStorageUtil. getElementText(elem, "rating");
            if (!ratingStr.isEmpty() && !ratingStr.equals("0")) {
                order.setRating(Integer.parseInt(ratingStr));
            }
            String feedback = XMLStorageUtil.getElementText(elem, "feedback");
            if (!feedback.isEmpty()) {
                order.setFeedback(feedback);
            }

            // Load order items
            NodeList itemNodes = elem.getElementsByTagName("item");
            for (int j = 0; j < itemNodes. getLength(); j++) {
                Element itemElem = (Element) itemNodes.item(j);
                String itemId = XMLStorageUtil.getElementText(itemElem, "itemId");
                String itemName = XMLStorageUtil.getElementText(itemElem, "itemName");
                int quantity = Integer.parseInt(XMLStorageUtil.getElementText(itemElem, "quantity"));
                BigDecimal price = new BigDecimal(XMLStorageUtil.getElementText(itemElem, "price"));

                MenuItem menuItem = new MenuItem(itemId, itemName, "", price);
                order.addItem(menuItem, quantity);
            }

            // Apply discount after items are added
            String discountStr = XMLStorageUtil.getElementText(elem, "discount");
            if (!discountStr.isEmpty() && !discountStr.equals("0")) {
                order.applyDiscount(new BigDecimal(discountStr));
            }

            return order;
        } catch (Exception e) {
            System.err.println("Error parsing order: " + e.getMessage());
            return null;
        }
    }

    private void setFieldValue(Object obj, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            // Ignore if reflection fails
        }
    }

    private void saveOrdersToXML() {
        try {
            Document doc = XMLStorageUtil.createDocument();
            Element root = doc.createElement("orders");
            doc.appendChild(root);

            for (Order order : orders.values()) {
                Element elem = doc.createElement("order");

                // Order details
                XMLStorageUtil.appendElement(doc, elem, "orderId", order.getOrderId());
                XMLStorageUtil.appendElement(doc, elem, "queueNumber", String.valueOf(order.getQueueNumber()));

                // Customer details (store directly to avoid dependency on CustomerRepository)
                Customer customer = order.getCustomer();
                XMLStorageUtil.appendElement(doc, elem, "customerId", customer.getCustomerId());
                XMLStorageUtil.appendElement(doc, elem, "customerName", customer.getName());
                XMLStorageUtil.appendElement(doc, elem, "customerPhone", customer.getPhone());
                XMLStorageUtil.appendElement(doc, elem, "customerEmail", customer.getEmail());
                XMLStorageUtil. appendElement(doc, elem, "customerRegistered", String.valueOf(customer.isRegistered()));

                // Order status and amounts
                XMLStorageUtil.appendElement(doc, elem, "status", order.getStatus().name());
                XMLStorageUtil.appendElement(doc, elem, "priority", order.getPriority().name());
                XMLStorageUtil. appendElement(doc, elem, "totalAmount", order.getTotalAmount().toString());
                XMLStorageUtil.appendElement(doc, elem, "discount", order.getDiscount().toString());
                XMLStorageUtil. appendElement(doc, elem, "finalAmount", order.getFinalAmount().toString());
                XMLStorageUtil.appendElement(doc, elem, "estimatedMinutes", String. valueOf(order.getEstimatedMinutes()));
                XMLStorageUtil.appendElement(doc, elem, "rating", String.valueOf(order.getRating()));
                XMLStorageUtil.appendElement(doc, elem, "feedback", order. getFeedback() != null ? order.getFeedback() : "");

                // Save order items
                Element itemsElem = doc.createElement("items");
                for (OrderItem item : order.getItems()) {
                    Element itemElem = doc.createElement("item");
                    XMLStorageUtil.appendElement(doc, itemElem, "itemId", item.getItemId());
                    XMLStorageUtil.appendElement(doc, itemElem, "itemName", item.getItemName());
                    XMLStorageUtil.appendElement(doc, itemElem, "quantity", String.valueOf(item.getQuantity()));
                    XMLStorageUtil.appendElement(doc, itemElem, "price", item.getUnitPrice().toString());
                    itemsElem.appendChild(itemElem);
                }
                elem.appendChild(itemsElem);

                root.appendChild(elem);
            }

            XMLStorageUtil.saveDocument(doc, ORDERS_FILENAME);
        } catch (Exception e) {
            System.err.println("Error saving orders: " + e.getMessage());
        }
    }
}