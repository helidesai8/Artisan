package com.example.Artisan.controllers;

import com.example.Artisan.DTOs.UserProductRatingDTO;
import com.example.Artisan.entities.User;
import com.example.Artisan.repositories.UserRepository;
import com.example.Artisan.services.UserProductRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing user product ratings.
 */
@RestController
@RequestMapping("/api/v1/ratings")
public class UserProductRatingController {

    @Autowired
    private UserProductRatingService userProductRatingService;
    @Autowired
    private UserRepository userRepository;

    /**
     * Adds a rating for a product.
     *
     * @param productId           The ID of the product.
     * @param userProductRatingDTO The rating details.
     * @return The saved rating.
     */
    @PostMapping("/{productId}")
    public ResponseEntity<UserProductRatingDTO> addRating(@PathVariable Long productId,
                                                          @RequestBody UserProductRatingDTO userProductRatingDTO) {
        Long userId = extractUserIdFromSecurityContext();
        UserProductRatingDTO savedRating = userProductRatingService.saveRating(userProductRatingDTO, userId, productId);
        return ResponseEntity.ok(savedRating);
    }

    /**
     * Updates a rating for a product.
     *
     * @param productId           The ID of the product.
     * @param userProductRatingDTO The updated rating details.
     * @return The updated rating.
     */
    @PutMapping("/{productId}")
    public ResponseEntity<UserProductRatingDTO> updateRating(@PathVariable Long productId,
                                                             @RequestBody UserProductRatingDTO userProductRatingDTO) {
        Long userId = extractUserIdFromSecurityContext();
        UserProductRatingDTO updatedRating = userProductRatingService.updateRating(userId, productId, userProductRatingDTO);
        return ResponseEntity.ok(updatedRating);
    }

    /**
     * Deletes a rating for a product.
     *
     * @param productId The ID of the product.
     * @return A response indicating the success of the deletion.
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long productId) {
        Long userId = extractUserIdFromSecurityContext();
        userProductRatingService.deleteRating(userId, productId);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves all ratings for a product.
     *
     * @param productId The ID of the product.
     * @return A list of user product ratings.
     */
    @GetMapping("/{productId}")
    public ResponseEntity<List<UserProductRatingDTO>> getRatingsByProduct(@PathVariable Long productId) {
        List<UserProductRatingDTO> userProductRatingDTOs = userProductRatingService.getRatingsByProduct(productId);
        return ResponseEntity.ok(userProductRatingDTOs);
    }

    /**
     * Extracts the user ID from the security context.
     *
     * @return The user ID.
     */
    private Long extractUserIdFromSecurityContext() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);
        return user.getUserId();
    }

}
