package com.example.Artisan.services;

import com.example.Artisan.entities.User;
import com.example.Artisan.entities.UserProfileImage;
import com.example.Artisan.exceptions.ResourceNotFoundException;
import com.example.Artisan.repositories.UserProfileImageRepository;
import com.example.Artisan.repositories.UserRepository;
import com.example.Artisan.services.user_profile.UserProfileImageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserProfileImageServiceImplTest {

    @Mock
    private UserProfileImageRepository userProfileImageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private UserProfileImageServiceImpl userProfileImageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void uploadProfileImage_InvalidFileType_ExceptionThrown() throws IOException {
        // Arrange
        long userId = 1;
        byte[] imageBytes = {}; // Invalid file type
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", imageBytes);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userProfileImageService.uploadProfileImage(userId, multipartFile);
        });
    }

    @Test
    void uploadProfileImage_UserNotFound_ExceptionThrown_DifferentTest() throws IOException {
        // Arrange
        long userId = 1;
        byte[] imageBytes = {}; // Valid image bytes
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", imageBytes);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userProfileImageService.uploadProfileImage(userId, multipartFile);
        });
    }

    @Test
    void uploadProfileImage_ValidImage_Success() throws IOException {
        // Mock data
        Long userId = 1L;
        User user = new User();
        user.setUserId(userId);

        // Provide a valid JPG, JPEG, or PNG image file
        MultipartFile image = new MockMultipartFile("test.jpg", "test.jpg", "image/jpeg", "image data".getBytes());

        Map<String, String> uploadResult = new HashMap<>();
        uploadResult.put("url", "https://example.com/image.jpg");

        // Stubbing repository methods
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cloudinaryService.uploadImage(image)).thenReturn(uploadResult);

        // Method call
        String imageUrl = userProfileImageService.uploadProfileImage(userId, image);

        // Verification
        assertNotNull(imageUrl);
        assertTrue(imageUrl.startsWith("https://example.com"));
        verify(userRepository, times(1)).findById(userId);
        verify(cloudinaryService, times(1)).uploadImage(image);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void uploadProfileImage_UserNotFound_ExceptionThrown() throws IOException {
        // Mock data
        Long userId = 1L;
        MultipartFile image = new MockMultipartFile("image.jpg", new byte[0]);

        // Stubbing repository methods
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Method call and verification
        assertThrows(ResourceNotFoundException.class, () -> userProfileImageService.uploadProfileImage(userId, image));
        verify(cloudinaryService, never()).uploadImage(image);
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteProfileImage_ProfileImageExists_Success() {
        // Mock data
        Long userId = 1L;
        UserProfileImage profileImage = new UserProfileImage();
        profileImage.setId(1L);

        // Stubbing repository methods
        when(userProfileImageRepository.findByUserUserId(userId)).thenReturn(profileImage);

        // Method call
        userProfileImageService.deleteProfileImage(userId);

        // Verification
        verify(userProfileImageRepository, times(1)).delete(profileImage);
        verify(cloudinaryService, times(1)).deleteImage(profileImage.getUserProfileImageUrl());
    }

    @Test
    void deleteProfileImage_ProfileImageNotExists_NoAction() {
        // Mock data
        Long userId = 1L;

        // Stubbing repository methods
        when(userProfileImageRepository.findByUserUserId(userId)).thenReturn(null);

        // Method call
        userProfileImageService.deleteProfileImage(userId);

        // Verification
        verify(userProfileImageRepository, never()).delete(any());
        verify(cloudinaryService, never()).deleteImage(anyString());
    }
}
