package org.TBCS.ui;

import org.TBCS.repository.*;
import org.TBCS. service.*;
import org.TBCS. util.ConsoleHelper;




public class MainMenu {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";

    private final CustomerRepository customerRepository;
    private final MenuRepository menuRepository;
    private final ChefRepository chefRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final OrderRepository orderRepository;

    private final OrderService orderService;
    private final KitchenService kitchenService;
    private final DeliveryService deliveryService;
    private final FeedbackService feedbackService;

    private final CustomerMenu customerMenu;
    private final AdminMenu adminMenu;

    public MainMenu() {
        // Initialize repositories (order matters - CustomerRepository first)
        this.customerRepository = new CustomerRepository();
        this.menuRepository = new MenuRepository();
        this.chefRepository = new ChefRepository();
        this.driverRepository = new DriverRepository();
        this.vehicleRepository = new VehicleRepository();
        this.orderRepository = new OrderRepository(); 
        // Initialize services
        this.orderService = new OrderService(orderRepository, customerRepository);
        this.kitchenService = new KitchenService(chefRepository);
        this.deliveryService = new DeliveryService(driverRepository, vehicleRepository, orderRepository);
        this.feedbackService = new FeedbackService(orderRepository);

        // Initialize sub-menus
        this.customerMenu = new CustomerMenu(customerRepository, menuRepository, orderService,
                deliveryService, feedbackService);
        this.adminMenu = new AdminMenu(menuRepository, chefRepository, driverRepository,
                vehicleRepository, orderService, kitchenService,
                deliveryService, orderRepository);
    }

    public void run() {
        System.out.println(ANSI_RED);
        ConsoleHelper.printHeader("TASTEBUDS CATERING SYSTEM (TBCS)");
        System.out.println("          Welcome to TasteBuds Restaurant, Dhaka!");
        System.out.println(ANSI_RESET);

        while (true) {
            printMainMenu();
            int choice = ConsoleHelper.readIntInRange("Enter your choice: ", 0, 2);

            switch (choice) {
                case 1 -> customerMenu.show();
                case 2 -> adminLogin();
                case 0 -> {
                    ConsoleHelper.printSuccess("Thank you for using TBCS.  Goodbye!");
                    return;
                }
            }
        }
    }

    private void printMainMenu() {
        System.out.println(ANSI_YELLOW);
        ConsoleHelper.printSubHeader("MAIN MENU");
        System.out.println("  Currently Serving Order:  #" + orderService.getCurrentServingNumber());
        ConsoleHelper.printDivider();
        System.out.println("  1. Customer Portal");
        System.out.println("  2. Admin/Staff Login");
        System.out.println("  0. Exit");
        System.out.println(ANSI_RESET);
        ConsoleHelper.printDivider();
    }

    private void adminLogin() {
        ConsoleHelper.printSubHeader("ADMIN/STAFF LOGIN");
        String password = ConsoleHelper.readString("Enter password: ");

        if ("admin123".equals(password)) {
            ConsoleHelper.printSuccess("Login successful!");
            adminMenu.show();
        } else {
            ConsoleHelper.printError("Invalid password!");
            ConsoleHelper.waitForEnter();
        }
    }
}