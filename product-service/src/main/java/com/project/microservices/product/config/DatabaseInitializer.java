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
                new ProductData("test", "preducat1", "l-33", BigDecimal.valueOf(123), 10),
                new ProductData("gg", "llll", "l-44", BigDecimal.valueOf(555), 15),
                new ProductData("Premium Widget", "High-quality widget for advanced users", "l-55", BigDecimal.valueOf(299.99), 5),
                new ProductData("Basic Tool", "Essential tool for everyday use", "l-66", BigDecimal.valueOf(49.99), 25)
        );
    }

    // Helper record to hold product initialization data
    private record ProductData(String name, String description, String skuCode, BigDecimal price, Integer quantity) {}
}
