package com.example.Artisan.services.user_profile;

import com.example.Artisan.DTOs.UserDTO;
import com.example.Artisan.entities.User;
import com.example.Artisan.exceptions.ResourceNotFoundException;
import com.example.Artisan.repositories.UserRepository;
import com.example.Artisan.services.user_profile.UserProfileService;
import com.example.Artisan.utils.DtoEntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class implements the UserProfileService interface and provides the implementation for managing user profiles.
 * It contains methods to update and retrieve user details.
 */
@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserRepository userRepo;


    @Autowired
    private DtoEntityConverter dtoEntityConverter;


    @Override
    public UserDTO updateUser(UserDTO userDto, Long userId) {
        User existingUser = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "Id", userId));

        // Update user entity with data from DTO
        existingUser.setContactNumber(userDto.getContactNumber());
        existingUser.setStreetAddress(userDto.getStreetAddress());
        existingUser.setCity(userDto.getCity());
        existingUser.setState(userDto.getState());
        existingUser.setPostalCode(userDto.getPostalCode());
        existingUser.setCountry(userDto.getCountry());

        // Save the updated user entity
        User updateduser = userRepo.save(existingUser);
        return dtoEntityConverter.convertToDto(updateduser, UserDTO.class);
    }

    @Override
    public UserDTO getUserDetails(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "Id", userId));

        UserDTO userDto = dtoEntityConverter.convertToDto(user, UserDTO.class);
        userDto.setCountry(user.getCountry());
        userDto.setCity(user.getCity());
        userDto.setContactNumber(user.getContactNumber());
        userDto.setStreetAddress(user.getStreetAddress());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setState(user.getState());
        userDto.setPostalCode(user.getPostalCode());

        // Check if the user has a profile image
        if (user.getUserProfileImage() != null) {
            userDto.setProfileImageUrl(user.getUserProfileImage().getUserProfileImageUrl());

        } else {
            userDto.setProfileImageUrl(null); // Set to null if there's no profile image
        }



        return userDto;
    }

}
