package com.example.Artisan.controllers;

import com.example.Artisan.DTOs.ArtistDTO;
import com.example.Artisan.services.artist.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This is a Rest Controller for managing Artist related requests.
 * It is mapped to the "/api/v1/artist" URL path.
 */
@RestController
@CrossOrigin
@RequestMapping("api/v1/artist")
public class ArtistController {

    private ArtistService artistService;

    /**
     * Autowired constructor for dependency injection of ArtistService.
     *
     * @param artistService The service to handle artist related operations.
     */
    @Autowired
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    /**
     * Endpoint to get the current authenticated artist.
     * It is mapped to the "/me" URL path.
     *
     * @return ResponseEntity containing the ArtistDTO of the authenticated artist.
     */
    @GetMapping("/me")
    public ResponseEntity<ArtistDTO> getArtist(){
        var userName = SecurityContextHolder.getContext().getAuthentication().getName();
        var artist = artistService.getArtist(userName);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(artist);
    }

    /**
     * Endpoint to get all artists.
     * It is mapped to the "/all" URL path.
     *
     * @return ResponseEntity containing a list of all ArtistDTOs.
     * In case of any exception, it returns an internal server error.
     */
    @GetMapping("/all")
    public ResponseEntity<List<ArtistDTO>>getAllArtists(){
        try {
            var artist = artistService.getAllArtists();
            return ResponseEntity.ok(artist);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(null);
        }
    }
}