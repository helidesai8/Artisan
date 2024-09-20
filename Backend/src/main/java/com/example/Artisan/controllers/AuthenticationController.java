package com.example.Artisan.controllers;

import com.example.Artisan.DTOs.ArtistDTO;
import com.example.Artisan.DTOs.UserDTO;
import com.example.Artisan.exceptions.APIException;
import com.example.Artisan.payloads.LoginCredentials;
import com.example.Artisan.responses.AuthenticationResponse;
import com.example.Artisan.services.MailService;
import com.example.Artisan.services.artist.ArtistServiceImpl;
import com.example.Artisan.services.UserService;
import com.example.Artisan.utils.Mail;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static com.example.Artisan.entities.Role.ARTIST;
import static com.example.Artisan.entities.Role.USER;

@RestController
@CrossOrigin
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final ArtistServiceImpl artistService;
    private final MailService mailService;


    @PostMapping("/mail")
    public String sendMail(@RequestBody Mail mail){
        mailService.sendMail(mail.getEmail(),mail.getSubject(), mail.getContent());
        return "String";
    }
    @PostMapping("/user/register")
    public ResponseEntity<AuthenticationResponse> userRegisterHandler(HttpServletRequest request, HttpServletResponse response, @Valid @RequestBody UserDTO user){

        String encodedPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPass);
        user.setRole(USER);

        try {
            AuthenticationResponse authenticationResponse = userService.registerUser(user);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(authenticationResponse);
        } catch (APIException e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(getErrorMessage(e.getMessage()));

        }
    }

    @PostMapping("/user/login")
    public ResponseEntity<AuthenticationResponse> UserLoginHandler(HttpServletRequest request, @Valid @RequestBody LoginCredentials credentials){
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userService.loginUser(credentials));
        }catch(AuthenticationException e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(getErrorMessage(e.getMessage()));

        }
    }

    @PostMapping("/user/forgot")
    public ResponseEntity<String> UserLoginHandler(String email){
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userService.forgotPassword(email));
        }catch(AuthenticationException e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Bad Request");

        }
    }

    @PostMapping("/artist/register")
    public ResponseEntity<AuthenticationResponse> artistRegisterHandler(HttpServletRequest request, HttpServletResponse response, @Valid @RequestBody ArtistDTO artist){

        String encodedPass = passwordEncoder.encode(artist.getPassword());
        artist.setPassword(encodedPass);
        artist.setRole(ARTIST);

        try {
            AuthenticationResponse authenticationResponse = artistService.registerArtist(artist);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(authenticationResponse);
        } catch (APIException e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(getErrorMessage(e.getMessage()));
        }
    }

    @PostMapping("/artist/login")
    public ResponseEntity<AuthenticationResponse> artistLoginHandler(HttpServletRequest request, @Valid @RequestBody LoginCredentials credentials){
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(artistService.loginArtist(credentials));
        }catch(AuthenticationException e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(getErrorMessage(e.getMessage()));

        }
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthenticationResponse> logoutHandler(HttpServletRequest request){
        return null;
    }

    private AuthenticationResponse getErrorMessage(String message){
        return AuthenticationResponse.builder().message(message).build();
    }
}
