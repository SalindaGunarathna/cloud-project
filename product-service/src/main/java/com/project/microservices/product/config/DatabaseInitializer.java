package com.project.microservices.product.config;


import com.project.microservices.product.client.InventoryClient;
import com.project.microservices.product.model.Product;
import com.project.microservices.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final InventoryClient inventoryClient;

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            log.info("Initializing product database with sample data...");

            List<ProductData> productsData = createInitialProductsData();

            for (ProductData productData : productsData) {
                // Create and save product
                Product product = Product.builder()
                        .name(productData.name())
                        .description(productData.description())
                        .skuCode(productData.skuCode())
                        .price(productData.price())
                        .build();

                Product savedProduct = productRepository.save(product);
                log.info("Created product: {} with skuCode: {}", savedProduct.getName(), savedProduct.getSkuCode());

                // Add product to inventory service
                try {
                    inventoryClient.addProduct(savedProduct.getSkuCode(), productData.quantity());
                    log.info("Added product {} to inventory with quantity: {}", savedProduct.getSkuCode(), productData.quantity());
                } catch (Exception e) {
                    log.warn("Failed to add product {} to inventory: {}", savedProduct.getSkuCode(), e.getMessage());
                    // Continue with next product even if inventory service call fails
                }
            }

            log.info("Database initialized with {} products", productsData.size());
        } else {
            log.info("Database already contains {} products", productRepository.count());
        }
    }

    private List<ProductData> createInitialProductsData() {
        return List.of(
                new ProductData(
                        "Laptop",
                        "14-inch business laptop with Intel i5 processor and 8GB RAM",
                        "L-1001",
                        BigDecimal.valueOf(750.00),
                        12
                ),
                new ProductData(
                        "Smartphone",
                        "6.5-inch AMOLED display, 128GB storage, dual SIM",
                        "S-2002",
                        BigDecimal.valueOf(499.99),
                        30
                ),
                new ProductData(
                        "Wireless Headphones",
                        "Noise-cancelling over-ear headphones with 30-hour battery life",
                        "H-3003",
                        BigDecimal.valueOf(199.99),
                        20
                ),
                new ProductData(
                        "Mechanical Keyboard",
                        "RGB backlit keyboard with tactile switches",
                        "K-4004",
                        BigDecimal.valueOf(89.99),
                        40
                )
        );
    }

    // Helper record to hold product initialization data
    private record ProductData(String name, String description, String skuCode, BigDecimal price, Integer quantity) {}
}
