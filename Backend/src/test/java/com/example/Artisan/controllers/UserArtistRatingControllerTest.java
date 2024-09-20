package com.example.Artisan.controllers;

import com.example.Artisan.DTOs.UserArtistRatingDTO;
import com.example.Artisan.entities.User;
import com.example.Artisan.entities.UserArtistRating;
import com.example.Artisan.entities.UserArtistRatingAssociationId;
import com.example.Artisan.repositories.UserArtistRatingRepository;
import com.example.Artisan.repositories.UserRepository;
import com.example.Artisan.services.UserArtistRatingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserArtistRatingControllerTest {

    @Mock
    private UserArtistRatingService userArtistRatingService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserArtistRatingRepository userArtistRatingRepository;

    @InjectMocks
    private UserArtistRatingController userArtistRatingController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockSecurityContext();
    }

    private void mockSecurityContext() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("user@example.com", "password");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }


    @Test
    void updateRating() {
        Long artistId = 1L;
        User user = new User();
        user.setUserId(1L);

        when(userRepository.findByEmail(anyString())).thenReturn(user);

        UserArtistRatingDTO ratingDTO = new UserArtistRatingDTO();
        when(userArtistRatingService.updateRating(eq(user.getUserId()), eq(artistId), any(UserArtistRatingDTO.class))).thenReturn(ratingDTO);

        ResponseEntity<UserArtistRatingDTO> response = userArtistRatingController.updateRating(artistId, ratingDTO);

        assertEquals(200, response.getStatusCode().value());
        verify(userArtistRatingService).updateRating(user.getUserId(), artistId, ratingDTO);
    }

    @Test
    void deleteRating() {
        Long artistId = 1L;
        User user = new User();
        user.setUserId(1L);

        when(userRepository.findByEmail(anyString())).thenReturn(user);

        ResponseEntity<Void> response = userArtistRatingController.deleteRating(artistId);

        assertEquals(200, response.getStatusCodeValue());
        verify(userArtistRatingService).deleteRating(user.getUserId(), artistId);
    }

    @Test
    public void getRatingsByArtist_noRatings_returnsEmptyList() {
        Long artistId = 1L;

        when(userArtistRatingRepository.findByArtist_ArtistId(artistId)).thenReturn(new ArrayList<>());

        List<UserArtistRatingDTO> result = userArtistRatingService.getRatingsByArtist(artistId);

        assertTrue(result.isEmpty());
    }


    @AfterEach
    void cleanUp(){
        SecurityContextHolder.clearContext();
    }

}
