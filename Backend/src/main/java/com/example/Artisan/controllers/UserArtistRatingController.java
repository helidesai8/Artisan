package com.example.Artisan.controllers;

import com.example.Artisan.DTOs.UserArtistRatingDTO;
import com.example.Artisan.entities.User;
import com.example.Artisan.repositories.UserRepository;
import com.example.Artisan.services.UserArtistRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing user artist ratings.
 */
@RestController
@RequestMapping("/api/v1/ratings/artists")
public class UserArtistRatingController {

    @Autowired
    private UserArtistRatingService userArtistRatingService;
    @Autowired
    private UserRepository userRepository;

    /**
     * Adds a rating for an artist.
     *
     * @param artistId             The ID of the artist.
     * @param userArtistRatingDTO  The rating DTO containing the rating details.
     * @return                     The saved rating DTO with additional user information.
     */
    @PostMapping("/{artistId}")
    public ResponseEntity<UserArtistRatingDTO> addRating(@PathVariable Long artistId,
                                                          @RequestBody UserArtistRatingDTO userArtistRatingDTO) {
        Long userId = extractUserIdFromSecurityContext();
        UserArtistRatingDTO savedRating = userArtistRatingService.saveRating(userArtistRatingDTO,userId,artistId);
        User user = userRepository.findById(userId).orElse(null);
        savedRating.setFirstName(user.getFirstName());
        savedRating.setLastName(user.getLastName());
        return ResponseEntity.ok(savedRating);
    }

    /**
     * Extracts the user ID from the security context.
     *
     * @return The user ID, or null if not found.
     */
    private Long extractUserIdFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email);
            return user.getUserId();
        }
        return null;
    }

    /**
     * Updates a rating for an artist.
     *
     * @param artistId             The ID of the artist.
     * @param userArtistRatingDTO  The updated rating DTO containing the new rating details.
     * @return                     The updated rating DTO.
     */
    @PutMapping("/{artistId}")
    public ResponseEntity<UserArtistRatingDTO> updateRating(@PathVariable Long artistId,
                                                             @RequestBody UserArtistRatingDTO userArtistRatingDTO) {
        Long userId = extractUserIdFromSecurityContext();
        UserArtistRatingDTO updatedRating = userArtistRatingService.updateRating(userId, artistId, userArtistRatingDTO);
        return ResponseEntity.ok(updatedRating);
    }

    /**
     * Deletes a rating for an artist.
     *
     * @param artistId  The ID of the artist.
     * @return          ResponseEntity indicating the success of the deletion.
     */
    @DeleteMapping("/{artistId}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long artistId) {
        Long userId = extractUserIdFromSecurityContext();
        userArtistRatingService.deleteRating(userId, artistId);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves all ratings for a specific artist.
     *
     * @param artistId  The ID of the artist.
     * @return          ResponseEntity containing a list of user artist rating DTOs.
     */
    @GetMapping("/{artistId}")
    public ResponseEntity<List<UserArtistRatingDTO>> getRatingsByArtist(@PathVariable Long artistId) {
        List<UserArtistRatingDTO> userArtistRatingDTOs = userArtistRatingService.getRatingsByArtist(artistId);
        return ResponseEntity.ok(userArtistRatingDTOs);
    }

}

