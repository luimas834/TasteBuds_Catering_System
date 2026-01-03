package org.TBCS.service;

import org.TBCS. model.Chef;
import org.TBCS.model.Customer;
import org.TBCS.model.OrderPriority;
import org. TBCS.model.OrderStatus;
import org.TBCS.repository.ChefRepository;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.junit. jupiter.api.Assertions.*;

/**
 * Integration tests for KitchenService.
 * Tests the "Kitchen Preparation (Head Chef)" scenario.
 */
public class KitchenServiceTest {

    private KitchenService kitchenService;
    private ChefRepository chefRepository;
    private Customer customer;
    private org.TBCS.model.Order order;  // Fully qualified to avoid conflict with JUnit's @Order

    @BeforeEach
    void setUp() {
        chefRepository = new ChefRepository();
        kitchenService = new KitchenService(chefRepository);
        customer = new Customer("Test Customer", "01712345678", "test@email.com", true);
        order = new org. TBCS.model.Order(1, customer);

        // Reset all chefs to available before each test
        resetAllChefsToAvailable();
    }

    @AfterEach
    void tearDown() {
        // Reset all chefs to available after each test
        resetAllChefsToAvailable();
    }

    private void resetAllChefsToAvailable() {
        List<Chef> allChefs = chefRepository.findAll();
        for (Chef chef : allChefs) {
            chef.setAvailable(true);
            chefRepository.save(chef);
        }
    }

    @Test
    @DisplayName("Scenario: Kitchen Preparation - Assign chefs to order")
    void testAssignChefsToOrder() {
        List<Chef> availableChefs = kitchenService.getAvailableChefs();
        assertFalse(availableChefs.isEmpty(), "Should have at least one available chef");

        Chef chef = availableChefs.get(0);

        kitchenService.assignChefsToOrder(order, Arrays.asList(chef), OrderPriority.NORMAL, 30);

        assertTrue(order.getAssignedChefIds().contains(chef.getChefId()));
        assertEquals(OrderPriority.NORMAL, order. getPriority());
        assertEquals(30, order.getEstimatedMinutes());
        assertEquals(OrderStatus.PREPARING, order.getStatus());
    }

    @Test
    @DisplayName("Scenario: Kitchen Preparation - Assign as priority order")
    void testAssignPriorityOrder() {
        List<Chef> availableChefs = kitchenService. getAvailableChefs();
        assertFalse(availableChefs.isEmpty(), "Should have at least one available chef");

        Chef chef = availableChefs. get(0);

        kitchenService.assignChefsToOrder(order, Arrays.asList(chef), OrderPriority.PRIORITY, 15);

        assertEquals(OrderPriority.PRIORITY, order.getPriority());
        assertEquals(15, order.getEstimatedMinutes());
    }

    @Test
    @DisplayName("Scenario: Kitchen Preparation - Assign multiple chefs")
    void testAssignMultipleChefs() {
        List<Chef> availableChefs = kitchenService. getAvailableChefs();

        Assumptions.assumeTrue(availableChefs.size() >= 2,
                "Skipping test:  Need at least 2 available chefs");

        List<Chef> chefsToAssign = availableChefs.subList(0, 2);

        kitchenService.assignChefsToOrder(order, chefsToAssign, OrderPriority. NORMAL, 45);

        assertEquals(2, order.getAssignedChefIds().size());
    }

    @Test
    @DisplayName("Scenario: Kitchen Preparation - Chef becomes unavailable when assigned")
    void testChefUnavailableWhenAssigned() {
        List<Chef> availableChefs = kitchenService.getAvailableChefs();
        assertFalse(availableChefs.isEmpty(), "Should have at least one available chef");

        Chef chef = availableChefs.get(0);
        String chefId = chef.getChefId();

        assertTrue(chef.isAvailable());

        kitchenService.assignChefsToOrder(order, Arrays.asList(chef), OrderPriority.NORMAL, 30);

        Chef updatedChef = chefRepository. findById(chefId);
        assertFalse(updatedChef. isAvailable());
    }

    @Test
    @DisplayName("Scenario: Kitchen Preparation - Mark order ready releases chefs")
    void testMarkOrderReadyReleasesChefs() {
        List<Chef> availableChefs = kitchenService.getAvailableChefs();
        assertFalse(availableChefs.isEmpty(), "Should have at least one available chef");

        Chef chef = availableChefs.get(0);
        String chefId = chef.getChefId();

        kitchenService.assignChefsToOrder(order, Arrays.asList(chef), OrderPriority.NORMAL, 30);

        assertFalse(chefRepository.findById(chefId).isAvailable());

        kitchenService.markOrderReady(order);

        assertTrue(chefRepository. findById(chefId).isAvailable());
        assertEquals(OrderStatus.READY, order.getStatus());
        assertNotNull(order.getReadyTime());
    }

    @Test
    @DisplayName("Scenario: Kitchen Preparation - Estimated time is set")
    void testEstimatedTimeSet() {
        List<Chef> availableChefs = kitchenService.getAvailableChefs();
        assertFalse(availableChefs.isEmpty(), "Should have at least one available chef");

        Chef chef = availableChefs.get(0);

        kitchenService.assignChefsToOrder(order, Arrays.asList(chef), OrderPriority.NORMAL, 25);

        assertEquals(25, order.getEstimatedMinutes());
    }
}