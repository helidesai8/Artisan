package com.example.Artisan.services.story;

import com.example.Artisan.DTOs.ArtistStoryDTO;
import com.example.Artisan.entities.Artist;
import com.example.Artisan.entities.ArtistStory;
import com.example.Artisan.exceptions.ResourceNotFoundException;
import com.example.Artisan.repositories.ArtistRepository;
import com.example.Artisan.repositories.ArtistStoryRepository;
import com.example.Artisan.services.story.ArtistStoryService;
import com.example.Artisan.utils.DtoEntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class ArtistStoryServiceImpl implements ArtistStoryService {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtistStoryRepository artistStoryRepository;


    @Autowired
    private DtoEntityConverter dtoEntityConverter;

    @Override
    @Transactional
    public ArtistStoryDTO addOrUpdateStory(Long artistId, ArtistStoryDTO storyDto) {
        // Find the artist
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist", "Id", artistId));

        // Check if a story already exists for the artist
        ArtistStory existingStory = artistStoryRepository.findByArtistArtistId(artistId);
        if (existingStory != null) {
            // Update existing story
            existingStory.setStory(storyDto.getStory());
            return dtoEntityConverter.convertToDto(artistStoryRepository.save(existingStory), ArtistStoryDTO.class);
        } else {
            // Create new story
            ArtistStory newStory = dtoEntityConverter.convertToEntity(storyDto, ArtistStory.class);
            newStory.setArtist(artist);
            return dtoEntityConverter.convertToDto(artistStoryRepository.save(newStory), ArtistStoryDTO.class);
        }
    }

}

