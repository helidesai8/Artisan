package com.example.Artisan.repositories.products;

import com.example.Artisan.entities.products.ProductStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductStoryRepository extends JpaRepository<ProductStory, Long> {
}
