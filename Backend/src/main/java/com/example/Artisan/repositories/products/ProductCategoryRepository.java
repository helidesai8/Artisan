package com.example.Artisan.repositories.products;

import com.example.Artisan.entities.products.ProductCategory;
import com.example.Artisan.entities.products.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    ProductCategory findByName(String name);
}
