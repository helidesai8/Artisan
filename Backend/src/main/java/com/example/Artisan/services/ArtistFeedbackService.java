package com.example.Artisan.services;

import com.example.Artisan.entities.ArtistFeedback;
import com.example.Artisan.repositories.ArtistFeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class represents the service layer for managing artist feedback.
 * It provides methods to add feedback, retrieve all feedback for an artist,
 * and calculate the average rating for an artist.
 */
@Service
public class ArtistFeedbackService {

    private final ArtistFeedbackRepository artistFeedbackRepository;

    @Autowired
    public ArtistFeedbackService(ArtistFeedbackRepository artistFeedbackRepository) {
        this.artistFeedbackRepository = artistFeedbackRepository;
    }

    /**
     * Adds the given feedback to the artist feedback repository.
     *
     * @param feedback The feedback to be added.
     * @return The added feedback.
     */
    public ArtistFeedback addFeedback(ArtistFeedback feedback) {
        return artistFeedbackRepository.save(feedback);
    }

    /**
     * Retrieves all feedback for the artist with the given artist ID.
     *
     * @param artistId The ID of the artist.
     * @return A list of artist feedback.
     */
    public List<ArtistFeedback> findAllFeedbackForArtist(Long artistId) {
        return artistFeedbackRepository.findByArtist_ArtistId(artistId);
    }

    /**
     * Calculates the average rating for the artist with the given artist ID.
     *
     * @param artistId The ID of the artist.
     * @return The average rating.
     */
    public double calculateAverageRatingForArtist(Long artistId) {
        List<ArtistFeedback> feedbacks = artistFeedbackRepository.findByArtist_ArtistId(artistId);
        return feedbacks.stream().mapToDouble(ArtistFeedback::getRating).average().orElse(0.0);
    }
}