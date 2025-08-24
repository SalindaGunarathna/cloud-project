package com.project.microservices.product.migration;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.project.microservices.product.model.Product;
import com.project.microservices.product.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@ChangeLog(order = "001")
@Slf4j
public class initial {

    @ChangeSet(order = "001", id = "init-products", author = "developer")
    public void initProducts(ProductRepository productRepository) {
        log.info("Starting initial product data migration...");

        if (productRepository.count() == 0) {
            List<Product> products = Arrays.asList(
                    Product.builder()
                            .name("iPhone 15")
                            .description("Latest Apple iPhone with advanced features")
                            .skuCode("iphone_15")
                            .price(new BigDecimal("999.99"))
                            .build(),

                    Product.builder()
                            .name("Google Pixel 8")
                            .description("Google's flagship smartphone with pure Android experience")
                            .skuCode("pixel_8")
                            .price(new BigDecimal("699.99"))
                            .build(),

                    Product.builder()
                            .name("Samsung Galaxy S24")
                            .description("Premium Samsung smartphone with cutting-edge technology")
                            .skuCode("galaxy_24")
                            .price(new BigDecimal("899.99"))
                            .build(),

                    Product.builder()
                            .name("OnePlus 12")
                            .description("High-performance smartphone with fast charging")
                            .skuCode("oneplus_12")
                            .price(new BigDecimal("799.99"))
                            .build()
            );

            productRepository.saveAll(products);
            log.info("Successfully inserted {} products", products.size());
        } else {
            log.info("Products already exist. Skipping initial data migration.");
        }
    }


}