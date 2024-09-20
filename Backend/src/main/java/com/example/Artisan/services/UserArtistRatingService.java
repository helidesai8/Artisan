package com.example.Artisan.services;

import com.example.Artisan.DTOs.UserArtistRatingDTO;

import java.util.List;

public interface UserArtistRatingService {
    /**
     * Saves a user's rating for an artist.
     *
     * @param userArtistRatingDTO The rating DTO containing the rating details.
     * @param userId              The ID of the user.
     * @param artistId            The ID of the artist.
     * @return The saved user's rating DTO.
     */
    UserArtistRatingDTO saveRating(UserArtistRatingDTO userArtistRatingDTO, Long userId, Long artistId);

    /**
     * Updates a user's rating for an artist.
     *
     * @param userId              The ID of the user.
     * @param artistId            The ID of the artist.
     * @param userArtistRatingDTO The updated rating DTO containing the new rating details.
     * @return The updated user's rating DTO.
     */
    UserArtistRatingDTO updateRating(Long userId, Long artistId, UserArtistRatingDTO userArtistRatingDTO);

    /**
     * Deletes a user's rating for an artist.
     *
     * @param userId   The ID of the user.
     * @param artistId The ID of the artist.
     */
    void deleteRating(Long userId, Long artistId);

    /**
     * Retrieves all ratings for a specific artist.
     *
     * @param artistId The ID of the artist.
     * @return A list of user's rating DTOs for the artist.
     */
    List<UserArtistRatingDTO> getRatingsByArtist(Long artistId);

    /**
     * Extracts the user ID from the security context.
     *
     * @return The user ID extracted from the security context.
     */
    Long extractUserIdFromSecurityContext();
}
