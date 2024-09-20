package com.example.Artisan.repositories;

import com.example.Artisan.entities.UserArtistRating;
import com.example.Artisan.entities.UserArtistRatingAssociationId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserArtistRatingRepository extends JpaRepository<UserArtistRating, UserArtistRatingAssociationId> {
    List<UserArtistRating> findByArtist_ArtistId(Long artistId);
}