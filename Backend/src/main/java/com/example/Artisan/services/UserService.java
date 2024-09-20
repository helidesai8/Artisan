package com.example.Artisan.services;

import com.example.Artisan.DTOs.UserDTO;
import com.example.Artisan.entities.User;
import com.example.Artisan.payloads.LoginCredentials;
import com.example.Artisan.responses.AuthenticationResponse;

public interface UserService {
    /**
     * Registers a new user.
     *
     * @param userDTO The user data transfer object containing the user information.
     * @return An authentication response containing the user's authentication token.
     */
    AuthenticationResponse registerUser(UserDTO userDTO);

    /**
     * Logs in a user with the provided login credentials.
     *
     * @param loginCredentials The login credentials of the user.
     * @return An authentication response containing the user's authentication token.
     */
    AuthenticationResponse loginUser(LoginCredentials loginCredentials);

    /**
     * Retrieves a user's data transfer object by email.
     *
     * @param email The email of the user.
     * @return The user's data transfer object.
     */
    UserDTO getUser(String email);

    /**
     * Sends a password reset email to the user with the provided email.
     *
     * @param email The email of the user.
     * @return A string indicating the success or failure of the password reset process.
     */
    String forgotPassword(String email);

    /**
     * Retrieves a user entity by email.
     *
     * @param email The email of the user.
     * @return The user entity.
     */
    User getUserEntityByEmail(String email);
}
