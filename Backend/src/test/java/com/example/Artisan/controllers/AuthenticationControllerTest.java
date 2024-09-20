package com.example.Artisan.controllers;

import com.example.Artisan.DTOs.ArtistDTO;
import com.example.Artisan.DTOs.UserDTO;
import com.example.Artisan.exceptions.APIException;
import com.example.Artisan.payloads.LoginCredentials;
import com.example.Artisan.responses.AuthenticationResponse;
import com.example.Artisan.services.UserService;
import com.example.Artisan.services.artist.ArtistServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserService userService;

    @Mock
    private ArtistServiceImpl artistService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void userRegisterHandler_ReturnsAuthenticationResponse() {
        UserDTO userDto = new UserDTO();
        userDto.setPassword("password");
        AuthenticationResponse authResponse = new AuthenticationResponse();
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(userService.registerUser(userDto)).thenReturn(authResponse);

        ResponseEntity<AuthenticationResponse> response = authenticationController.userRegisterHandler(null, null, userDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
    }

    @Test
    void userLoginHandler_ReturnsAuthenticationResponse() {
        LoginCredentials credentials = new LoginCredentials();
        AuthenticationResponse authResponse = new AuthenticationResponse();
        when(userService.loginUser(credentials)).thenReturn(authResponse);

        ResponseEntity<AuthenticationResponse> response = authenticationController.UserLoginHandler(null, credentials);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
    }

    @Test
    void artistRegisterHandler_ReturnsAuthenticationResponse() {
        ArtistDTO artistDto = new ArtistDTO();
        artistDto.setPassword("password");
        AuthenticationResponse authResponse = new AuthenticationResponse();
        when(passwordEncoder.encode(artistDto.getPassword())).thenReturn("encodedPassword");
        when(artistService.registerArtist(artistDto)).thenReturn(authResponse);

        ResponseEntity<AuthenticationResponse> response = authenticationController.artistRegisterHandler(null, null, artistDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
    }

    @Test
    void artistLoginHandler_ReturnsAuthenticationResponse() {
        LoginCredentials credentials = new LoginCredentials();
        AuthenticationResponse authResponse = new AuthenticationResponse();
        when(artistService.loginArtist(credentials)).thenReturn(authResponse);

        ResponseEntity<AuthenticationResponse> response = authenticationController.artistLoginHandler(null, credentials);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
    }

    @Test
    public void userRegisterHandler_SuccessfulRegistration() {
        UserDTO userDto = new UserDTO();
        userDto.setPassword("password");
        AuthenticationResponse authResponse = new AuthenticationResponse();
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(userService.registerUser(userDto)).thenReturn(authResponse);

        ResponseEntity<AuthenticationResponse> response = authenticationController.userRegisterHandler(null, null, userDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
    }

    @Test
    public void userRegisterHandler_RegistrationFailure() {
        UserDTO userDto = new UserDTO();
        userDto.setPassword("password");
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(userService.registerUser(userDto)).thenThrow(new APIException("Registration failed"));

        ResponseEntity<AuthenticationResponse> response = authenticationController.userRegisterHandler(null, null, userDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Registration failed", response.getBody().getMessage());
    }

    @Test
    public void userLoginHandler_SuccessfulLogin() {
        LoginCredentials credentials = new LoginCredentials();
        AuthenticationResponse authResponse = new AuthenticationResponse();
        when(userService.loginUser(credentials)).thenReturn(authResponse);

        ResponseEntity<AuthenticationResponse> response = authenticationController.UserLoginHandler(null, credentials);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
    }

    @Test
    public void userLoginHandler_LoginFailure() {
        LoginCredentials credentials = new LoginCredentials();
        when(userService.loginUser(credentials)).thenThrow(new AuthenticationException("Login failed") {});

        ResponseEntity<AuthenticationResponse> response = authenticationController.UserLoginHandler(null, credentials);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Login failed", response.getBody().getMessage());
    }

    @Test
    public void artistRegisterHandler_SuccessfulRegistration() {
        ArtistDTO artistDto = new ArtistDTO();
        artistDto.setPassword("password");
        AuthenticationResponse authResponse = new AuthenticationResponse();
        when(passwordEncoder.encode(artistDto.getPassword())).thenReturn("encodedPassword");
        when(artistService.registerArtist(artistDto)).thenReturn(authResponse);

        ResponseEntity<AuthenticationResponse> response = authenticationController.artistRegisterHandler(null, null, artistDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
    }

    @Test
    public void artistRegisterHandler_RegistrationFailure() {
        ArtistDTO artistDto = new ArtistDTO();
        artistDto.setPassword("password");
        when(passwordEncoder.encode(artistDto.getPassword())).thenReturn("encodedPassword");
        when(artistService.registerArtist(artistDto)).thenThrow(new APIException("Registration failed"));

        ResponseEntity<AuthenticationResponse> response = authenticationController.artistRegisterHandler(null, null, artistDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Registration failed", response.getBody().getMessage());
    }

    @Test
    public void artistLoginHandler_SuccessfulLogin() {
        LoginCredentials credentials = new LoginCredentials();
        AuthenticationResponse authResponse = new AuthenticationResponse();
        when(artistService.loginArtist(credentials)).thenReturn(authResponse);

        ResponseEntity<AuthenticationResponse> response = authenticationController.artistLoginHandler(null, credentials);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
    }

    @Test
    public void artistLoginHandler_LoginFailure() {
        LoginCredentials credentials = new LoginCredentials();
        when(artistService.loginArtist(credentials)).thenThrow(new AuthenticationException("Login failed") {
        });

        ResponseEntity<AuthenticationResponse> response = authenticationController.artistLoginHandler(null, credentials);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Login failed", response.getBody().getMessage());
        assertTrue(response.getBody().getMessage().contains("Login failed"));
    }
}