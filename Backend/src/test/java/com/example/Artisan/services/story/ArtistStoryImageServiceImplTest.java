package com.example.Artisan.services.story;

import com.example.Artisan.entities.ArtistStory;
import com.example.Artisan.entities.ArtistStoryImage;
import com.example.Artisan.exceptions.InvalidFileException;
import com.example.Artisan.exceptions.ResourceNotFoundException;
import com.example.Artisan.repositories.ArtistStoryImageRepository;
import com.example.Artisan.repositories.ArtistStoryRepository;
import com.example.Artisan.services.CloudinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtistStoryImageServiceImplTest {

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private ArtistStoryImageRepository artistStoryImageRepository;

    @Mock
    private ArtistStoryRepository artistStoryRepository;

    @InjectMocks
    private ArtistStoryImageServiceImpl artistStoryImageService;

    private final Long validStoryId = 1L;
    private final Long invalidStoryId = 99L;
    private final String validImageUrl = "http://cloudinary.com/image.jpg";
    private final ArtistStory validArtistStory = new ArtistStory();

    @BeforeEach
    void setUp() {
        validArtistStory.setId(validStoryId);
        lenient().when(artistStoryRepository.findById(validStoryId)).thenReturn(Optional.of(validArtistStory));
        lenient().when(artistStoryRepository.findById(invalidStoryId)).thenReturn(Optional.empty());
    }

    @Test
    void uploadStoryImage_withValidData_uploadsSuccessfully() throws IOException {
        MultipartFile[] images = {new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[]{1, 2, 3, 4})};
        when(cloudinaryService.uploadImage(any(MultipartFile.class))).thenReturn(Map.of("url", validImageUrl));

        var urls = artistStoryImageService.uploadStoryImage(validStoryId, images);

        assertFalse(urls.isEmpty());
        assertEquals(validImageUrl, urls.get(0));
        verify(artistStoryImageRepository, times(1)).save(any(ArtistStoryImage.class));
    }


    @Test
    void uploadStoryImage_withInvalidFileType_throwsInvalidFileException() {
        MultipartFile[] images = {new MockMultipartFile("image", "image.txt", "text/plain", new byte[]{1, 2, 3, 4})};

        assertThrows(InvalidFileException.class, () -> artistStoryImageService.uploadStoryImage(validStoryId, images));
    }

    @Test
    void uploadStoryImage_withNonExistentStory_throwsResourceNotFoundException() {
        MultipartFile[] images = {new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[]{1, 2, 3, 4})};

        assertThrows(ResourceNotFoundException.class, () -> artistStoryImageService.uploadStoryImage(invalidStoryId, images));
    }

    @Test
    void deleteStoryImages_deletesImagesSuccessfully() {
        ArtistStoryImage storyImage = new ArtistStoryImage();
        storyImage.setImageUrl(validImageUrl);
        when(artistStoryImageRepository.findByArtistStoryId(validStoryId)).thenReturn(Collections.singletonList(storyImage));

        artistStoryImageService.deleteStoryImages(validStoryId);

        verify(cloudinaryService, times(1)).deleteImage(validImageUrl);
        verify(artistStoryImageRepository, times(1)).delete(storyImage);
    }

    @Test
    void getStoryIdByArtistId_withExistingArtist_returnsStoryId() {
        Long artistId = 1L;
        when(artistStoryRepository.findByArtistArtistId(artistId)).thenReturn(validArtistStory);

        Long resultStoryId = artistStoryImageService.getStoryIdByArtistId(artistId);

        assertEquals(validStoryId, resultStoryId);
    }

    @Test
    void getStoryIdByArtistId_withNonExistingArtist_throwsResourceNotFoundException() {
        Long artistId = 2L;
        when(artistStoryRepository.findByArtistArtistId(artistId)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> artistStoryImageService.getStoryIdByArtistId(artistId));
    }
}

