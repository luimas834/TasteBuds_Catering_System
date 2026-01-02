package org. TBCS.ui;

import org.TBCS.model.*;
import org.TBCS.repository.*;
import org. TBCS.service.*;
import org.TBCS.util.ConsoleHelper;

import java.math.BigDecimal;
import java.util.List;

public class CustomerMenu {
    private final CustomerRepository customerRepository;
    private final MenuRepository menuRepository;
    private final OrderService orderService;
    private final DeliveryService deliveryService;
    private final FeedbackService feedbackService;

    private Customer currentCustomer;

    public CustomerMenu(CustomerRepository customerRepository, MenuRepository menuRepository,
                        OrderService orderService, DeliveryService deliveryService,
                        FeedbackService feedbackService) {
        this.customerRepository = customerRepository;
        this.menuRepository = menuRepository;
        this.orderService = orderService;
        this.deliveryService = deliveryService;
        this.feedbackService = feedbackService;
    }

    public void show() {
        while (true) {
            ConsoleHelper.printSubHeader("CUSTOMER PORTAL");

            if (currentCustomer != null) {
                System.out.println("  Logged in as: " + currentCustomer.getName());
                System.out.println("  Phone: " + currentCustomer.getPhone());
                System.out. println("  Status: " + (currentCustomer.isRegistered() ? "Registered Member" : "Guest"));
                if (currentCustomer.isRegistered()) {
                    System.out.println("  Monthly Orders: " + currentCustomer.getMonthlyOrderCount());
                    BigDecimal discountRate = orderService.calculateDiscountRate(currentCustomer);
                    if (discountRate.compareTo(BigDecimal.ZERO) > 0) {
                        System.out.println("  Your Discount:  " + discountRate.multiply(new BigDecimal("100")).intValue() + "%");
                    }
                }
                ConsoleHelper.printDivider();
            }

            System.out.println("  1. Login / Register");
            System.out. println("  2. View Menu");
            System.out.println("  3. Place Order");
            System.out. println("  4. Track My Order");
            System.out. println("  5. Mark Order as Delivered");
            System.out. println("  6. Give Feedback & Rating");
            System.out.println("  0. Back to Main Menu");
            ConsoleHelper.printDivider();

            int choice = ConsoleHelper.readIntInRange("Enter your choice: ", 0, 6);

            switch (choice) {
                case 1 -> loginOrRegister();
                case 2 -> viewMenu();
                case 3 -> placeOrder();
                case 4 -> trackOrder();
                case 5 -> markDelivered();
                case 6 -> giveFeedback();
                case 0 -> {
                    currentCustomer = null;
                    return;
                }
            }
        }
    }

    private void loginOrRegister() {
        ConsoleHelper.printSubHeader("LOGIN / REGISTER");

        String phone = ConsoleHelper.readString("Enter your phone number: ");
        currentCustomer = customerRepository.findByPhone(phone);

        if (currentCustomer == null) {
            System.out.println("\nNew customer!  Please provide your details:");
            String name = ConsoleHelper.readString("Enter your name: ");
            String email = ConsoleHelper.readString("Enter your email: ");
            boolean register = ConsoleHelper.readYesNo("Would you like to register for discounts? ");

            currentCustomer = new Customer(name, phone, email, register);
            customerRepository.save(currentCustomer);

            if (register) {
                ConsoleHelper.printSuccess("Registration successful! You'll earn discounts on future orders.");
            } else {
                ConsoleHelper.printSuccess("Welcome!  You can register anytime to earn discounts.");
            }
        } else {
            ConsoleHelper.printSuccess("Welcome back, " + currentCustomer.getName() + "!");

            if (! currentCustomer.isRegistered()) {
                if (ConsoleHelper.readYesNo("Would you like to register now for discounts?")) {
                    currentCustomer.setRegistered(true);
                    customerRepository.save(currentCustomer);
                    ConsoleHelper. printSuccess("You are now registered!  Discounts will apply from next order.");
                }
            }
        }

        ConsoleHelper.waitForEnter();
    }

    private void viewMenu() {
        ConsoleHelper.printSubHeader("OUR MENU");

        List<String> categories = menuRepository.getCategories();
        for (String category : categories) {
            System.out.println("\n=== " + category. toUpperCase() + " ===");
            List<MenuItem> items = menuRepository.findByCategory(category);
            for (MenuItem item : items) {
                if (item.isAvailable()) {
                    System.out.printf("  [%s] %-25s Tk %s%n",
                            item.getItemId(), item.getName(), item.getPrice());
                }
            }
        }

        ConsoleHelper.waitForEnter();
    }

    private void placeOrder() {
        if (currentCustomer == null) {
            ConsoleHelper.printError("Please login first!");
            ConsoleHelper.waitForEnter();
            return;
        }

        ConsoleHelper.printSubHeader("PLACE ORDER");

        // Show available menu items
        List<MenuItem> availableItems = menuRepository.findAvailable();
        if (availableItems.isEmpty()) {
            ConsoleHelper.printError("No items available right now.");
            ConsoleHelper. waitForEnter();
            return;
        }

        System. out.println("Available Items:");
        for (int i = 0; i < availableItems.size(); i++) {
            MenuItem item = availableItems.get(i);
            System.out. printf("  %2d. [%s] %-25s Tk %s%n",
                    i + 1, item.getItemId(), item.getName(), item.getPrice());
        }
        ConsoleHelper.printDivider();

        // Create order and add items
        Order order = orderService.createOrder(currentCustomer);

        boolean addingItems = true;
        while (addingItems) {
            int itemChoice = ConsoleHelper.readIntInRange(
                    "Select item number (0 to finish): ", 0, availableItems.size());

            if (itemChoice == 0) {
                addingItems = false;
            } else {
                MenuItem selectedItem = availableItems.get(itemChoice - 1);
                int quantity = ConsoleHelper.readIntInRange("Quantity: ", 1, 20);

                orderService.addItemToOrder(order, selectedItem, quantity);

                ConsoleHelper.printSuccess("Added " + quantity + "x " + selectedItem.getName());

                if (! ConsoleHelper.readYesNo("Add more items?")) {
                    addingItems = false;
                }
            }
        }

        if (order.getItems().isEmpty()) {
            ConsoleHelper.printError("No items added.  Order cancelled.");
            ConsoleHelper.waitForEnter();
            return;
        }

        // Show order summary before finalizing
        ConsoleHelper.printSubHeader("ORDER SUMMARY");
        System.out.println("Customer: " + currentCustomer.getName());
        System.out.println("\nItems:");
        for (OrderItem item : order.getItems()) {
            System.out.println("  - " + item);
        }
        ConsoleHelper.printDivider();
        System.out.println("Subtotal: Tk " + order.getTotalAmount());

        // Calculate and show potential discount
        if (currentCustomer.isRegistered() && currentCustomer.getMonthlyOrderCount() > 0) {
            BigDecimal discountRate = orderService. calculateDiscountRate(currentCustomer);
            System.out.println("Discount (" + discountRate.multiply(new BigDecimal("100")).intValue() +
                    "%): -Tk " + order.getTotalAmount().multiply(discountRate).setScale(2));
        }

        if (! ConsoleHelper.readYesNo("Confirm order?")) {
            ConsoleHelper.printInfo("Order cancelled.");
            ConsoleHelper.waitForEnter();
            return;
        }

        // Finalize order (applies discount)
        orderService.finalizeOrder(order);

        // Display final receipt
        ConsoleHelper.printSubHeader("ORDER CONFIRMED");
        System.out.println("+------------------------------------------+");
        System.out. println("|              ORDER RECEIPT               |");
        System.out. println("+------------------------------------------+");
        System.out.printf("| Queue Number: #%-25d |%n", order.getQueueNumber());
        System.out.printf("| Order ID: %-30s |%n", order.getOrderId());
        System.out.printf("| Customer: %-30s |%n", currentCustomer.getName());
        System.out.println("+------------------------------------------+");
        System.out.println("| Items:                                    |");
        for (OrderItem item : order.getItems()) {
            System.out.printf("|   %-38s |%n", item.getItemName() + " x" + item.getQuantity());
            System.out.printf("|   %38s |%n", "Tk " + item.getTotalPrice());
        }
        System. out.println("+------------------------------------------+");
        System.out.printf("| Subtotal: %30s |%n", "Tk " + order.getTotalAmount());
        if (order.getDiscount().compareTo(BigDecimal. ZERO) > 0) {
            System.out.printf("| Discount: %30s |%n", "-Tk " + order.getDiscount());
        }
        System.out.printf("| TOTAL: %33s |%n", "Tk " + order.getFinalAmount());
        System.out.println("+------------------------------------------+");
        System.out.println("| Status: " + order.getStatus().getDisplayName());
        System.out. println("+------------------------------------------+");

        ConsoleHelper.printSuccess("Order placed successfully!");
        System.out.println("Your Queue Number is: #" + order.getQueueNumber());
        System.out.println("Please wait for your order to be prepared.");

        ConsoleHelper.waitForEnter();
    }

    private void trackOrder() {
        ConsoleHelper.printSubHeader("TRACK ORDER");

        int queueNumber = ConsoleHelper.readInt("Enter your queue number: ");
        Order order = orderService.getOrderByQueueNumber(queueNumber);

        if (order == null) {
            ConsoleHelper.printError("Order not found!");
            ConsoleHelper. waitForEnter();
            return;
        }

        System.out.println("\n+------------------------------------------+");
        System.out.println("|             ORDER STATUS                 |");
        System.out. println("+------------------------------------------+");
        System.out.printf("| Queue #:  %-31d |%n", order.getQueueNumber());
        System.out.printf("| Status: %-32s |%n", order.getStatus().getDisplayName());
        System.out.printf("| Priority: %-30s |%n", order.getPriority().getDisplayName());
        System.out.printf("| Items:  %-33d |%n", order.getItems().size());
        System.out.printf("| Total: %-33s |%n", "Tk " + order.getFinalAmount());
        System.out.println("+------------------------------------------+");

        // Show progress
        System.out.println("\n  Order Progress:");
        System.out.println("  [" + (order.getStatus().ordinal() >= 0 ? "X" : " ") + "] Order Placed");
        System.out.println("  [" + (order.getStatus().ordinal() >= 1 ? "X" : " ") + "] In Queue");
        System.out.println("  [" + (order. getStatus().ordinal() >= 2 ? "X" : " ") + "] Being Prepared");
        System.out.println("  [" + (order.getStatus().ordinal() >= 3 ? "X" : " ") + "] Ready for Delivery");
        System.out. println("  [" + (order.getStatus().ordinal() >= 4 ? "X" :  " ") + "] Out for Delivery");
        System.out.println("  [" + (order.getStatus().ordinal() >= 5 ? "X" : " ") + "] Delivered");

        if (order.getEstimatedMinutes() > 0 && order.getStatus() == OrderStatus.PREPARING) {
            System.out. println("\n  Estimated time:  " + order.getEstimatedMinutes() + " minutes");
        }

        if (order.getAssignedDriverId() != null) {
            Delivery delivery = deliveryService.getDeliveryByQueueNumber(queueNumber);
            if (delivery != null && delivery.getDriver() != null) {
                System.out.println("\n  Driver: " + delivery.getDriver().getName());
                System.out. println("  Vehicle: " + delivery.getVehicle().getRegistrationNumber());
            }
        }

        ConsoleHelper.waitForEnter();
    }

    private void markDelivered() {
        ConsoleHelper.printSubHeader("MARK ORDER AS DELIVERED");

        int queueNumber = ConsoleHelper.readInt("Enter your queue number: ");
        Delivery delivery = deliveryService.getDeliveryByQueueNumber(queueNumber);

        if (delivery == null) {
            ConsoleHelper.printError("Delivery not found for this order!");
            ConsoleHelper.waitForEnter();
            return;
        }

        Order order = delivery.getOrder();

        if (order. getStatus() == OrderStatus.DELIVERED) {
            ConsoleHelper. printInfo("This order has already been marked as delivered.");
            ConsoleHelper.waitForEnter();
            return;
        }

        if (order.getStatus() != OrderStatus.OUT_FOR_DELIVERY) {
            ConsoleHelper.printError("Order is not out for delivery yet.");
            System.out.println("Current status: " + order.getStatus().getDisplayName());
            ConsoleHelper.waitForEnter();
            return;
        }

        if (ConsoleHelper.readYesNo("Confirm that you have received your order?")) {
            deliveryService.markDelivered(delivery);
            ConsoleHelper.printSuccess("Order marked as delivered!  Thank you for ordering from TasteBuds!");
            System.out.println("\nWe hope you enjoy your meal!");
            System.out.println("Please consider giving us feedback and rating.");
        }

        ConsoleHelper.waitForEnter();
    }

    private void giveFeedback() {
        ConsoleHelper.printSubHeader("FEEDBACK & RATING");

        int queueNumber = ConsoleHelper.readInt("Enter your order queue number: ");
        Order order = orderService.getOrderByQueueNumber(queueNumber);

        if (order == null) {
            ConsoleHelper.printError("Order not found!");
            ConsoleHelper.waitForEnter();
            return;
        }

        if (order.getStatus() != OrderStatus.DELIVERED) {
            ConsoleHelper.printError("You can only give feedback for delivered orders.");
            System.out.println("Current status: " + order.getStatus().getDisplayName());
            ConsoleHelper.waitForEnter();
            return;
        }

        if (order.getRating() > 0) {
            ConsoleHelper.printInfo("You've already submitted feedback for this order.");
            System.out.println("\nYour previous feedback:");
            System.out.println("Rating: " + getStarRating(order.getRating()));
            System.out. println("Comment: " + (order.getFeedback() != null ? order.getFeedback() : "No comment"));
            ConsoleHelper.waitForEnter();
            return;
        }

        System.out.println("\nHow was your experience with TasteBuds?");
        System.out.println("Rate from 1 (Poor) to 5 (Excellent)");
        int rating = ConsoleHelper.readIntInRange("Your rating (1-5): ", 1, 5);

        String comment = ConsoleHelper.readString("Any comments or suggestions? (Press Enter to skip): ");

        try {
            feedbackService.submitFeedback(order, rating, comment. isEmpty() ? null : comment);
            ConsoleHelper.printSuccess("Thank you for your feedback!");
            System.out.println("\nYour rating:  " + getStarRating(rating));
            if (! comment.isEmpty()) {
                System.out.println("Your comment: " + comment);
            }
            System.out.println("\nWe appreciate your feedback and will use it to improve our service!");
        } catch (Exception e) {
            ConsoleHelper.printError(e.getMessage());
        }

        ConsoleHelper.waitForEnter();
    }

    private String getStarRating(int rating) {
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            stars.append(i < rating ? "★" : "☆");
        }
        return stars.toString() + " (" + rating + "/5)";
    }
}