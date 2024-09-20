/**
    * Adds or updates a story for the specified artist.
    *
    * @param artistId  the ID of the artist
    * @param storyDto  the story DTO containing the details of the story
    * @return the updated artist story DTO
    */
package com.example.Artisan.services.story;

import com.example.Artisan.DTOs.ArtistStoryDTO;

public interface ArtistStoryService {

    ArtistStoryDTO addOrUpdateStory(Long artistId, ArtistStoryDTO storyDto);


}
