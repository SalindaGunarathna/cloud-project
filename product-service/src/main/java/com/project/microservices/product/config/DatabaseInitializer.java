package com.project.microservices.product.config;


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

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            log.info("Initializing product database with sample data...");

            List<Product> products = List.of(
                    Product.builder()
                            .name("Test Product 1")
                            .description("preducat1")
                            .skuCode("l-33")
                            .price(BigDecimal.valueOf(123))
                            .build(),

                    Product.builder()
                            .name("Sample Product 2")
                            .description("llll")
                            .skuCode("l-44")
                            .price(BigDecimal.valueOf(555))
                            .build()
            );

            productRepository.saveAll(products);
            log.info("Database initialized with {} products", products.size());
        } else {
            log.info("Database already contains {} products", productRepository.count());
        }
    }
}
