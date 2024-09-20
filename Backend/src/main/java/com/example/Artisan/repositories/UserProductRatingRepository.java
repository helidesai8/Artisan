package com.example.Artisan.repositories;

import com.example.Artisan.entities.UserProductRating;
import com.example.Artisan.entities.UserProductRatingAssociationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProductRatingRepository extends JpaRepository<UserProductRating, UserProductRatingAssociationId> {
    List<UserProductRating> findByProductId(Long productId);
}
