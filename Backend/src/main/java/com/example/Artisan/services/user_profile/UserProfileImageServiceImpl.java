package com.example.Artisan.services.user_profile;

import java.io.IOException;
import java.util.Map;

import com.example.Artisan.entities.User;
import com.example.Artisan.entities.UserProfileImage;
import com.example.Artisan.exceptions.InvalidFileException;
import com.example.Artisan.exceptions.ResourceNotFoundException;
import com.example.Artisan.repositories.UserProfileImageRepository;
import com.example.Artisan.repositories.UserRepository;
import com.example.Artisan.services.CloudinaryService;
import com.example.Artisan.utils.ImageUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;

/**
 * Service implementation for managing user profile images.
 */
@Service
public class UserProfileImageServiceImpl implements UserProfileImageService {

    @Autowired
    private UserProfileImageRepository UserProfileImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    /**
     * Uploads a profile image for a user.
     *
     * @param userId the ID of the user
     * @param image  the image file to upload
     * @return the URL of the uploaded image
     * @throws IOException           if an I/O error occurs during the upload process
     * @throws InvalidFileException  if the file type is not valid (only JPG, JPEG, and PNG files are allowed)
     * @throws ResourceNotFoundException if the user with the given ID is not found
     */
    @Override
    @Transactional
    public String uploadProfileImage(Long userId, MultipartFile image) throws IOException {
        // Find the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        // Validate file type
        if (!ImageUtil.isValidImageType(image)) {
            throw new InvalidFileException("Invalid file type. Only JPG, JPEG, and PNG files are allowed.");
        }
        // Find existing profile image, if any
        UserProfileImage existingProfileImage = user.getUserProfileImage();

        // If an existing profile image exists, update it; otherwise, create a new one
        UserProfileImage profileImage = existingProfileImage != null ? existingProfileImage : new UserProfileImage();

        // Upload image to Cloudinary
        Map<String, String> uploadResult = cloudinaryService.uploadImage(image);
        String imageUrl = uploadResult.get("url");

        // Set artist and profile image URL
        profileImage.setUser(user);
        profileImage.setUserProfileImageUrl(imageUrl);

        // Save or update profile image entity
        user.setUserProfileImage(profileImage);
        userRepository.save(user);

        // Return the URL of the uploaded image
        return imageUrl;
    }

    /**
     * Deletes the profile image of a user.
     *
     * @param userId the ID of the user
     */
    @Override
    @Transactional
    public void deleteProfileImage(Long userId) {
        // Find the profile image entity by userId
        UserProfileImage profileImage = UserProfileImageRepository.findByUserUserId(userId);
        if (profileImage != null) {
            // Delete the profile image entity from the database
            UserProfileImageRepository.delete(profileImage);
            // Delete the associated image from Cloudinary (optional, if implemented in CloudinaryService)
            cloudinaryService.deleteImage(profileImage.getUserProfileImageUrl());
        }
    }
}


