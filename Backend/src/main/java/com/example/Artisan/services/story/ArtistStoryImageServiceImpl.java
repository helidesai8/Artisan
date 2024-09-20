package com.example.Artisan.services.story;

import com.example.Artisan.entities.ArtistStory;
import com.example.Artisan.entities.ArtistStoryImage;
import com.example.Artisan.exceptions.InvalidFileException;
import com.example.Artisan.exceptions.ResourceNotFoundException;
import com.example.Artisan.repositories.ArtistStoryImageRepository;
import com.example.Artisan.repositories.ArtistStoryRepository;
import com.example.Artisan.services.CloudinaryService;
import com.example.Artisan.utils.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service implementation for managing artist story images.
 */
@Service
public class ArtistStoryImageServiceImpl implements ArtistStoryImageService {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ArtistStoryImageRepository artistStoryImageRepository;

    @Autowired
    private ArtistStoryRepository artistStoryRepository;

    /**
     * Uploads story images for a given story ID.
     *
     * @param storyId the ID of the artist story
     * @param images  the array of multipart files representing the images to be uploaded
     * @return a list of URLs of the uploaded images
     * @throws IOException           if an I/O error occurs during the file upload
     * @throws InvalidFileException  if the file type is not valid (only JPG, JPEG, and PNG files are allowed)
     * @throws ResourceNotFoundException if the artist story with the given ID is not found
     */
    @Override
    @Transactional
    public List<String> uploadStoryImage(Long storyId, MultipartFile[] images) throws IOException {
        // Validate file type for each image
        for (MultipartFile image : images) {
            if (!ImageUtil.isValidImageType(image)) {
                throw new InvalidFileException("Invalid file type. Only JPG, JPEG, and PNG files are allowed.");
            }
        }

        // Delete existing images for the story
        deleteStoryImages(storyId);

        // Upload new images
        List<String> uploadedFileUrls = new ArrayList<>();
        for (MultipartFile image : images) {
            Map<String, String> uploadResult = cloudinaryService.uploadImage(image);
            String imageUrl = uploadResult.get("url");

            ArtistStoryImage artistStoryImage = new ArtistStoryImage();
            var story = artistStoryRepository.findById(storyId)
                    .orElseThrow(() -> new ResourceNotFoundException("Artist Story", "Id", storyId));
            artistStoryImage.setArtistStory(story);
            artistStoryImage.setImageUrl(imageUrl);
            artistStoryImageRepository.save(artistStoryImage);

            uploadedFileUrls.add(imageUrl);
        }

        return uploadedFileUrls;
    }

    /**
     * Deletes all story images for a given story ID.
     *
     * @param storyId the ID of the artist story
     */
    @Override
    @Transactional
    public void deleteStoryImages(Long storyId) {
        List<ArtistStoryImage> storyImages = artistStoryImageRepository.findByArtistStoryId(storyId);
        for (ArtistStoryImage storyImage : storyImages) {
            cloudinaryService.deleteImage(storyImage.getImageUrl());
            artistStoryImageRepository.delete(storyImage);
        }
    }

    /**
     * Gets the story ID for a given artist ID.
     *
     * @param artistId the ID of the artist
     * @return the ID of the artist story
     * @throws ResourceNotFoundException if the artist story with the given artist ID is not found
     */
    public Long getStoryIdByArtistId(Long artistId) {
        ArtistStory artistStory = artistStoryRepository.findByArtistArtistId(artistId);
        if (artistStory != null) {
            return artistStory.getId();
        }
        throw new ResourceNotFoundException("Artist Story", "Artist ID", artistId);
    }
}

