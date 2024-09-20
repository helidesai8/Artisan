package com.example.Artisan.services.story;

import com.example.Artisan.DTOs.ArtistStoryDTO;
import com.example.Artisan.entities.Artist;
import com.example.Artisan.entities.ArtistStory;
import com.example.Artisan.repositories.ArtistRepository;
import com.example.Artisan.repositories.ArtistStoryRepository;
import com.example.Artisan.utils.DtoEntityConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ArtistStoryServiceImplTest {

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private ArtistStoryRepository artistStoryRepository;

    @Mock
    private DtoEntityConverter dtoEntityConverter;

    @InjectMocks
    private ArtistStoryServiceImpl artistStoryService;

    private final Long validArtistId = 1L;
    private final Artist validArtist = new Artist();
    private final ArtistStory validArtistStory = new ArtistStory();
    private final ArtistStoryDTO validArtistStoryDTO = new ArtistStoryDTO();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        validArtist.setArtistId(validArtistId);
        validArtistStory.setArtist(validArtist);
        validArtistStoryDTO.setStory("Test story");
    }

    @Test
    public void addOrUpdateStory_withNewStory_createsNewStory() {
        when(artistRepository.findById(validArtistId)).thenReturn(Optional.of(validArtist));
        when(artistStoryRepository.findByArtistArtistId(validArtistId)).thenReturn(null);
        when(dtoEntityConverter.convertToEntity(any(ArtistStoryDTO.class), eq(ArtistStory.class))).thenReturn(validArtistStory);
        when(artistStoryRepository.save(any(ArtistStory.class))).thenReturn(validArtistStory);
        when(dtoEntityConverter.convertToDto(any(ArtistStory.class), eq(ArtistStoryDTO.class))).thenReturn(validArtistStoryDTO);

        ArtistStoryDTO result = artistStoryService.addOrUpdateStory(validArtistId, validArtistStoryDTO);

        assertEquals(validArtistStoryDTO.getStory(), result.getStory());
        verify(artistRepository, times(1)).findById(validArtistId);
        verify(artistStoryRepository, times(1)).findByArtistArtistId(validArtistId);
        verify(dtoEntityConverter, times(1)).convertToEntity(any(ArtistStoryDTO.class), eq(ArtistStory.class));
        verify(artistStoryRepository, times(1)).save(any(ArtistStory.class));
        verify(dtoEntityConverter, times(1)).convertToDto(any(ArtistStory.class), eq(ArtistStoryDTO.class));
    }

    @Test
    public void addOrUpdateStory_withExistingStory_updatesStory() {
        when(artistRepository.findById(validArtistId)).thenReturn(Optional.of(validArtist));
        when(artistStoryRepository.findByArtistArtistId(validArtistId)).thenReturn(validArtistStory);
        when(artistStoryRepository.save(any(ArtistStory.class))).thenReturn(validArtistStory);
        when(dtoEntityConverter.convertToDto(any(ArtistStory.class), eq(ArtistStoryDTO.class))).thenReturn(validArtistStoryDTO);

        ArtistStoryDTO result = artistStoryService.addOrUpdateStory(validArtistId, validArtistStoryDTO);

        assertEquals(validArtistStoryDTO.getStory(), result.getStory());
        verify(artistRepository, times(1)).findById(validArtistId);
        verify(artistStoryRepository, times(1)).findByArtistArtistId(validArtistId);
        verify(artistStoryRepository, times(1)).save(any(ArtistStory.class));
        verify(dtoEntityConverter, times(1)).convertToDto(any(ArtistStory.class), eq(ArtistStoryDTO.class));
    }
}