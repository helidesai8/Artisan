package com.example.Artisan.services.profile;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

/**
 * The ArtistProfileImageService interface provides methods for managing artist profile images.
 */
public interface ArtistProfileImageService {

    /**
     * Uploads a profile image for the specified artist.
     *
     * @param artistId the ID of the artist
     * @param image the profile image to upload
     * @return the URL of the uploaded image
     * @throws IOException if an I/O error occurs during the upload process
     */
    String uploadProfileImage(Long artistId, MultipartFile image) throws IOException;

    /**
     * Deletes the profile image of the specified artist.
     *
     * @param artistId the ID of the artist
     */
    void deleteProfileImage(Long artistId);

}
