package com.example.Artisan.repositories.products;

import com.example.Artisan.entities.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByArtist_ArtistId(Long artistId);

    @Query("SELECT avg(upr.rating) FROM Product p JOIN p.userProductRatings upr WHERE p.category.id = :categoryId")
    float getAverageRatingForCategory(@Param("categoryId") Long categoryId);

}
