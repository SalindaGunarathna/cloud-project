package com.project.microservices.order.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface InventoryClient {

    @GetExchange("/api/inventory")
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackIsInStock")
    @Retry(name = "inventory")
    boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity);

    @PostExchange("/api/inventory/add")
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackAddProduct")
    @Retry(name = "inventory")
    void addProduct(@RequestParam String skuCode, @RequestParam Integer quantity);

    @PostExchange("/api/inventory/remove")
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackRemoveProduct")
    @Retry(name = "inventory")
    boolean removeProduct(@RequestParam String skuCode, @RequestParam Integer orderQuantity);


    default boolean fallbackIsInStock(String skuCode, Integer quantity, Throwable throwable) {
        return false;
    }

    default void fallbackAddProduct(String skuCode, Integer quantity, Throwable throwable) {

    }

    default boolean fallbackRemoveProduct(String skuCode, Integer orderQuantity, Throwable throwable) {
        return false;
    }
}
