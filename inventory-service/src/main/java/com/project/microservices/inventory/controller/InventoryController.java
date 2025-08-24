package com.project.microservices.inventory.controller;

import com.project.microservices.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity) {
        return inventoryService.isInStock(skuCode, quantity);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void updateInventoryOnProductCreation(@RequestParam String skuCode, @RequestParam Integer quantity) {
        inventoryService.updateInventoryOnProductCreation(skuCode, quantity);
    }

    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public boolean updateInventoryOnOrderPlaced(@RequestParam String skuCode, @RequestParam Integer orderQuantity) {
        return inventoryService.updateInventoryOnOrderPlaced(skuCode, orderQuantity);
    }
}
