package com.example.Artisan.services.artist;

import com.example.Artisan.DTOs.ArtistDTO;
import com.example.Artisan.DTOs.UserDTO;
import com.example.Artisan.payloads.LoginCredentials;
import com.example.Artisan.responses.AuthenticationResponse;

import java.util.List;

public interface ArtistService {
    /**
     * Registers a new artist.
     *
     * @param artistDTO The artist data transfer object containing the artist information.
     * @return An authentication response indicating the success or failure of the registration.
     */
    AuthenticationResponse registerArtist(ArtistDTO artistDTO);

    /**
     * Logs in an existing artist.
     *
     * @param loginCredentials The login credentials of the artist.
     * @return An authentication response indicating the success or failure of the login.
     */
    AuthenticationResponse loginArtist(LoginCredentials loginCredentials);

    /**
     * Retrieves an artist by their email.
     *
     * @param email The email of the artist.
     * @return The artist data transfer object.
     */
    ArtistDTO getArtist(String email);

    /**
     * Retrieves an artist by their ID.
     *
     * @param artistId The ID of the artist.
     * @return The artist data transfer object.
     */
    ArtistDTO getArtistById(long artistId);

    /**
     * Retrieves all artists.
     *
     * @return A list of artist data transfer objects.
     */
    List<ArtistDTO> getAllArtists();
}
