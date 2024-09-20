package com.example.Artisan.services.user_profile;

import com.example.Artisan.DTOs.UserDTO;

/**
 * The UserProfileService interface provides methods to update and retrieve user profile details.
 */
public interface UserProfileService {

    /**
     * Updates the user profile with the given user data.
     *
     * @param user   the user data to update
     * @param userId the ID of the user
     * @return the updated user profile
     */
    UserDTO updateUser(UserDTO user, Long userId);

    /**
     * Retrieves the user details for the given user ID.
     *
     * @param userId the ID of the user
     * @return the user details
     */
    UserDTO getUserDetails(Long userId);

}
