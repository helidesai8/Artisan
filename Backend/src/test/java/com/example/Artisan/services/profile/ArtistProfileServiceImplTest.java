package com.example.Artisan.services.profile;

import com.example.Artisan.DTOs.ArtistDTO;
import com.example.Artisan.entities.Artist;
import com.example.Artisan.exceptions.ResourceNotFoundException;
import com.example.Artisan.repositories.ArtistRepository;
import com.example.Artisan.utils.DtoEntityConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ArtistProfileServiceImplTest {

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private DtoEntityConverter dtoEntityConverter;

    @InjectMocks
    private ArtistProfileServiceImpl artistProfileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void updateArtistDetails_ArtistNotFound() {
        Long artistId = 1L;
        ArtistDTO artistDto = new ArtistDTO();

        when(artistRepository.findById(artistId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> artistProfileService.update(artistDto, artistId));
    }

    @Test
    void getArtistDetails_Success() {
        Long artistId = 1L;
        Artist artist = new Artist();
        artist.setArtistId(artistId);

        ArtistDTO artistDto = new ArtistDTO();
        artistDto.setAboutMe("About me");
        artistDto.setCity("City");

        when(artistRepository.findById(artistId)).thenReturn(Optional.of(artist));
        when(dtoEntityConverter.convertToDto(artist, ArtistDTO.class)).thenReturn(artistDto);

        ArtistDTO fetchedArtistDto = artistProfileService.getArtistDetails(artistId);

        assertEquals(artistDto.getAboutMe(), fetchedArtistDto.getAboutMe());
        assertEquals(artistDto.getCity(), fetchedArtistDto.getCity());
    }

    @Test
    void getArtistDetails_ArtistNotFound() {
        Long artistId = 1L;

        when(artistRepository.findById(artistId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> artistProfileService.getArtistDetails(artistId));
    }
}
