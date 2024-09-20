package com.example.Artisan.services;

import com.example.Artisan.DTOs.UserProductRatingDTO;
import com.example.Artisan.entities.User;
import com.example.Artisan.entities.UserProductRating;
import com.example.Artisan.entities.UserProductRatingAssociationId;
import com.example.Artisan.entities.products.Product;
import com.example.Artisan.repositories.UserProductRatingRepository;
import com.example.Artisan.repositories.UserRepository;
import com.example.Artisan.repositories.products.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserProductRatingTest {

    @InjectMocks
    private UserProductRatingServiceImpl userProductRatingService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserProductRatingRepository userProductRatingRepository;

    @Test
    public void saveRating_withValidUserAndProduct_savesRating() {
        UserProductRatingDTO userProductRatingDTO = new UserProductRatingDTO();
        userProductRatingDTO.setRating(5);
        userProductRatingDTO.setComment("Great product!");

        User user = new User();
        Product product = new Product();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        userProductRatingService.saveRating(userProductRatingDTO, 1L, 1L);

        ArgumentCaptor<UserProductRating> captor = ArgumentCaptor.forClass(UserProductRating.class);
        verify(userProductRatingRepository, times(1)).save(captor.capture());
        UserProductRating savedRating = captor.getValue();

        assertEquals(userProductRatingDTO.getRating(), savedRating.getRating());
        assertEquals(userProductRatingDTO.getComment(), savedRating.getComment());
    }

    @Test
    public void updateRating_withValidUserAndProduct_updatesRating() {
        UserProductRatingDTO userProductRatingDTO = new UserProductRatingDTO();
        userProductRatingDTO.setRating(4);
        userProductRatingDTO.setComment("Good product!");

        UserProductRatingAssociationId id = new UserProductRatingAssociationId(1L, 1L);
        UserProductRating userProductRating = new UserProductRating();
        userProductRating.setId(id);

        when(userProductRatingRepository.findById(any(UserProductRatingAssociationId.class))).thenReturn(Optional.of(userProductRating));

        userProductRatingService.updateRating(1L, 1L, userProductRatingDTO);

        ArgumentCaptor<UserProductRating> captor = ArgumentCaptor.forClass(UserProductRating.class);
        verify(userProductRatingRepository, times(1)).save(captor.capture());
        UserProductRating updatedRating = captor.getValue();

        assertEquals(userProductRatingDTO.getRating(), updatedRating.getRating());
        assertEquals(userProductRatingDTO.getComment(), updatedRating.getComment());
    }

    @Test
    public void deleteRating_withValidUserAndProduct_deletesRating() {
        UserProductRatingAssociationId id = new UserProductRatingAssociationId(1L, 1L);

        userProductRatingService.deleteRating(1L, 1L);

        verify(userProductRatingRepository, times(1)).deleteById(any(UserProductRatingAssociationId.class));
    }
}