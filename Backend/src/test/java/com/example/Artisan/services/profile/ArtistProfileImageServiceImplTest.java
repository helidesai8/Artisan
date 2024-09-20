package com.example.Artisan.services.profile;

import com.example.Artisan.entities.Artist;
import com.example.Artisan.entities.ArtistProfileImage;
import com.example.Artisan.exceptions.InvalidFileException;
import com.example.Artisan.repositories.ArtistProfileImageRepository;
import com.example.Artisan.repositories.ArtistRepository;
import com.example.Artisan.services.CloudinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ArtistProfileImageServiceImplTest {

    @Mock
    private ArtistProfileImageRepository artistProfileImageRepository;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private ArtistProfileImageServiceImpl artistProfileImageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void uploadProfileImage_Success() throws IOException {
        Long artistId = 1L;
        MultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "image".getBytes());
        Artist artist = new Artist();
        artist.setArtistId(artistId);

        when(artistRepository.findById(artistId)).thenReturn(Optional.of(artist));
        when(cloudinaryService.uploadImage(image)).thenReturn(Map.of("url", "http://image-url.com"));

        String imageUrl = artistProfileImageService.uploadProfileImage(artistId, image);

        assertEquals("http://image-url.com", imageUrl);
        verify(artistRepository, times(1)).save(artist);
    }

    @Test
    void uploadProfileImage_InvalidFileType() {
        Long artistId = 1L;
        MultipartFile image = new MockMultipartFile("image", "image.txt", "text/plain", "image".getBytes());

        assertThrows(InvalidFileException.class, () -> artistProfileImageService.uploadProfileImage(artistId, image));
    }

    @Test
    void deleteProfileImage_Success() {
        Long artistId = 1L;
        ArtistProfileImage profileImage = new ArtistProfileImage();
        profileImage.setProfileImageUrl("http://image-url.com");

        when(artistProfileImageRepository.findByArtistArtistId(artistId)).thenReturn(profileImage);

        artistProfileImageService.deleteProfileImage(artistId);

        verify(artistProfileImageRepository, times(1)).delete(profileImage);
        verify(cloudinaryService, times(1)).deleteImage(profileImage.getProfileImageUrl());
    }

    @Test
    void deleteProfileImage_NoImageToDelete() {
        Long artistId = 1L;

        when(artistProfileImageRepository.findByArtistArtistId(artistId)).thenReturn(null);

        artistProfileImageService.deleteProfileImage(artistId);

        verify(artistProfileImageRepository, times(0)).delete(any());
        verify(cloudinaryService, times(0)).deleteImage(any());
    }
}