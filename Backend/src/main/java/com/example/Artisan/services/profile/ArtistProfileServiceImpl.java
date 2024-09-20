package com.example.Artisan.services.profile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Base64;

import com.example.Artisan.DTOs.ArtistDTO;
import com.example.Artisan.DTOs.ArtistStoryDTO;
import com.example.Artisan.entities.Artist;
import com.example.Artisan.entities.ArtistStory;
import com.example.Artisan.entities.ArtistStoryImage;
import com.example.Artisan.exceptions.ResourceNotFoundException;
import com.example.Artisan.repositories.ArtistRepository;
import com.example.Artisan.repositories.ArtistStoryImageRepository;
import com.example.Artisan.services.profile.ArtistProfileService;
import com.example.Artisan.utils.DtoEntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArtistProfileServiceImpl implements ArtistProfileService {

    @Autowired
    private ArtistRepository artistRepo;

    @Autowired
    private ArtistStoryImageRepository artistStoryImageRepository;

    @Autowired
    private DtoEntityConverter dtoEntityConverter;


    @Override
    public ArtistDTO update(ArtistDTO artistDto, Long artistId) {
        Artist artist = artistRepo.findById(artistId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist", "Id", artistId));

        // Update artist entity with data from DTO
        if (artistDto.getAboutMe() != null) {
            artist.setAboutMe(artistDto.getAboutMe());
        }
        if (artistDto.getCity() != null) {
            artist.setCity(artistDto.getCity());
        }

        // Save the updated artist entity
        Artist updatedArtist = artistRepo.save(artist);
        return dtoEntityConverter.convertToDto(updatedArtist, ArtistDTO.class);
    }
    @Override
    public ArtistDTO getArtistDetails(Long artistId) {
        Artist artist = artistRepo.findById(artistId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist", "Id", artistId));

        ArtistDTO artistDto = dtoEntityConverter.convertToDto(artist, ArtistDTO.class);
        artistDto.setAboutMe(artist.getAboutMe());
        artistDto.setCity(artist.getCity());
        artistDto.setFirstName(artist.getFirstName());
        artistDto.setLastName(artist.getLastName());
        // Check if the artist has a profile image
        if (artist.getProfileImage() != null) {
            artistDto.setProfileImageUrl(artist.getProfileImage().getProfileImageUrl());

        } else {
            artistDto.setProfileImageUrl(null); // Set to null if there's no profile image
        }

        // Get the artist story
        ArtistStory artistStory = artist.getArtistStory();
        if (artistStory != null) {
            ArtistStoryDTO artistStoryDto = dtoEntityConverter.convertToDto(artistStory, ArtistStoryDTO.class);

            // Fetch the story images and add them to the story DTO
            var artistStories = artistStoryImageRepository.findByArtistStoryId(artistStory.getId()).stream();
            List<String> imageUrls =  artistStories.map(ArtistStoryImage::getImageUrl).toList();
            artistStoryDto.setStoryImageUrls(imageUrls);

            artistDto.setStory(artistStoryDto);
        }

        return artistDto;
    }

}
