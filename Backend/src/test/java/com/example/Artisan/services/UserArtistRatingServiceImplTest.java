package com.example.Artisan.services;

import com.example.Artisan.DTOs.UserArtistRatingDTO;
import com.example.Artisan.entities.Artist;
import com.example.Artisan.entities.User;
import com.example.Artisan.entities.UserArtistRating;
import com.example.Artisan.entities.UserArtistRatingAssociationId;
import com.example.Artisan.exceptions.RatingNotFoundException;
import com.example.Artisan.repositories.ArtistRepository;
import com.example.Artisan.repositories.UserArtistRatingRepository;
import com.example.Artisan.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserArtistRatingServiceImplTest {

    @InjectMocks
    UserArtistRatingServiceImpl userArtistRatingService;

    @Mock
    UserArtistRatingRepository userArtistRatingRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ArtistRepository artistRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveRating() {
        UserArtistRatingDTO dto = new UserArtistRatingDTO();
        dto.setRating(5);
        dto.setComment("Great artist!");

        User user = new User();
        user.setUserId(1L);

        Artist artist = new Artist();
        artist.setArtistId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));

        UserArtistRatingDTO result = userArtistRatingService.saveRating(dto, 1L, 1L);

        assertEquals(5, result.getRating());
        assertEquals("Great artist!", result.getComment());

        verify(userArtistRatingRepository, times(1)).save(any(UserArtistRating.class));
    }

    @Test
    public void testUpdateRating_NotFound() {
        UserArtistRatingDTO dto = new UserArtistRatingDTO();
        dto.setRating(5);
        dto.setComment("Great artist!");

        UserArtistRatingAssociationId id = new UserArtistRatingAssociationId(1L, 1L);

        when(userArtistRatingRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RatingNotFoundException.class, () -> userArtistRatingService.updateRating(1L, 1L, dto));
    }

    @Test
    void extractUserIdFromSecurityContext_NoUserInContext_ThrowsException() {
        Authentication auth = new UsernamePasswordAuthenticationToken("unknown@test.com", "password");
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(userRepository.findByEmail(anyString())).thenReturn(null);

        assertThrows(NullPointerException.class, () -> userArtistRatingService.extractUserIdFromSecurityContext());
    }

    @Test
    public void deleteRating_existingRating_ratingDeleted() {
        Long userId = 1L;
        Long artistId = 1L;
        UserArtistRatingAssociationId id = new UserArtistRatingAssociationId(userId, artistId);

        doNothing().when(userArtistRatingRepository).deleteById(any(UserArtistRatingAssociationId.class));

        userArtistRatingService.deleteRating(userId, artistId);

        verify(userArtistRatingRepository, times(1)).deleteById(any(UserArtistRatingAssociationId.class));
    }

    @Test
    void extractUserIdFromSecurityContext_UserInContext_ReturnsUserId() {
        User user = new User();
        user.setUserId(1L);
        user.setEmail("test@test.com");

        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), "password");
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        Long userId = userArtistRatingService.extractUserIdFromSecurityContext();

        assertEquals(user.getUserId(), userId);
    }

    @Test
    public void getRatingsByArtist_existingArtist_returnsRatingList() {
        Long artistId = 1L;
        List<UserArtistRating> userArtistRatings = new ArrayList<>();
        User user = new User();
        user.setFirstName("test");
        user.setLastName("user");
        UserArtistRating rating = new UserArtistRating();
        rating.setUser(user);
        rating.setRating(5);
        rating.setComment("Great artist!");
        userArtistRatings.add(rating);

        when(userArtistRatingRepository.findByArtist_ArtistId(artistId)).thenReturn(userArtistRatings);

        List<UserArtistRatingDTO> result = userArtistRatingService.getRatingsByArtist(artistId);

        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getRating());
        assertEquals("Great artist!", result.get(0).getComment());
    }

    @AfterEach
    void cleanup(){
        SecurityContextHolder.clearContext();
    }

}