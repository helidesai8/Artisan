package com.example.Artisan.controllers;

import com.example.Artisan.DTOs.UserDTO;
import com.example.Artisan.responses.ProfileImageApiResponse;
import com.example.Artisan.services.UserServiceImpl;
import com.example.Artisan.services.user_profile.UserProfileImageService;
import com.example.Artisan.services.user_profile.UserProfileService;
import com.example.Artisan.utils.TokenExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserProfileControllerTest {

    @InjectMocks
    private UserProfileController userProfileController;

    @Mock
    private UserProfileService userProfileService;

    @Mock
    private UserProfileImageService profileImageService;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private TokenExtractor tokenExtractor;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    public void setup() {
        tokenExtractor = mock(TokenExtractor.class);
        userService = mock(UserServiceImpl.class);
        userProfileService = mock(UserProfileService.class);
        profileImageService = mock(UserProfileImageService.class);
        userProfileController = new UserProfileController(tokenExtractor, userService,
                userProfileService, profileImageService);
    }

    @Test
    void updateUser_ReturnsUpdatedUser() {
        UserDTO userDto = new UserDTO();
        BindingResult result = mock(BindingResult.class);
        when(tokenExtractor.extractEmailFromToken()).thenReturn("test@test.com");
        when(userService.getUser("test@test.com")).thenReturn(userDto);
        when(userProfileService.updateUser(userDto, userDto.getUserId())).thenReturn(userDto);

        ResponseEntity<?> response = userProfileController.updateUser(userDto, result);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
    }

    @Test
    void getSingleUser_ReturnsUser() {
        UserDTO userDto = new UserDTO();
        when(tokenExtractor.extractEmailFromToken()).thenReturn("test@test.com");
        when(userService.getUser("test@test.com")).thenReturn(userDto);
        when(userProfileService.getUserDetails(userDto.getUserId())).thenReturn(userDto);

        ResponseEntity<UserDTO> response = userProfileController.getSingleUser();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
    }

    @Test
    void uploadProfileImage_ReturnsProfileImageApiResponse() throws IOException {
        MultipartFile image = new MockMultipartFile("image", "Hello, World!".getBytes());
        when(tokenExtractor.extractEmailFromToken()).thenReturn("test@test.com");
        when(userService.getUser("test@test.com")).thenReturn(new UserDTO());
        when(profileImageService.uploadProfileImage(anyLong(), eq(image))).thenReturn("imageUrl");

        ResponseEntity<ProfileImageApiResponse> response = userProfileController.uploadProfileImage(image);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Profile image uploaded successfully.", response.getBody().getMessage());
    }

    @Test
    void deleteProfileImage_ReturnsProfileImageApiResponse() {
        when(tokenExtractor.extractEmailFromToken()).thenReturn("test@test.com");
        when(userService.getUser("test@test.com")).thenReturn(new UserDTO());

        ResponseEntity<ProfileImageApiResponse> response = userProfileController.deleteProfileImage();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Profile image deleted successfully.", response.getBody().getMessage());
    }
}