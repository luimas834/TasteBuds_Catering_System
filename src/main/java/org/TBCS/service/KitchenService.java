package org.TBCS.service;

import org.TBCS. model.*;
import org.TBCS.repository.*;

import java. time.LocalDateTime;
import java.util. List;

public class KitchenService {
    private final ChefRepository chefRepository;

    public KitchenService(ChefRepository chefRepository) {
        this.chefRepository = chefRepository;
    }

    public void assignChefsToOrder(Order order, List<Chef> chefs, OrderPriority priority, int estimatedMinutes) {
        for (Chef chef : chefs) {
            chef.setAvailable(false);
            chefRepository.save(chef);
            order.addChef(chef. getChefId());
        }

        order.setPriority(priority);
        order.setEstimatedMinutes(estimatedMinutes);
        order.setStatus(OrderStatus. PREPARING);
    }

    public void markOrderReady(Order order) {
        // Release all assigned chefs
        for (String chefId : order.getAssignedChefIds()) {
            Chef chef = chefRepository.findById(chefId);
            if (chef != null) {
                chef. setAvailable(true);
                chefRepository.save(chef);
            }
        }
        order.setReadyTime(LocalDateTime.now());
        order.setStatus(OrderStatus. READY);
    }

    public List<Chef> getAvailableChefs() {
        return chefRepository. findAvailable();
    }
}