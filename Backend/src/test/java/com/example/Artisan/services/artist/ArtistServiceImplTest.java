package com.example.Artisan.services.artist;


import com.example.Artisan.DTOs.ArtistDTO;
import com.example.Artisan.entities.Artist;
import com.example.Artisan.exceptions.APIException;
import com.example.Artisan.payloads.LoginCredentials;
import com.example.Artisan.repositories.ArtistRepository;
import com.example.Artisan.responses.AuthenticationResponse;
import com.example.Artisan.security.JWTConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class ArtistServiceImplTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private JWTConfig jwtConfig;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private ArtistServiceImpl artistService;

    @BeforeEach
    public void setup() {
        openMocks(this);
    }

    @Test
    public void registerArtist_successfulRegistration_returnsAuthenticationResponse() {
        ArtistDTO artistDTO = new ArtistDTO();
        artistDTO.setEmail("artist@example.com");

        Artist artist = new Artist();
        artist.setEmail("artist@example.com");

        when(modelMapper.map(artistDTO, Artist.class)).thenReturn(artist);
        when(artistRepository.save(artist)).thenReturn(artist);
        when(jwtConfig.generateToken(artist)).thenReturn("accessToken");
        when(jwtConfig.generateRefreshToken(artist)).thenReturn("refreshToken");

        AuthenticationResponse response = artistService.registerArtist(artistDTO);

        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        verify(artistRepository, times(1)).save(artist);
    }

    @Test
    public void registerArtist_withExistingEmail_throwsAPIException() {
        ArtistDTO artistDTO = new ArtistDTO();
        artistDTO.setEmail("artist@example.com");

        when(modelMapper.map(artistDTO, Artist.class)).thenReturn(new Artist());
        when(artistRepository.save(any(Artist.class)))
                .thenThrow(new DataIntegrityViolationException("User already exists"));

        Exception exception = assertThrows(APIException.class, () -> artistService.registerArtist(artistDTO));
        assertEquals("User already exists with emailId", exception.getMessage());
    }

    @Test
    public void loginArtist_successfulLogin_returnsAuthenticationResponse() {
        LoginCredentials credentials = new LoginCredentials();
        credentials.setEmail("artist@example.com");
        credentials.setPassword("password");

        Artist artist = new Artist();
        artist.setEmail("artist@example.com");

        when(artistRepository.findByEmail(anyString())).thenReturn(artist);
        when(jwtConfig.generateToken(artist)).thenReturn("accessToken");
        when(jwtConfig.generateRefreshToken(artist)).thenReturn("refreshToken");

        AuthenticationResponse response = artistService.loginArtist(credentials);

        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(artistRepository, times(1)).findByEmail(anyString());
    }

    @Test
    public void getArtist_returnsArtistDTO() {
        String email = "artist@example.com";

        Artist artist = new Artist();
        artist.setEmail(email);
        ArtistDTO artistDTO = new ArtistDTO();
        artistDTO.setEmail(email);

        when(artistRepository.findByEmail(email)).thenReturn(artist);
        when(modelMapper.map(artist, ArtistDTO.class)).thenReturn(artistDTO);

        ArtistDTO result = artistService.getArtist(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(artistRepository, times(1)).findByEmail(email);
    }
}
