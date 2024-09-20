package com.example.Artisan.services;

import com.example.Artisan.DTOs.UserProductRatingDTO;
import com.example.Artisan.entities.products.Product;
import com.example.Artisan.entities.User;
import com.example.Artisan.entities.UserProductRating;
import com.example.Artisan.entities.UserProductRatingAssociationId;
import com.example.Artisan.exceptions.RatingNotFoundException;
import com.example.Artisan.repositories.products.ProductRepository;
import com.example.Artisan.repositories.UserProductRatingRepository;
import com.example.Artisan.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserProductRatingServiceImpl implements UserProductRatingService {

    @Autowired
    private UserProductRatingRepository userProductRatingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;


    @Override
    @Transactional
    public UserProductRatingDTO saveRating(UserProductRatingDTO userProductRatingDTO, Long userId, Long productId){
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        UserProductRating userProductRating = new UserProductRating();
        userProductRating.setId(new UserProductRatingAssociationId(user.getUserId(), product.getId()));
        userProductRating.setUser(user);
        userProductRating.setProduct(product);
        userProductRating.setRating(userProductRatingDTO.getRating());
        userProductRating.setComment(userProductRatingDTO.getComment());

        userProductRatingRepository.save(userProductRating);
        return userProductRatingDTO;
    }

    private Long extractUserIdFromSecurityContext() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);
        return user.getUserId();
    }


    @Override
    @Transactional
    public UserProductRatingDTO updateRating(Long userId, Long productId, UserProductRatingDTO userProductRatingDTO) {
        UserProductRatingAssociationId id = new UserProductRatingAssociationId(userId, productId);
        var userProductRating = userProductRatingRepository.findById(id)
                .orElseThrow(() -> new RatingNotFoundException(productId,userId));

        userProductRating.setRating(userProductRatingDTO.getRating());
        userProductRating.setComment(userProductRatingDTO.getComment());

        userProductRatingRepository.save(userProductRating);
        return userProductRatingDTO;
    }

    @Override
    @Transactional
    public void deleteRating(Long userId, Long productId) {
        UserProductRatingAssociationId id = new UserProductRatingAssociationId(userId, productId);
        userProductRatingRepository.deleteById(id);
    }

    @Override
    public List<UserProductRatingDTO> getRatingsByProduct(Long productId) {
        List<UserProductRating> userProductRatings = userProductRatingRepository.findByProductId(productId);
        List<UserProductRatingDTO> ratingDTOs = new ArrayList<>();

        for (UserProductRating rating : userProductRatings) {
            UserProductRatingDTO dto = new UserProductRatingDTO();
            dto.setRating(rating.getRating());
            dto.setComment(rating.getComment());
            dto.setFirstName(rating.getUser().getFirstName());
            dto.setLastName(rating.getUser().getLastName());
            ratingDTOs.add(dto);
        }

        return ratingDTOs;
    }

}
