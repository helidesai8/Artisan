package com.example.Artisan.controllers;

import com.example.Artisan.DTOs.UserDTO;
import com.example.Artisan.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        Authentication auth = new UsernamePasswordAuthenticationToken("test_user", "password");
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    public void testTest() {
        String result = userController.test();
        assertEquals("Success", result);
    }

    @Test
    public void getUser_ReturnsUserDTO() {
        UserDTO userDto = new UserDTO();
        when(userService.getUser("test_user")).thenReturn(userDto);

        ResponseEntity<UserDTO> result = userController.getUser();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(userDto, result.getBody());
    }

    @AfterEach
    public void clear() {
        SecurityContextHolder.clearContext();
    }
}