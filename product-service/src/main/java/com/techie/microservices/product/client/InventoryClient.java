package com.techie.microservices.product.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PostExchange;

public interface InventoryClient {

    @PostExchange("/api/inventory/add")
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackAddProduct")
    @Retry(name = "inventory")
    void addProduct(@RequestParam String skuCode, @RequestParam Integer quantity);


    default Object fallbackAddProduct(String skuCode, Integer quantity, Throwable throwable) {
        return null;
    }

}
