package com.example.Artisan.controllers;

import com.example.Artisan.DTOs.ArtistDTO;
import com.example.Artisan.DTOs.UserDTO;
import com.example.Artisan.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The UserController class handles the HTTP requests related to user operations.
 */
@RestController
@CrossOrigin
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    UserService userService;

    /**
     * Handles the GET request to "/test" endpoint.
     * @return A string indicating the success of the request.
     */
    @GetMapping("/test")
    public String test(){
        return "Success";
    }

    /**
     * Handles the GET request to "/me" endpoint.
     * Retrieves the currently authenticated user's information.
     * @return A ResponseEntity containing the UserDTO object and the HTTP status code.
     */
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUser(){
        var userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUser(userName));
    }
}
