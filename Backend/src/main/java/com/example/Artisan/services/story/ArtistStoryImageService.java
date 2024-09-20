package com.example.Artisan.services.story;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ArtistStoryImageService {
    /**
     * Uploads story images for a given story ID.
     * 
     * @param storyId the ID of the story
     * @param images  the array of images to upload
     * @return a list of URLs for the uploaded images
     * @throws IOException if an I/O error occurs during the upload process
     */
    List<String> uploadStoryImage(Long storyId, MultipartFile[] images) throws IOException;

    /**
     * Deletes all story images for a given story ID.
     * 
     * @param storyId the ID of the story
     */
    void deleteStoryImages(Long storyId);

    /**
     * Retrieves the story ID associated with a given artist ID.
     * 
     * @param artistId the ID of the artist
     * @return the ID of the story associated with the artist
     */
    Long getStoryIdByArtistId(Long artistId);
}
