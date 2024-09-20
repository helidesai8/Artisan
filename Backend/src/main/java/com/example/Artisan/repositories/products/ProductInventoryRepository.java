package com.example.Artisan.repositories.products;

import com.example.Artisan.entities.products.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Long> {
    public ProductInventory findByProductId(Long productId);
}
