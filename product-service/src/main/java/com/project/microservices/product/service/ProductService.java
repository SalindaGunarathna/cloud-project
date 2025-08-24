package com.project.microservices.product.service;

import com.project.microservices.product.dto.ProductRequest;
import com.project.microservices.product.dto.ProductResponse;
import com.project.microservices.product.client.InventoryClient;
import com.project.microservices.product.model.Product;
import com.project.microservices.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final InventoryClient inventoryClient;


    public ProductResponse createAndAddProduct(ProductRequest productRequest) {

        Optional<Product> existingProduct = productRepository.findBySkuCode(productRequest.skuCode());

        if (existingProduct.isPresent()) {
            log.info("Product with skuCode={} already exists, updating inventory only", productRequest.skuCode());
            inventoryClient.addProduct(productRequest.skuCode(), productRequest.quantity());
            Product product = existingProduct.get(); // Extract product for cleaner code
            return new ProductResponse(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getSkuCode(),
                    product.getPrice()
            );
        }

        // If not exists, create new product
        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .skuCode(productRequest.skuCode())
                .price(productRequest.price())
                .build();

        Product savedProduct = productRepository.save(product); // Store saved entity
        log.info("New product created successfully with skuCode={}", savedProduct.getSkuCode());

        // Initialize inventory for new product
        inventoryClient.addProduct(savedProduct.getSkuCode(), productRequest.quantity());

        return new ProductResponse(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getDescription(),
                savedProduct.getSkuCode(),
                savedProduct.getPrice()
        );
    }
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> new ProductResponse(product.getId(), product.getName(), product.getDescription(),
                        product.getSkuCode(),
                        product.getPrice()))
                .toList();
    }
}
