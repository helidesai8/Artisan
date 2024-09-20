package com.example.Artisan.services;

import com.example.Artisan.DTOs.UserDTO;
import com.example.Artisan.entities.User;
import com.example.Artisan.exceptions.ResourceNotFoundException;
import com.example.Artisan.repositories.UserRepository;
import com.example.Artisan.services.user_profile.UserProfileServiceImpl;
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

class UserProfileServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DtoEntityConverter dtoEntityConverter;

    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateUser_ValidUserDTO_ReturnsUpdatedUserDTO() {
        // Arrange
        long userId = 1;
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setEmail("john@example.com");
        userDTO.setContactNumber("1234567890");
        userDTO.setStreetAddress("123 Main St");
        userDTO.setCity("City");
        userDTO.setState("State");
        userDTO.setPostalCode("12345");
        userDTO.setCountry("Country");

        User existingUser = new User();
        existingUser.setUserId(userId);
        existingUser.setFirstName("Old");
        existingUser.setLastName("User");
        existingUser.setEmail("old@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        when(dtoEntityConverter.convertToDto(existingUser, UserDTO.class)).thenReturn(userDTO);

        // Act
        UserDTO updatedUserDTO = userProfileService.updateUser(userDTO, userId);

        // Assert
        assertEquals(userDTO.getFirstName(), updatedUserDTO.getFirstName());
        assertEquals(userDTO.getLastName(), updatedUserDTO.getLastName());
        assertEquals(userDTO.getEmail(), updatedUserDTO.getEmail());
        assertEquals(userDTO.getContactNumber(), updatedUserDTO.getContactNumber());
        assertEquals(userDTO.getStreetAddress(), updatedUserDTO.getStreetAddress());
        assertEquals(userDTO.getCity(), updatedUserDTO.getCity());
        assertEquals(userDTO.getState(), updatedUserDTO.getState());
        assertEquals(userDTO.getPostalCode(), updatedUserDTO.getPostalCode());
        assertEquals(userDTO.getCountry(), updatedUserDTO.getCountry());
    }

    @Test
    void getUserDetails_ValidUserId_ReturnsUserDTO() {
        // Arrange
        long userId = 1;
        User user = new User();
        user.setUserId(userId);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setContactNumber("1234567890");
        user.setStreetAddress("123 Main St");
        user.setCity("City");
        user.setState("State");
        user.setPostalCode("12345");
        user.setCountry("Country");

        UserDTO expectedUserDTO = new UserDTO();
        expectedUserDTO.setFirstName("John");
        expectedUserDTO.setLastName("Doe");
        expectedUserDTO.setEmail("john@example.com");
        expectedUserDTO.setContactNumber("1234567890");
        expectedUserDTO.setStreetAddress("123 Main St");
        expectedUserDTO.setCity("City");
        expectedUserDTO.setState("State");
        expectedUserDTO.setPostalCode("12345");
        expectedUserDTO.setCountry("Country");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(dtoEntityConverter.convertToDto(user, UserDTO.class)).thenReturn(expectedUserDTO);

        // Act
        UserDTO actualUserDTO = userProfileService.getUserDetails(userId);

        // Assert
        assertEquals(expectedUserDTO, actualUserDTO);
    }

    @Test
    void updateUser_UserNotFound_ThrowsResourceNotFoundException() {
        // Arrange
        long userId = 1;
        UserDTO userDTO = new UserDTO();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userProfileService.updateUser(userDTO, userId);
        });
    }

    @Test
    void getUserDetails_UserNotFound_ThrowsResourceNotFoundException() {
        // Arrange
        long userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userProfileService.getUserDetails(userId);
        });
    }
}

