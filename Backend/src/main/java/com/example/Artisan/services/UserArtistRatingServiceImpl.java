package com.example.Artisan.services;

import com.example.Artisan.DTOs.UserArtistRatingDTO;
import com.example.Artisan.entities.User;
import com.example.Artisan.entities.UserArtistRating;
import com.example.Artisan.entities.UserArtistRatingAssociationId;
import com.example.Artisan.entities.Artist;
import com.example.Artisan.exceptions.RatingNotFoundException;
import com.example.Artisan.repositories.UserArtistRatingRepository;
import com.example.Artisan.repositories.UserRepository;
import com.example.Artisan.repositories.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the UserArtistRatingService interface and provides the implementation
 * for managing user ratings for artists.
 */
@Service
public class UserArtistRatingServiceImpl implements UserArtistRatingService {

    @Autowired
    private UserArtistRatingRepository userArtistRatingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;


    @Override
    @Transactional
    public UserArtistRatingDTO saveRating(UserArtistRatingDTO userArtistRatingDTO, Long userId, Long artistId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artist not found"));

        UserArtistRating userArtistRating = new UserArtistRating();
        userArtistRating.setId(new UserArtistRatingAssociationId(user.getUserId(), artist.getArtistId()));
        userArtistRating.setUser(user);
        userArtistRating.setArtist(artist);
        userArtistRating.setRating(userArtistRatingDTO.getRating());
        userArtistRating.setComment(userArtistRatingDTO.getComment());

        userArtistRatingRepository.save(userArtistRating);
        return userArtistRatingDTO;
    }

    public Long extractUserIdFromSecurityContext() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);
        return user.getUserId();
    }


    @Override
    @Transactional
    public UserArtistRatingDTO updateRating(Long userId, Long artistId, UserArtistRatingDTO userArtistRatingDTO) {
        UserArtistRatingAssociationId id = new UserArtistRatingAssociationId(userId, artistId);
        UserArtistRating userArtistRating = userArtistRatingRepository.findById(id)
                .orElseThrow(() -> new RatingNotFoundException(artistId,userId));

        userArtistRating.setRating(userArtistRatingDTO.getRating());
        userArtistRating.setComment(userArtistRatingDTO.getComment());

        userArtistRatingRepository.save(userArtistRating);
        return userArtistRatingDTO;
    }

    @Override
    @Transactional
    public void deleteRating(Long userId, Long artistId) {
        UserArtistRatingAssociationId id = new UserArtistRatingAssociationId(userId, artistId);
        userArtistRatingRepository.deleteById(id);
    }

    @Override
    public List<UserArtistRatingDTO> getRatingsByArtist(Long artistId) {
        List<UserArtistRating> userArtistRatings = userArtistRatingRepository.findByArtist_ArtistId(artistId);
        List<UserArtistRatingDTO> ratingDTOs = new ArrayList<>();

        for (UserArtistRating rating : userArtistRatings) {
            UserArtistRatingDTO dto = new UserArtistRatingDTO();
            dto.setRating(rating.getRating());
            dto.setComment(rating.getComment());
            dto.setFirstName(rating.getUser().getFirstName());
            dto.setLastName(rating.getUser().getLastName());
            ratingDTOs.add(dto);
        }

        return ratingDTOs;
    }

}
