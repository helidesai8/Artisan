/**
 * The UserProfileController class handles the API endpoints related to user profiles.
 * It provides methods for updating user profile, getting single user details, uploading and deleting profile images.
 */
package com.example.Artisan.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.example.Artisan.DTOs.UserDTO;
import com.example.Artisan.exceptions.InvalidFileException;
import com.example.Artisan.exceptions.ResourceNotFoundException;
import com.example.Artisan.responses.ProfileImageApiResponse;
import com.example.Artisan.services.UserServiceImpl;
import com.example.Artisan.services.user_profile.UserProfileImageService;
import com.example.Artisan.services.user_profile.UserProfileService;
import com.example.Artisan.utils.TokenExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("api/v1/user/profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserProfileImageService profileImageService;

    @Autowired
    private UserServiceImpl userService;

    private final TokenExtractor tokenExtractor;

    @Autowired
    public UserProfileController(TokenExtractor tokenExtractor, UserServiceImpl userService,
                                 UserProfileService userProfileService, UserProfileImageService profileImageService) {
        this.tokenExtractor = tokenExtractor;
        this.userService = userService;
        this.userProfileService = userProfileService;
        this.profileImageService = profileImageService;

    }

    /**
     * Updates the user profile.
     *
     * @param userDto The UserDTO object containing the updated user profile information.
     * @param result  The BindingResult object for validation errors.
     * @return A ResponseEntity containing the updated UserDTO object if successful, or a validation error response if there are errors.
     */
    @PutMapping(" ")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserDTO userDto, BindingResult result) {
        // Extract the logged-in user's email from the security context
        String email = tokenExtractor.extractEmailFromToken();

        // Use UserProfileService to get the user based on email
        UserDTO existingUser = this.userService.getUser(email);
        if (existingUser == null) {
            throw new ResourceNotFoundException("User", "Email", email);
        }

        // Check for validation errors
        if (result.hasErrors()) {
            // Construct a validation error response
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(new ProfileImageApiResponse("Validation errors", false));
        }

        // Use UserProfileService to update the user
        UserDTO updatedUser = this.userProfileService.updateUser(userDto, existingUser.getUserId());
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Gets the details of a single user.
     *
     * @return A ResponseEntity containing the UserDTO object of the requested user if found.
     */
    @GetMapping(" ")
    public ResponseEntity<UserDTO> getSingleUser() {
        // Extract the logged-in user's email from the security context
        String email = tokenExtractor.extractEmailFromToken();

        Long userId = userService.getUser(email).getUserId();

        // Use ArtistService to get the artist based on email
        UserDTO existingUser = this.userProfileService.getUserDetails(userId);

        if (existingUser == null) {
            throw new ResourceNotFoundException("User", "Email", email);
        }

        return ResponseEntity.ok(existingUser);
    }

    /**
     * Uploads or updates the profile image of the user.
     *
     * @param image The MultipartFile object representing the image file to be uploaded.
     * @return A ResponseEntity containing the ProfileImageApiResponse object with the upload status and image URL.
     */
    @PostMapping("/profileImage")
    public ResponseEntity<ProfileImageApiResponse> uploadProfileImage(@RequestParam("image") MultipartFile image) {
        try {
            // Extract the logged-in user's email from the security context
            String email = tokenExtractor.extractEmailFromToken();

            // Use UserService to get the user based on email
            UserDTO existingUser = this.userService.getUser(email);

            // Upload profile image
            String imageUrl = profileImageService.uploadProfileImage(existingUser.getUserId(), image);

            // Return base64-encoded image string in the response
            return ResponseEntity.ok(new ProfileImageApiResponse("Profile image uploaded successfully.", true, imageUrl));
        } catch (IOException e) {
            var errorResponse = new ProfileImageApiResponse("Failed to upload profile image.", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Deletes the profile image of the user.
     *
     * @return A ResponseEntity containing the ProfileImageApiResponse object with the delete status.
     */
    @DeleteMapping("/profileImage")
    public ResponseEntity<ProfileImageApiResponse> deleteProfileImage() {
        try {
            // Extract the logged-in user's email from the security context
            String email = tokenExtractor.extractEmailFromToken();

            // Use UserService to get the user based on email
            UserDTO existingUser = this.userService.getUser(email);
            profileImageService.deleteProfileImage(existingUser.getUserId());
            return ResponseEntity.ok(new ProfileImageApiResponse("Profile image deleted successfully.", true));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ProfileImageApiResponse(e.getMessage(), false));
        } catch (InvalidFileException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ProfileImageApiResponse(e.getMessage(), false));
        }
    }
}
