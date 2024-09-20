package com.example.Artisan.services.profile;

import java.io.IOException;
import java.util.Map;

import com.example.Artisan.entities.Artist;
import com.example.Artisan.entities.ArtistProfileImage;
import com.example.Artisan.exceptions.InvalidFileException;
import com.example.Artisan.exceptions.ResourceNotFoundException;
import com.example.Artisan.repositories.ArtistProfileImageRepository;
import com.example.Artisan.repositories.ArtistRepository;
import com.example.Artisan.services.CloudinaryService;
import com.example.Artisan.utils.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;

/**
 * Service implementation for managing artist profile images.
 */
@Service
public class ArtistProfileImageServiceImpl implements ArtistProfileImageService {

    @Autowired
    private ArtistProfileImageRepository artistProfileImageRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    /**
     * Uploads a profile image for the specified artist.
     *
     * @param artistId the ID of the artist
     * @param image    the profile image to upload
     * @return the URL of the uploaded image
     * @throws IOException           if an I/O error occurs during the upload process
     * @throws InvalidFileException  if the file type is invalid
     * @throws ResourceNotFoundException if the artist is not found
     */
    @Override
    @Transactional
    public String uploadProfileImage(Long artistId, MultipartFile image) throws IOException {
        // Validate file type
        if (!ImageUtil.isValidImageType(image)) {
            throw new InvalidFileException("Invalid file type. Only JPG, JPEG, and PNG files are allowed.");
        }

        // Find the artist
        var artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist", "Id", artistId));

        // Find existing profile image, if any
        ArtistProfileImage existingProfileImage = artist.getProfileImage();

        // If an existing profile image exists, update it; otherwise, create a new one
        ArtistProfileImage profileImage = existingProfileImage != null ? existingProfileImage : new ArtistProfileImage();

        // Upload image to Cloudinary
        Map<String, String> uploadResult = cloudinaryService.uploadImage(image);
        String imageUrl = uploadResult.get("url");

        // Set artist and profile image URL
        profileImage.setArtist(artist);
        profileImage.setProfileImageUrl(imageUrl);

        // Save or update profile image entity
        artist.setProfileImage(profileImage);
        artistRepository.save(artist);

        // Return the URL of the uploaded image
        return imageUrl;
    }

    /**
     * Deletes the profile image for the specified artist.
     *
     * @param artistId the ID of the artist
     */
    @Override
    @Transactional
    public void deleteProfileImage(Long artistId) {
        // Find the profile image entity by artistId
        ArtistProfileImage profileImage = artistProfileImageRepository.findByArtistArtistId(artistId);
        if (profileImage != null) {
            // Delete the profile image entity from the database
            artistProfileImageRepository.delete(profileImage);
            // Delete the associated image from Cloudinary (optional, if implemented in CloudinaryService)
            cloudinaryService.deleteImage(profileImage.getProfileImageUrl());
        }
    }
}

