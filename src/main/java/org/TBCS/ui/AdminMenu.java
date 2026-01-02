package org.TBCS.ui;

import org.TBCS.model.*;
import org.TBCS. repository.*;
import org.TBCS.service.*;
import org.TBCS.util.ConsoleHelper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AdminMenu {
    private final MenuRepository menuRepository;
    private final ChefRepository chefRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final KitchenService kitchenService;
    private final DeliveryService deliveryService;

    public AdminMenu(MenuRepository menuRepository, ChefRepository chefRepository,
                     DriverRepository driverRepository, VehicleRepository vehicleRepository,
                     OrderService orderService, KitchenService kitchenService,
                     DeliveryService deliveryService, OrderRepository orderRepository) {
        this.menuRepository = menuRepository;
        this.chefRepository = chefRepository;
        this. driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
        this.orderService = orderService;
        this. kitchenService = kitchenService;
        this.deliveryService = deliveryService;
        this.orderRepository = orderRepository;
    }

    public void show() {
        while (true) {
            ConsoleHelper.printSubHeader("ADMIN PANEL");
            System.out.println("  Currently Serving: #" + orderService.getCurrentServingNumber());
            ConsoleHelper.printDivider();
            System.out.println("  1. Manage Menu Items");
            System.out.println("  2. Manage Chefs");
            System.out. println("  3. Manage Drivers");
            System.out. println("  4. Manage Vehicles");
            System.out. println("  5. Kitchen Preparation (Head Chef)");
            System.out.println("  6. Delivery Assignment");
            System.out.println("  7. Driver Checkout");
            System.out.println("  8. View All Orders");
            System.out. println("  9. View Statistics");
            System.out.println("  0.  Logout");
            ConsoleHelper.printDivider();

            int choice = ConsoleHelper. readIntInRange("Enter your choice: ", 0, 9);

            switch (choice) {
                case 1 -> manageMenu();
                case 2 -> manageChefs();
                case 3 -> manageDrivers();
                case 4 -> manageVehicles();
                case 5 -> kitchenPreparation();
                case 6 -> deliveryAssignment();
                case 7 -> driverCheckout();
                case 8 -> viewAllOrders();
                case 9 -> viewStatistics();
                case 0 -> { return; }
            }
        }
    }

    // ==================== MENU MANAGEMENT ====================
    private void manageMenu() {
        while (true) {
            ConsoleHelper.printSubHeader("MENU MANAGEMENT");
            System.out.println("  1. View All Menu Items");
            System.out. println("  2. Add New Item");
            System.out.println("  3. Update Item Price");
            System.out.println("  4. Toggle Item Availability");
            System. out.println("  5. Delete Item");
            System.out. println("  0. Back");
            ConsoleHelper.printDivider();

            int choice = ConsoleHelper.readIntInRange("Enter your choice: ", 0, 5);

            switch (choice) {
                case 1 -> viewAllMenuItems();
                case 2 -> addMenuItem();
                case 3 -> updateMenuItemPrice();
                case 4 -> toggleMenuItemAvailability();
                case 5 -> deleteMenuItem();
                case 0 -> { return; }
            }
        }
    }

    private void viewAllMenuItems() {
        ConsoleHelper.printSubHeader("ALL MENU ITEMS");
        List<MenuItem> items = menuRepository.findAll();

        if (items.isEmpty()) {
            System.out.println("  No menu items found.");
        } else {
            System.out.printf("  %-6s %-25s %-15s %-10s %-10s%n", "ID", "Name", "Category", "Price", "Status");
            ConsoleHelper.printDivider();
            for (MenuItem item : items) {
                System.out.printf("  %-6s %-25s %-15s Tk%-7s %-10s%n",
                        item.getItemId(), item.getName(), item.getCategory(),
                        item.getPrice(), item.isAvailable() ? "Available" : "Unavailable");
            }
        }
        ConsoleHelper.waitForEnter();
    }

    private void addMenuItem() {
        ConsoleHelper.printSubHeader("ADD NEW MENU ITEM");

        String name = ConsoleHelper.readString("Item name: ");
        String category = ConsoleHelper.readString("Category (e.g., Main Course, Beverage, Dessert): ");
        BigDecimal price = ConsoleHelper.readBigDecimal("Price (Tk): ");

        String itemId = menuRepository.generateItemId();
        MenuItem newItem = new MenuItem(itemId, name, category, price);
        menuRepository.save(newItem);

        ConsoleHelper.printSuccess("Menu item added successfully!");
        System.out.println("Item ID: " + itemId);
        ConsoleHelper.waitForEnter();
    }

    private void updateMenuItemPrice() {
        ConsoleHelper.printSubHeader("UPDATE ITEM PRICE");

        String itemId = ConsoleHelper.readString("Enter Item ID: ");
        MenuItem item = menuRepository.findById(itemId);

        if (item == null) {
            ConsoleHelper.printError("Item not found!");
        } else {
            System.out.println("Current: " + item.getName() + " - Tk " + item.getPrice());
            BigDecimal newPrice = ConsoleHelper.readBigDecimal("New price (Tk): ");
            item.setPrice(newPrice);
            menuRepository.save(item);
            ConsoleHelper.printSuccess("Price updated successfully!");
        }
        ConsoleHelper.waitForEnter();
    }

    private void toggleMenuItemAvailability() {
        ConsoleHelper.printSubHeader("TOGGLE ITEM AVAILABILITY");

        String itemId = ConsoleHelper.readString("Enter Item ID: ");
        MenuItem item = menuRepository.findById(itemId);

        if (item == null) {
            ConsoleHelper.printError("Item not found!");
        } else {
            item.setAvailable(!item.isAvailable());
            menuRepository.save(item);
            ConsoleHelper.printSuccess(item.getName() + " is now " +
                    (item.isAvailable() ? "AVAILABLE" : "UNAVAILABLE"));
        }
        ConsoleHelper.waitForEnter();
    }

    private void deleteMenuItem() {
        ConsoleHelper.printSubHeader("DELETE MENU ITEM");

        String itemId = ConsoleHelper.readString("Enter Item ID to delete: ");
        MenuItem item = menuRepository.findById(itemId);

        if (item == null) {
            ConsoleHelper. printError("Item not found!");
        } else {
            System.out.println("Item:  " + item.getName());
            if (ConsoleHelper.readYesNo("Are you sure you want to delete this item?")) {
                menuRepository.delete(itemId);
                ConsoleHelper. printSuccess("Item deleted successfully!");
            }
        }
        ConsoleHelper.waitForEnter();
    }

    // ==================== CHEF MANAGEMENT ====================
    private void manageChefs() {
        while (true) {
            ConsoleHelper.printSubHeader("CHEF MANAGEMENT");
            System.out.println("  1. View All Chefs");
            System.out.println("  2. Add New Chef");
            System. out.println("  3. Delete Chef");
            System.out. println("  0. Back");
            ConsoleHelper.printDivider();

            int choice = ConsoleHelper.readIntInRange("Enter your choice:  ", 0, 3);

            switch (choice) {
                case 1 -> viewAllChefs();
                case 2 -> addChef();
                case 3 -> deleteChef();
                case 0 -> { return; }
            }
        }
    }

    private void viewAllChefs() {
        ConsoleHelper.printSubHeader("ALL CHEFS");
        List<Chef> chefs = chefRepository.findAll();

        if (chefs.isEmpty()) {
            System.out.println("  No chefs found.");
        } else {
            System.out. printf("  %-10s %-20s %-20s %-10s%n", "ID", "Name", "Specialization", "Status");
            ConsoleHelper.printDivider();
            for (Chef chef : chefs) {
                System.out.printf("  %-10s %-20s %-20s %-10s%n",
                        chef.getChefId(), chef.getName(), chef.getSpecialization(),
                        chef.isAvailable() ? "Available" : "Busy");
            }
        }
        ConsoleHelper.waitForEnter();
    }

    private void addChef() {
        ConsoleHelper.printSubHeader("ADD NEW CHEF");

        String name = ConsoleHelper.readString("Chef name: ");
        String specialization = ConsoleHelper.readString("Specialization:  ");

        Chef chef = new Chef(name, specialization);
        chefRepository.save(chef);

        ConsoleHelper.printSuccess("Chef added successfully!");
        System.out.println("Chef ID: " + chef.getChefId());
        ConsoleHelper.waitForEnter();
    }

    private void deleteChef() {
        ConsoleHelper.printSubHeader("DELETE CHEF");

        String chefId = ConsoleHelper.readString("Enter Chef ID:  ");
        Chef chef = chefRepository.findById(chefId);

        if (chef == null) {
            ConsoleHelper. printError("Chef not found!");
        } else if (! chef.isAvailable()) {
            ConsoleHelper.printError("Cannot delete chef who is currently assigned to an order!");
        } else {
            System.out.println("Chef: " + chef.getName());
            if (ConsoleHelper.readYesNo("Are you sure? ")) {
                chefRepository. delete(chefId);
                ConsoleHelper.printSuccess("Chef deleted successfully!");
            }
        }
        ConsoleHelper.waitForEnter();
    }

    // ==================== DRIVER MANAGEMENT ====================
    private void manageDrivers() {
        while (true) {
            ConsoleHelper.printSubHeader("DRIVER MANAGEMENT");
            System.out.println("  1. View All Drivers");
            System.out.println("  2. Add New Driver");
            System.out. println("  3. View Driver License Details");
            System.out.println("  4. Delete Driver");
            System.out. println("  0. Back");
            ConsoleHelper.printDivider();

            int choice = ConsoleHelper.readIntInRange("Enter your choice: ", 0, 4);

            switch (choice) {
                case 1 -> viewAllDrivers();
                case 2 -> addDriver();
                case 3 -> viewDriverLicense();
                case 4 -> deleteDriver();
                case 0 -> { return; }
            }
        }
    }

    private void viewAllDrivers() {
        ConsoleHelper.printSubHeader("ALL DRIVERS");
        List<Driver> drivers = driverRepository.findAll();

        if (drivers.isEmpty()) {
            System.out.println("  No drivers found.");
        } else {
            System.out.printf("  %-10s %-15s %-15s %-15s %-10s%n",
                    "ID", "Name", "Phone", "License", "Status");
            ConsoleHelper.printDivider();
            for (Driver driver : drivers) {
                String licenseStatus = driver.hasValidLicense() ? "Valid" : "EXPIRED";
                System.out. printf("  %-10s %-15s %-15s %-15s %-10s%n",
                        driver.getDriverId(), driver.getName(), driver.getPhone(),
                        driver.getDrivingLicense().getLicenseNumber(),
                        driver.isAvailable() ? "Available" : "On Delivery");
            }
        }
        ConsoleHelper.waitForEnter();
    }

    private void addDriver() {
        ConsoleHelper.printSubHeader("ADD NEW DRIVER");

        String name = ConsoleHelper.readString("Driver name: ");
        String phone = ConsoleHelper.readString("Phone number: ");

        System.out.println("\nDriving License Details (for security purposes):");
        String licenseNumber = ConsoleHelper. readString("License number: ");
        String licenseType = ConsoleHelper.readString("License type (e.g., Commercial): ");

        System.out.println("Issue date:");
        int issueYear = ConsoleHelper.readInt("  Year (e.g., 2024): ");
        int issueMonth = ConsoleHelper.readIntInRange("  Month (1-12): ", 1, 12);
        int issueDay = ConsoleHelper.readIntInRange("  Day (1-31): ", 1, 31);

        System.out.println("Expiry date:");
        int expiryYear = ConsoleHelper.readInt("  Year (e.g., 2029): ");
        int expiryMonth = ConsoleHelper.readIntInRange("  Month (1-12): ", 1, 12);
        int expiryDay = ConsoleHelper.readIntInRange("  Day (1-31): ", 1, 31);

        LocalDate issueDate = LocalDate. of(issueYear, issueMonth, issueDay);
        LocalDate expiryDate = LocalDate.of(expiryYear, expiryMonth, expiryDay);

        DrivingLicense license = new DrivingLicense(licenseNumber, issueDate, expiryDate, licenseType);
        Driver driver = new Driver(name, phone, license);
        driverRepository.save(driver);

        ConsoleHelper.printSuccess("Driver added successfully!");
        System.out. println("Driver ID: " + driver.getDriverId());
        ConsoleHelper.waitForEnter();
    }

    private void viewDriverLicense() {
        ConsoleHelper.printSubHeader("DRIVER LICENSE DETAILS");

        String driverId = ConsoleHelper.readString("Enter Driver ID: ");
        Driver driver = driverRepository. findById(driverId);

        if (driver == null) {
            ConsoleHelper.printError("Driver not found!");
        } else {
            DrivingLicense license = driver. getDrivingLicense();
            System.out.println("\n+------------------------------------------+");
            System.out. println("|          DRIVING LICENSE DETAILS         |");
            System.out. println("+------------------------------------------+");
            System.out.printf("| Driver:  %-32s |%n", driver.getName());
            System.out.printf("| License No: %-28s |%n", license.getLicenseNumber());
            System. out.printf("| Type: %-34s |%n", license.getLicenseType());
            System. out.printf("| Issue Date: %-28s |%n", license.getIssueDate());
            System.out.printf("| Expiry Date: %-27s |%n", license.getExpiryDate());
            System.out.printf("| Status: %-32s |%n", license.isValid() ? "VALID" : "EXPIRED");
            System.out.println("+------------------------------------------+");
        }
        ConsoleHelper.waitForEnter();
    }

    private void deleteDriver() {
        ConsoleHelper.printSubHeader("DELETE DRIVER");

        String driverId = ConsoleHelper.readString("Enter Driver ID: ");
        Driver driver = driverRepository.findById(driverId);

        if (driver == null) {
            ConsoleHelper.printError("Driver not found!");
        } else if (!driver.isAvailable()) {
            ConsoleHelper.printError("Cannot delete driver who is currently on delivery!");
        } else {
            System.out.println("Driver: " + driver.getName());
            if (ConsoleHelper.readYesNo("Are you sure? ")) {
                driverRepository. delete(driverId);
                ConsoleHelper.printSuccess("Driver deleted successfully!");
            }
        }
        ConsoleHelper.waitForEnter();
    }

    // ==================== VEHICLE MANAGEMENT ====================
    private void manageVehicles() {
        while (true) {
            ConsoleHelper.printSubHeader("VEHICLE MANAGEMENT");
            System. out.println("  1. View All Vehicles");
            System. out.println("  2. Add New Vehicle");
            System.out.println("  3. Delete Vehicle");
            System.out. println("  0. Back");
            ConsoleHelper.printDivider();

            int choice = ConsoleHelper.readIntInRange("Enter your choice: ", 0, 3);

            switch (choice) {
                case 1 -> viewAllVehicles();
                case 2 -> addVehicle();
                case 3 -> deleteVehicle();
                case 0 -> { return; }
            }
        }
    }

    private void viewAllVehicles() {
        ConsoleHelper.printSubHeader("ALL VEHICLES");
        List<Vehicle> vehicles = vehicleRepository. findAll();

        if (vehicles.isEmpty()) {
            System. out.println("  No vehicles found.");
        } else {
            System.out.printf("  %-10s %-15s %-15s %-10s%n", "ID", "Reg.  Number", "Type", "Status");
            ConsoleHelper.printDivider();
            for (Vehicle vehicle : vehicles) {
                System.out.printf("  %-10s %-15s %-15s %-10s%n",
                        vehicle.getVehicleId(), vehicle.getRegistrationNumber(),
                        vehicle. getVehicleType(), vehicle.isAvailable() ? "Available" : "In Use");
            }
        }
        ConsoleHelper.waitForEnter();
    }

    private void addVehicle() {
        ConsoleHelper.printSubHeader("ADD NEW VEHICLE");

        String regNumber = ConsoleHelper.readString("Registration number: ");
        String type = ConsoleHelper.readString("Vehicle type (e.g., Motorcycle, Car): ");

        Vehicle vehicle = new Vehicle(regNumber, type);
        vehicleRepository.save(vehicle);

        ConsoleHelper.printSuccess("Vehicle added successfully!");
        System.out.println("Vehicle ID: " + vehicle.getVehicleId());
        ConsoleHelper.waitForEnter();
    }

    private void deleteVehicle() {
        ConsoleHelper.printSubHeader("DELETE VEHICLE");

        String vehicleId = ConsoleHelper.readString("Enter Vehicle ID:  ");
        Vehicle vehicle = vehicleRepository.findById(vehicleId);

        if (vehicle == null) {
            ConsoleHelper. printError("Vehicle not found!");
        } else if (!vehicle.isAvailable()) {
            ConsoleHelper.printError("Cannot delete vehicle currently in use!");
        } else {
            System.out.println("Vehicle: " + vehicle.getRegistrationNumber());
            if (ConsoleHelper.readYesNo("Are you sure?")) {
                vehicleRepository.delete(vehicleId);
                ConsoleHelper. printSuccess("Vehicle deleted successfully!");
            }
        }
        ConsoleHelper.waitForEnter();
    }

    // ==================== KITCHEN PREPARATION (HEAD CHEF) ====================
    private void kitchenPreparation() {
        ConsoleHelper.printSubHeader("KITCHEN PREPARATION (Head Chef)");

        // Show orders in queue
        List<Order> queuedOrders = orderService.getOrdersByStatus(OrderStatus.IN_QUEUE);

        if (queuedOrders.isEmpty()) {
            ConsoleHelper.printInfo("No orders in queue.");
            ConsoleHelper.waitForEnter();
            return;
        }

        System.out.println("Orders in Queue:");
        ConsoleHelper.printDivider();
        for (Order order : queuedOrders) {
            System.out.printf("  Queue #%d - %s - %d items - Tk %s%n",
                    order. getQueueNumber(), order.getCustomer().getName(),
                    order.getItems().size(), order.getFinalAmount());
        }
        ConsoleHelper.printDivider();

        int queueNum = ConsoleHelper.readInt("Enter Queue Number to prepare (0 to cancel): ");
        if (queueNum == 0) return;

        Order order = orderService.getOrderByQueueNumber(queueNum);
        if (order == null || order.getStatus() != OrderStatus.IN_QUEUE) {
            ConsoleHelper.printError("Invalid order or order not in queue!");
            ConsoleHelper.waitForEnter();
            return;
        }

        // Show order details
        System.out.println("\n--- Order Details ---");
        System.out.println("Customer: " + order.getCustomer().getName());
        System.out.println("Items:");
        for (OrderItem item : order.getItems()) {
            System.out.println("  - " + item);
        }
        System.out.println("Total: Tk " + order.getFinalAmount());
        ConsoleHelper.printDivider();

        // Set priority
        System.out.println("\nOrder Priority:");
        System.out. println("  1. Normal Order");
        System.out.println("  2. Priority Order");
        int priorityChoice = ConsoleHelper.readIntInRange("Select priority:  ", 1, 2);
        OrderPriority priority = (priorityChoice == 2) ? OrderPriority.PRIORITY : OrderPriority. NORMAL;

        // Assign chefs
        List<Chef> availableChefs = kitchenService.getAvailableChefs();
        if (availableChefs.isEmpty()) {
            ConsoleHelper.printError("No available chefs!");
            ConsoleHelper.waitForEnter();
            return;
        }

        System.out.println("\nAvailable Chefs:");
        for (int i = 0; i < availableChefs.size(); i++) {
            Chef chef = availableChefs.get(i);
            System.out.printf("  %d. %s (%s)%n", i + 1, chef.getName(), chef.getSpecialization());
        }

        int numChefs = ConsoleHelper.readIntInRange(
                "How many chefs to assign (1-" + Math.min(3, availableChefs.size()) + "): ",
                1, Math.min(3, availableChefs.size()));

        List<Chef> selectedChefs = new ArrayList<>();
        for (int i = 0; i < numChefs; i++) {
            int chefChoice = ConsoleHelper.readIntInRange(
                    "Select chef " + (i + 1) + ": ", 1, availableChefs.size());
            Chef selected = availableChefs.get(chefChoice - 1);
            if (! selectedChefs.contains(selected)) {
                selectedChefs.add(selected);
            }
        }

        // Estimated time
        int estimatedTime = ConsoleHelper. readIntInRange("Estimated preparation time (minutes): ", 5, 120);

        // Assign chefs and start preparation
        kitchenService.assignChefsToOrder(order, selectedChefs, priority, estimatedTime);

        // Show preparation details
        ConsoleHelper.printSuccess("Order is now being prepared!");
        System.out.println("\n+------------------------------------------+");
        System.out.println("|      KITCHEN PREPARATION DETAILS         |");
        System.out. println("+------------------------------------------+");
        System.out.printf("| Order #:  %-31d |%n", order.getQueueNumber());
        System.out.printf("| Priority: %-30s |%n", priority.getDisplayName());
        System.out. println("| Assigned Chefs:                           |");
        for (Chef chef : selectedChefs) {
            System.out.printf("|   - %-36s |%n", chef.getName() + " (" + chef.getSpecialization() + ")");
        }
        System.out.printf("| Estimated Time: %-24s |%n", estimatedTime + " minutes");
        System.out.printf("| Status: %-32s |%n", order.getStatus().getDisplayName());
        System.out.println("+------------------------------------------+");

        // Option to mark ready
        if (ConsoleHelper.readYesNo("\nMark order as READY now? ")) {
            kitchenService.markOrderReady(order);
            ConsoleHelper.printSuccess("Order #" + order.getQueueNumber() + " is now READY for delivery!");
        }

        ConsoleHelper.waitForEnter();
    }

    // ==================== DELIVERY ASSIGNMENT ====================
    private void deliveryAssignment() {
        ConsoleHelper.printSubHeader("DELIVERY ASSIGNMENT");

        // Show ready orders
        List<Order> readyOrders = orderService.getOrdersByStatus(OrderStatus. READY);

        if (readyOrders.isEmpty()) {
            ConsoleHelper.printInfo("No orders ready for delivery.");
            ConsoleHelper.waitForEnter();
            return;
        }

        System.out.println("Orders Ready for Delivery:");
        ConsoleHelper.printDivider();
        for (Order order : readyOrders) {
            String priorityFlag = order.getPriority() == OrderPriority.PRIORITY ? " [PRIORITY]" : "";
            System.out.printf("  Queue #%d - %s - Tk %s%s%n",
                    order.getQueueNumber(), order.getCustomer().getName(),
                    order.getFinalAmount(), priorityFlag);
        }
        ConsoleHelper. printDivider();

        int queueNum = ConsoleHelper. readInt("Enter Queue Number to assign delivery (0 to cancel): ");
        if (queueNum == 0) return;

        Order order = orderService.getOrderByQueueNumber(queueNum);
        if (order == null || order.getStatus() != OrderStatus.READY) {
            ConsoleHelper.printError("Invalid order or order not ready!");
            ConsoleHelper.waitForEnter();
            return;
        }

        // Check if priority order and time constraint
        if (order.getPriority() == OrderPriority.PRIORITY) {
            ConsoleHelper.printWarning("PRIORITY ORDER - Must assign driver within 10 minutes!");
        }

        // Show available drivers
        List<Driver> availableDrivers = deliveryService.getAvailableDrivers();
        if (availableDrivers.isEmpty()) {
            ConsoleHelper.printError("No available drivers with valid license!");
            ConsoleHelper.waitForEnter();
            return;
        }

        System.out.println("\nAvailable Drivers:");
        for (int i = 0; i < availableDrivers.size(); i++) {
            Driver driver = availableDrivers.get(i);
            System.out.printf("  %d. %s - License: %s%n",
                    i + 1, driver.getName(), driver.getDrivingLicense().getLicenseNumber());
        }

        int driverChoice = ConsoleHelper.readIntInRange("Select driver: ", 1, availableDrivers.size());
        Driver selectedDriver = availableDrivers.get(driverChoice - 1);

        // Show available vehicles
        List<Vehicle> availableVehicles = deliveryService. getAvailableVehicles();
        if (availableVehicles.isEmpty()) {
            ConsoleHelper.printError("No available vehicles!");
            ConsoleHelper. waitForEnter();
            return;
        }

        System. out.println("\nAvailable Vehicles:");
        for (int i = 0; i < availableVehicles.size(); i++) {
            Vehicle vehicle = availableVehicles.get(i);
            System.out.printf("  %d. %s (%s)%n",
                    i + 1, vehicle.getRegistrationNumber(), vehicle.getVehicleType());
        }

        int vehicleChoice = ConsoleHelper.readIntInRange("Select vehicle: ", 1, availableVehicles.size());
        Vehicle selectedVehicle = availableVehicles.get(vehicleChoice - 1);

        // Create and assign delivery
        Delivery delivery = deliveryService.createDelivery(order);
        boolean success = deliveryService.assignDelivery(delivery, selectedDriver, selectedVehicle);

        if (success) {
            ConsoleHelper. printSuccess("Delivery assigned successfully!");
            System.out.println("\n+------------------------------------------+");
            System.out.println("|        DELIVERY ASSIGNMENT DETAILS       |");
            System.out. println("+------------------------------------------+");
            System.out.printf("| Order #: %-31d |%n", order.getQueueNumber());
            System.out.printf("| Priority: %-30s |%n", order.getPriority().getDisplayName());
            System.out.printf("| Driver: %-32s |%n", selectedDriver. getName());
            System.out. printf("| License: %-31s |%n", selectedDriver. getDrivingLicense().getLicenseNumber());
            System.out.printf("| Vehicle: %-31s |%n", selectedVehicle.getRegistrationNumber());
            System.out.printf("| Vehicle Type: %-26s |%n", selectedVehicle.getVehicleType());
            System.out.println("+------------------------------------------+");
            System.out.println("\nDriver must checkout using license number: " +
                    selectedDriver.getDrivingLicense().getLicenseNumber());
        } else {
            ConsoleHelper.printError("Failed to assign delivery!");
        }

        ConsoleHelper.waitForEnter();
    }

    // ==================== DRIVER CHECKOUT ====================
    private void driverCheckout() {
        ConsoleHelper.printSubHeader("DRIVER CHECKOUT");
        System.out.println("Driver must verify identity with license number.\n");

        int queueNum = ConsoleHelper.readInt("Enter Order Queue Number: ");
        String licenseNumber = ConsoleHelper. readString("Enter Driving License Number: ");

        Delivery delivery = deliveryService.getDeliveryByQueueNumber(queueNum);

        if (delivery == null) {
            ConsoleHelper.printError("No delivery found for this order!");
            ConsoleHelper.waitForEnter();
            return;
        }

        if (delivery.getDriver() == null) {
            ConsoleHelper.printError("No driver assigned to this delivery!");
            ConsoleHelper.waitForEnter();
            return;
        }

        boolean success = deliveryService. driverCheckout(delivery, licenseNumber);

        if (success) {
            ConsoleHelper.printSuccess("Driver Checkout Successful!");
            System.out.println("\n+------------------------------------------+");
            System.out.println("|          CHECKOUT CONFIRMATION           |");
            System.out. println("+------------------------------------------+");
            System.out.printf("| Order #: %-31d |%n", delivery.getOrder().getQueueNumber());
            System.out.printf("| Driver: %-32s |%n", delivery.getDriver().getName());
            System.out.printf("| License: %-31s |%n", licenseNumber);
            System.out.printf("| Vehicle: %-31s |%n", delivery.getVehicle().getRegistrationNumber());
            System.out.printf("| Checkout Time: %-25s |%n",
                    delivery.getCheckoutTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            System. out.printf("| Status: %-32s |%n", delivery.getOrder().getStatus().getDisplayName());
            System.out.println("+------------------------------------------+");
            System.out.println("\nDelivery is now OUT FOR DELIVERY.");
        } else {
            ConsoleHelper.printError("Checkout failed!  License number does not match assigned driver.");
        }

        ConsoleHelper.waitForEnter();
    }

    // ==================== VIEW ALL ORDERS ====================
    private void viewAllOrders() {
        ConsoleHelper.printSubHeader("ALL ORDERS");

        List<Order> orders = orderService.getAllOrders();

        if (orders.isEmpty()) {
            System.out.println("  No orders found.");
        } else {
            System.out.printf("  %-8s %-15s %-10s %-12s %-10s%n",
                    "Queue#", "Customer", "Items", "Total", "Status");
            ConsoleHelper.printDivider();
            for (Order order : orders) {
                System.out.printf("  %-8d %-15s %-10d Tk%-9s %-10s%n",
                        order.getQueueNumber(),
                        order.getCustomer().getName().substring(0, Math.min(14, order.getCustomer().getName().length())),
                        order.getItems().size(),
                        order.getFinalAmount(),
                        order.getStatus().name());
            }
        }
        ConsoleHelper.waitForEnter();
    }

    // ==================== STATISTICS ====================
    private void viewStatistics() {
        ConsoleHelper.printSubHeader("SYSTEM STATISTICS");

        List<Order> allOrders = orderService.getAllOrders();
        FeedbackService feedbackService = new FeedbackService(orderRepository);

        int totalOrders = allOrders. size();
        int deliveredOrders = orderService.getOrdersByStatus(OrderStatus.DELIVERED).size();
        int pendingOrders = totalOrders - deliveredOrders;

        BigDecimal totalRevenue = BigDecimal.ZERO;
        for (Order order : allOrders) {
            if (order.getStatus() == OrderStatus.DELIVERED) {
                totalRevenue = totalRevenue. add(order.getFinalAmount());
            }
        }

        System.out.println("\n+------------------------------------------+");
        System.out.println("|            SYSTEM STATISTICS             |");
        System.out.println("+------------------------------------------+");
        System.out.printf("| Total Orders: %-26d |%n", totalOrders);
        System.out.printf("| Delivered Orders: %-22d |%n", deliveredOrders);
        System.out. printf("| Pending Orders: %-24d |%n", pendingOrders);
        System.out. printf("| Total Revenue: %-25s |%n", "Tk " + totalRevenue);
        System.out.printf("| Average Rating: %-24s |%n",
                String.format("%.1f / 5", feedbackService.getAverageRating()));
        System.out.println("+------------------------------------------+");
        System.out.printf("| Available Chefs: %-23d |%n", chefRepository.findAvailable().size());
        System.out.printf("| Available Drivers: %-21d |%n", driverRepository.findAvailable().size());
        System.out.printf("| Available Vehicles: %-20d |%n", vehicleRepository.findAvailable().size());
        System.out.printf("| Menu Items: %-28d |%n", menuRepository.findAll().size());
        System.out.println("+------------------------------------------+");

        ConsoleHelper.waitForEnter();
    }
}