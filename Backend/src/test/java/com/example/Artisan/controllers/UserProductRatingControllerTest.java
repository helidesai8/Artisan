package com.example.Artisan.controllers;

import com.example.Artisan.DTOs.UserProductRatingDTO;
import com.example.Artisan.entities.User;
import com.example.Artisan.repositories.UserRepository;
import com.example.Artisan.controllers.UserProductRatingController;
import com.example.Artisan.services.UserProductRatingService;
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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserProductRatingControllerTest {

    @Mock
    private UserProductRatingService userProductRatingService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserProductRatingController userProductRatingController;

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
    void addRating() {
        Long productId = 1L;
        User user = new User();
        user.setUserId(1L);

        when(userRepository.findByEmail(anyString())).thenReturn(user);

        UserProductRatingDTO ratingDTO = new UserProductRatingDTO();
        when(userProductRatingService.saveRating(any(UserProductRatingDTO.class), eq(user.getUserId()), eq(productId))).thenReturn(ratingDTO);

        ResponseEntity<UserProductRatingDTO> response = userProductRatingController.addRating(productId, ratingDTO);

        assertEquals(200, response.getStatusCodeValue());
        verify(userProductRatingService).saveRating(ratingDTO, user.getUserId(), productId);
    }

    @Test
    void updateRating() {
        Long productId = 1L;
        User user = new User();
        user.setUserId(1L);

        when(userRepository.findByEmail(anyString())).thenReturn(user);

        UserProductRatingDTO ratingDTO = new UserProductRatingDTO();
        when(userProductRatingService.updateRating(eq(user.getUserId()), eq(productId), any(UserProductRatingDTO.class))).thenReturn(ratingDTO);

        ResponseEntity<UserProductRatingDTO> response = userProductRatingController.updateRating(productId, ratingDTO);

        assertEquals(200, response.getStatusCodeValue());
        verify(userProductRatingService).updateRating(user.getUserId(), productId, ratingDTO);
    }

    @Test
    void deleteRating() {
        Long productId = 1L;
        User user = new User();
        user.setUserId(1L);

        when(userRepository.findByEmail(anyString())).thenReturn(user);

        ResponseEntity<Void> response = userProductRatingController.deleteRating(productId);

        assertEquals(200, response.getStatusCodeValue());
        verify(userProductRatingService).deleteRating(user.getUserId(), productId);
    }

    @Test
    void getRatingsByProduct_returnsListOfRatings() {
        Long productId = 1L;
        User user = new User();
        user.setUserId(1L);
        user.setFirstName("Test");
        user.setLastName("User");

        UserProductRatingDTO ratingDTO = new UserProductRatingDTO();
        ratingDTO.setFirstName(user.getFirstName());
        ratingDTO.setLastName(user.getLastName());

        List<UserProductRatingDTO> ratingDTOList = Arrays.asList(ratingDTO);

        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(userProductRatingService.getRatingsByProduct(productId)).thenReturn(ratingDTOList);

        ResponseEntity<List<UserProductRatingDTO>> response = userProductRatingController.getRatingsByProduct(productId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(ratingDTOList, response.getBody());
    }

    @Test
    void getRatingsByProduct_whenNoRatings_returnsEmptyList() {
        Long productId = 1L;
        User user = new User();
        user.setUserId(1L);

        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(userProductRatingService.getRatingsByProduct(productId)).thenReturn(new ArrayList<>());

        ResponseEntity<List<UserProductRatingDTO>> response = userProductRatingController.getRatingsByProduct(productId);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isEmpty());
    }

}
