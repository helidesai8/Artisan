package com.example.Artisan.services.user_profile;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface UserProfileImageService {

    /**
     * Uploads a profile image for the specified user.
     * 
     * @param userId the ID of the user
     * @param image the image file to be uploaded
     * @return the URL of the uploaded image
     * @throws IOException if an I/O error occurs during the upload process
     */
    String uploadProfileImage(Long userId, MultipartFile image) throws IOException;

    /**
     * Deletes the profile image of the specified user.
     * 
     * @param userId the ID of the user
     */
    void deleteProfileImage(Long userId);

}
