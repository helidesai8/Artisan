package com.example.Artisan.services;

import com.example.Artisan.entities.Artist;
import com.example.Artisan.entities.ArtistFeedback;
import com.example.Artisan.entities.User;
import com.example.Artisan.repositories.ArtistFeedbackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ArtistFeedbackServiceTest {

    @InjectMocks
    private ArtistFeedbackService artistFeedbackService;

    @Mock
    private ArtistFeedbackRepository artistFeedbackRepository;

    private User user;
    private Artist artist;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        artist = new Artist();
        artist.setArtistId(1L);
        artist.setFirstName("First Name");
        artist.setLastName("Last Name");

        user = new User();
        user.setUserId(1L);
        user.setEmail("user@example.com");

        ArtistFeedback feedback = new ArtistFeedback();
        feedback.setId(1L);
        feedback.setArtist(artist);
        feedback.setUser(user);
        feedback.setFeedback("Great artwork!");

        when(artistFeedbackRepository.findById(1L)).thenReturn(Optional.of(feedback));
        when(artistFeedbackRepository.findByArtist_ArtistId(1L)).thenReturn(List.of(feedback));
    }

    @Test
    void testAddFeedback() {
        ArtistFeedback newFeedback = new ArtistFeedback();
        newFeedback.setArtist(artist);
        newFeedback.setUser(user);
        newFeedback.setFeedback("Incredible detail and colors!");

        when(artistFeedbackRepository.save(any(ArtistFeedback.class))).thenReturn(newFeedback);

        ArtistFeedback savedFeedback = artistFeedbackService.addFeedback(newFeedback);

        verify(artistFeedbackRepository, times(1)).save(any(ArtistFeedback.class));
        assertEquals("Incredible detail and colors!", savedFeedback.getFeedback());
    }

    @Test
    void testGetFeedbackForArtist() {
        List<ArtistFeedback> feedbackList = artistFeedbackService.findAllFeedbackForArtist(1L);

        assertEquals(1, feedbackList.size());
        assertEquals("Great artwork!", feedbackList.get(0).getFeedback());
    }

    @Test
    public void findAllFeedbackForArtist_ShouldReturnFeedbackForArtist() {
        // Arrange
        Long artistId = 1L;
        Artist artist = new Artist();
        artist.setArtistId(artistId);
        User user = new User();

        ArtistFeedback feedback1 = new ArtistFeedback();
        feedback1.setUser(user);
        feedback1.setArtist(artist);
        feedback1.setFeedback("Feedback 1");

        ArtistFeedback feedback2 = new ArtistFeedback();
        feedback2.setUser(user);
        feedback2.setArtist(artist);
        feedback2.setFeedback("Feedback 2");

        List<ArtistFeedback> expectedFeedback = List.of(feedback1, feedback2);

        Mockito.when(artistFeedbackRepository.findByArtist_ArtistId(artistId))
                .thenReturn(expectedFeedback);

        List<ArtistFeedback> actualFeedback = artistFeedbackService.findAllFeedbackForArtist(artistId);

        assertEquals(expectedFeedback, actualFeedback);
    }

}