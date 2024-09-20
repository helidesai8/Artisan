package com.example.Artisan.services;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.example.Artisan.DTOs.UserDTO;
import com.example.Artisan.entities.User;
import com.example.Artisan.exceptions.APIException;
import com.example.Artisan.payloads.LoginCredentials;
import com.example.Artisan.repositories.UserRepository;
import com.example.Artisan.responses.AuthenticationResponse;
import com.example.Artisan.security.JWTConfig;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;


import java.security.SecureRandom;
import java.util.Base64;
import java.util.Collections;


/**
 * This service manages user authentication within the application.
 *
 * ### Key functionalities:
 * - User registration
 * - User login
 * - Load user
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private static final int intitalByteSize =32;

    private final ModelMapper modelMapper;
    private final UserRepository userRepo;
    private final JWTConfig jwtConfig;
    private final AuthenticationManager authenticationManager;
    @Override
    public AuthenticationResponse registerUser(UserDTO userDTO) {

        try{
            User user = modelMapper.map(userDTO, User.class);

            User registeredUser = userRepo.save(user);

            String accessToken = jwtConfig.generateToken(registeredUser);
            String refreshToken = jwtConfig.generateRefreshToken(registeredUser);

            return AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        }catch (DataIntegrityViolationException e){
            throw new APIException("User already exists with emailId");
        }catch(ConstraintViolationException e){
            throw new APIException(e.getConstraintViolations().toString());
        }catch(Exception e){
            throw new RuntimeException("Internal Server Error");
        }

    }
    @Override
    public AuthenticationResponse loginUser(LoginCredentials credentials) {
        var authCredentials = new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword());
        authenticationManager.authenticate(authCredentials);

        User user = userRepo.findByEmail(credentials.getEmail());

        String accessToken = jwtConfig.generateToken(user);
        String refreshToken = jwtConfig.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public UserDTO getUser(String email) {
        User user = userRepo.findByEmail(email);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setPassword(null);
        return userDTO;
    }

    @Override
    public User getUserEntityByEmail(String email) {
        User user = userRepo.findByEmail(email);
        return user;
    }

    @Override
    public String forgotPassword(String email) {
        User user = userRepo.findByEmail(email);

        if(user == null){
            throw new RuntimeException("User not found with email : " + email);
        }

        byte[] bytes = new byte[intitalByteSize ]; // adjust length as needed
        new SecureRandom().nextBytes(bytes);
        String token = Base64.getEncoder().encodeToString(bytes);
        System.out.println(token);
        return null;
    }


}
