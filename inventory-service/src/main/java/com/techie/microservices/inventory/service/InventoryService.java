package com.techie.microservices.inventory.service;

import com.techie.microservices.inventory.model.Inventory;
import com.techie.microservices.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public boolean isInStock(String skuCode, Integer quantity) {
        log.info(" Start -- Received request to check stock for skuCode {}, with quantity {}", skuCode, quantity);
        boolean isInStock = inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity);
        log.info(" End -- Product with skuCode {}, and quantity {}, is in stock - {}", skuCode, quantity, isInStock);
        return isInStock;
    }

    public void updateInventoryOnProductCreation(String skuCode, Integer quantity) {
        log.info("Start -- Adding product to inventory: skuCode={}, quantity={}", skuCode, quantity);

        Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElse(new Inventory(null, skuCode, 0));

        int updatedQuantity = inventory.getQuantity() + quantity;
        inventory.setQuantity(updatedQuantity);
        inventoryRepository.save(inventory);

        log.info("End -- Inventory updated for skuCode={}, newQuantity={}", skuCode, updatedQuantity);
    }


    public boolean updateInventoryOnOrderPlaced(String skuCode, Integer orderQuantity) {
        log.info("Start -- Updating inventory for order: skuCode={}, orderQuantity={}", skuCode, orderQuantity);

        Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElse(null);

        if (inventory == null) {
            log.warn("Failed -- skuCode={} not found in inventory", skuCode);
            return false;
        }

        if (inventory.getQuantity() < orderQuantity) {
            log.warn("Failed -- Not enough stock for skuCode={}, available={}, requested={}",
                    skuCode, inventory.getQuantity(), orderQuantity);
            return false;
        }

        int updatedQuantity = inventory.getQuantity() - orderQuantity;
        inventory.setQuantity(updatedQuantity);
        inventoryRepository.save(inventory);

        log.info("End -- Inventory updated for skuCode={}, orderQuantity={}, remainingQuantity={}",
                skuCode, orderQuantity, updatedQuantity);
        return true;
    }
}

