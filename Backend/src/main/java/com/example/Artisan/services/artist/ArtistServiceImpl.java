package com.example.Artisan.services.artist;

import com.example.Artisan.DTOs.ArtistDTO;
import com.example.Artisan.entities.Artist;
import com.example.Artisan.exceptions.APIException;
import com.example.Artisan.exceptions.ArtistNotFoundException;
import com.example.Artisan.payloads.LoginCredentials;
import com.example.Artisan.repositories.ArtistRepository;
import com.example.Artisan.responses.AuthenticationResponse;
import com.example.Artisan.security.JWTConfig;
import com.example.Artisan.services.artist.ArtistService;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the ArtistService interface.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {

    private final ModelMapper modelMapper;
    private final ArtistRepository artistRepo;
    private final JWTConfig jwtConfig;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new artist.
     *
     * @param artistDTO The artist data transfer object.
     * @return The authentication response containing access and refresh tokens.
     * @throws APIException if a user already exists with the same email.
     * @throws RuntimeException if an internal server error occurs.
     */
    @Override
    public AuthenticationResponse registerArtist(ArtistDTO artistDTO) {
        try{
            Artist artist = modelMapper.map(artistDTO, Artist.class);

            Artist registeredArtist = artistRepo.save(artist);

            String accessToken = jwtConfig.generateToken(registeredArtist);
            String refreshToken = jwtConfig.generateRefreshToken(registeredArtist);

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

    /**
     * Logs in an artist.
     *
     * @param credentials The login credentials.
     * @return The authentication response containing access and refresh tokens.
     */
    @Override
    public AuthenticationResponse loginArtist(LoginCredentials credentials) {

        var authCredentials = new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword());
        authenticationManager.authenticate(authCredentials);

        Artist artist = artistRepo.findByEmail(credentials.getEmail());

        String accessToken = jwtConfig.generateToken(artist);
        String refreshToken = jwtConfig.generateRefreshToken(artist);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Retrieves an artist by email.
     *
     * @param email The email of the artist.
     * @return The artist data transfer object.
     */
    @Override
    public ArtistDTO getArtist(String email) {
        Artist artist = artistRepo.findByEmail(email);
        return mapArtistToDTO(artist);
    }

    /**
     * Retrieves an artist by ID.
     *
     * @param artistId The ID of the artist.
     * @return The artist data transfer object.
     * @throws ArtistNotFoundException if the artist is not found.
     */
    @Override
    public ArtistDTO getArtistById(long artistId) {
        Artist artist = artistRepo.findById(artistId).orElseThrow(() -> new ArtistNotFoundException(artistId));
        return mapArtistToDTO(artist);
    }

    /**
     * Retrieves all artists.
     *
     * @return The list of artist data transfer objects.
     */
    @Override
    public List<ArtistDTO> getAllArtists() {
        List<Artist> artists = artistRepo.findAll();
        List<ArtistDTO> artistDetails = new ArrayList<>();
        for (var artist: artists){
            artistDetails.add(mapArtistToDTO(artist));
        }

        return artistDetails;
    }

    /**
     * Maps an Artist entity to an ArtistDTO.
     *
     * @param artist The Artist entity.
     * @return The mapped ArtistDTO.
     */
    private ArtistDTO mapArtistToDTO(Artist artist){
        ArtistDTO artistDTO = modelMapper.map(artist, ArtistDTO.class);
        artistDTO.setPassword(null);
        return artistDTO;
    }
}
