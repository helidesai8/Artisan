package com.example.Artisan.utils;

import com.example.Artisan.DTOs.ArtistDTO;
import com.example.Artisan.exceptions.ResourceNotFoundException;
import com.example.Artisan.services.artist.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * The TokenExtractor class is responsible for extracting information from the security context token.
 * It provides methods to extract the artist ID and email of the logged-in user from the token.
 */
@Component
public class TokenExtractor {

    @Autowired
    private ArtistService artistService;
    public Long extractArtistIdFromToken() {
        // Extract the logged-in user's email from the security context
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = null;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        // Use ArtistService to get the artist based on email
        ArtistDTO existingArtist = this.artistService.getArtist(email);
        if (existingArtist == null) {
            throw new ResourceNotFoundException("Artist", "Email", email);
        }

        return existingArtist.getArtistId();
    }
    public String extractEmailFromToken() {
        // Extract the logged-in user's email from the security context
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = null;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }
        return email;
    }
}
