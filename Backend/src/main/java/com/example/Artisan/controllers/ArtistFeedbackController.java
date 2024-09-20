package com.example.Artisan.controllers;

import com.example.Artisan.entities.Artist;
import com.example.Artisan.entities.ArtistFeedback;
import com.example.Artisan.repositories.ArtistRepository;
import com.example.Artisan.services.ArtistFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;

/**
 * This is a Rest Controller for managing Artist Feedback related requests.
 * It is mapped to the "/api/artists" URL path.
 */
@RestController
@RequestMapping("/api/artists")
public class ArtistFeedbackController {

    private final ArtistFeedbackService artistFeedbackService;
    private final ArtistRepository artistRepository;

    /**
     * Method to get the current authenticated user's ID.
     *
     * @return The ID of the current authenticated user.
     */
    public Long getCurrentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Artist artist = artistRepository.findByEmail(email);
        return artist.getArtistId();
    }

    /**
     * Autowired constructor for dependency injection of ArtistFeedbackService and ArtistRepository.
     *
     * @param artistFeedbackService The service to handle artist feedback related operations.
     * @param artistRepository The repository to handle artist related operations.
     */
    @Autowired
    public ArtistFeedbackController(ArtistFeedbackService artistFeedbackService, ArtistRepository artistRepository, ArtistRepository artistRepository1) {
        this.artistFeedbackService = artistFeedbackService;
        this.artistRepository = artistRepository1;
    }

    /**
     * Endpoint to add feedback for a specific artist.
     * It is mapped to the "/{artistId}/feedback" URL path.
     *
     * @param artistId The ID of the artist to add feedback for.
     * @param feedback The feedback to be added.
     * @return ResponseEntity containing the saved ArtistFeedback.
     */
    @PostMapping("/{artistId}/feedback")
    public ResponseEntity<ArtistFeedback> addFeedback(@PathVariable Long artistId, @RequestBody ArtistFeedback feedback) {
        ArtistFeedback savedFeedback = artistFeedbackService.addFeedback(feedback);
        return ResponseEntity.ok(savedFeedback);
    }

    /**
     * Endpoint to get the average rating for a specific artist.
     * It is mapped to the "/{artistId}/rating" URL path.
     *
     * @param artistId The ID of the artist to get the average rating for.
     * @return ResponseEntity containing the average rating for the specified artist.
     */
    @GetMapping("/{artistId}/rating")
    public ResponseEntity<Double> getAverageRatingForArtist(@PathVariable Long artistId) {
        double averageRating = artistFeedbackService.calculateAverageRatingForArtist(artistId);
        return ResponseEntity.ok(averageRating);
    }

    /**
     * Endpoint to get all feedback for a specific artist.
     * It is mapped to the "/{artistId}/feedback" URL path.
     *
     * @param artistId The ID of the artist to get the feedback for.
     * @return ResponseEntity containing a list of all ArtistFeedbacks for the specified artist.
     */
    @GetMapping("/{artistId}/feedback")
    public ResponseEntity<List<ArtistFeedback>> getFeedbackForArtist(@PathVariable Long artistId) {
        List<ArtistFeedback> feedback = artistFeedbackService.findAllFeedbackForArtist(artistId);
        return ResponseEntity.ok(feedback);
    }
}