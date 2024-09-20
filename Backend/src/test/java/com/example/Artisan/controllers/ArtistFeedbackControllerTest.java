package com.example.Artisan.controllers;

import com.example.Artisan.entities.ArtistFeedback;
import com.example.Artisan.services.ArtistFeedbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ArtistFeedbackControllerTest {

    @InjectMocks
    private ArtistFeedbackController artistFeedbackController;

    @Mock
    private ArtistFeedbackService artistFeedbackService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addFeedbackReturnsFeedbackWhenSuccessful() {
        ArtistFeedback feedback = new ArtistFeedback();
        feedback.setFeedback("Great artist!");

        when(artistFeedbackService.addFeedback(any(ArtistFeedback.class))).thenReturn(feedback);

        ResponseEntity<ArtistFeedback> response = artistFeedbackController.addFeedback(1L, feedback);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Great artist!", response.getBody().getFeedback());
    }

    @Test
    public void getAverageRatingForArtistReturnsAverageRating() {
        double averageRating = 4.5;

        when(artistFeedbackService.calculateAverageRatingForArtist(1L)).thenReturn(averageRating);

        ResponseEntity<Double> response = artistFeedbackController.getAverageRatingForArtist(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(averageRating, response.getBody());
    }

    @Test
    public void getFeedbackForArtistReturnsFeedbackList() {
        List<ArtistFeedback> feedbackList = Arrays.asList(new ArtistFeedback(), new ArtistFeedback());

        when(artistFeedbackService.findAllFeedbackForArtist(1L)).thenReturn(feedbackList);

        ResponseEntity<List<ArtistFeedback>> response = artistFeedbackController.getFeedbackForArtist(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }
}