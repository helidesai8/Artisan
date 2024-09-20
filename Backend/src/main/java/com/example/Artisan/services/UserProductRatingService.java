package com.example.Artisan.services;

import com.example.Artisan.DTOs.UserProductRatingDTO;

import java.util.List;

public interface UserProductRatingService {
    /**
     * Saves a user's product rating.
     *
     * @param userProductRatingDTO The user's product rating DTO.
     * @param userId              The ID of the user.
     * @param productId           The ID of the product.
     * @return The saved user's product rating DTO.
     */
    UserProductRatingDTO saveRating(UserProductRatingDTO userProductRatingDTO, Long userId, Long productId);

    /**
     * Updates a user's product rating.
     *
     * @param userId              The ID of the user.
     * @param productId           The ID of the product.
     * @param userProductRatingDTO The updated user's product rating DTO.
     * @return The updated user's product rating DTO.
     */
    UserProductRatingDTO updateRating(Long userId, Long productId, UserProductRatingDTO userProductRatingDTO);

    /**
     * Deletes a user's product rating.
     *
     * @param userId    The ID of the user.
     * @param productId The ID of the product.
     */
    void deleteRating(Long userId, Long productId);

    /**
     * Retrieves a list of user's product ratings for a specific product.
     *
     * @param productId The ID of the product.
     * @return A list of user's product rating DTOs.
     */
    List<UserProductRatingDTO> getRatingsByProduct(Long productId);
}
