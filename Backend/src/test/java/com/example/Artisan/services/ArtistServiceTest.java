package com.example.Artisan.services;

import com.example.Artisan.DTOs.ArtistDTO;
import com.example.Artisan.entities.Artist;
import com.example.Artisan.exceptions.ArtistNotFoundException;
import com.example.Artisan.repositories.ArtistRepository;
import com.example.Artisan.services.artist.ArtistServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;

class ArtistServiceTest {
    @Mock
    private ArtistRepository artistRepo;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ArtistServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getArtistById_existingId_returnsArtistDTO() {
        long artistId = 1L;
        Artist expectedArtist = createArtist(artistId);
        ArtistDTO mappedDTO = createArtistDTO(expectedArtist);

        Mockito.when(artistRepo.findById(artistId)).thenReturn(Optional.of(expectedArtist));
        Mockito.when(modelMapper.map(expectedArtist, ArtistDTO.class)).thenReturn(mappedDTO);

        ArtistDTO actualDTO = service.getArtistById(artistId);

        Assertions.assertEquals(mappedDTO.getEmail(), actualDTO.getEmail());
        Assertions.assertNull(actualDTO.getPassword());
    }

    @Test
    void getArtistById_nonexistentId_throwsException() {
        long artistId = 1L;
        Mockito.when(artistRepo.findById(artistId)).thenReturn(Optional.empty());
        Assertions.assertThrows(ArtistNotFoundException.class, () -> service.getArtistById(artistId));
    }

    @Test
    void getAllArtists_returnsAllArtistDTOs() {
        List<Artist> artists = createArtistList();
        List<ArtistDTO> expectedDTOs = new ArrayList<>();
        for (Artist artist : artists) {
            expectedDTOs.add(createArtistDTO(artist));
        }

        Mockito.when(artistRepo.findAll()).thenReturn(artists);
        Mockito.when(modelMapper.map(Mockito.any(Artist.class), eq(ArtistDTO.class))).thenAnswer(invocation -> {
            Artist artist = invocation.getArgument(0, Artist.class);
            return createArtistDTO(artist);
        });

        List<ArtistDTO> actualDTOs = service.getAllArtists();
        Assertions.assertNotNull(actualDTOs);
        Assertions.assertEquals(expectedDTOs.size(), actualDTOs.size());
        Assertions.assertNull(actualDTOs.get(0).getPassword());
    }

    @Test
    void getAllArtists_NoArtistPresent_returnsEmptyList() {
        Mockito.when(artistRepo.findAll()).thenReturn(List.of());

        List<ArtistDTO> actualDTOs = service.getAllArtists();
        Assertions.assertTrue(actualDTOs.isEmpty());
    }

    private Artist createArtist(long id) {
        Artist artist = new Artist();
        artist.setEmail("artist" + id + "@Example.com");
        artist.setPassword("pass" + id);
        return artist;
    }

    private ArtistDTO createArtistDTO(Artist artist) {
        ArtistDTO artistDTO = new ArtistDTO();
        artistDTO.setEmail(artist.getEmail());
        artistDTO.setPassword(artist.getPassword());
        return artistDTO;
    }

    private List<Artist> createArtistList() {
        List<Artist> artists = new ArrayList<>(3);
        for (long i = 1L; i <= 3L; i++) {
            artists.add(createArtist(i));
        }

        return artists;
    }
}
