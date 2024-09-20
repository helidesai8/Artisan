package com.example.Artisan.utils;

import com.example.Artisan.DTOs.ArtistDTO;
import com.example.Artisan.entities.User;
import com.example.Artisan.exceptions.ResourceNotFoundException;
import com.example.Artisan.services.artist.ArtistService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TokenExtractorTest {

    @Mock
    private Authentication authentication;

    @Mock
    private ArtistService artistService;

    @InjectMocks
    private TokenExtractor tokenExtractor;

    @BeforeEach
    public void setUp() {
        // Initialize mocks and set up testing context
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void extractArtistId_UserDetailToken_returnsArtistId() {
        setUserAuthContext();
        ArtistDTO artistDTO = new ArtistDTO();
        artistDTO.setArtistId(1L);
        when(artistService.getArtist("test1@test1.com")).thenReturn(artistDTO);

        Long artistId = tokenExtractor.extractArtistIdFromToken();

        Assertions.assertEquals(1L, artistId);
    }

    @Test
    void extractArtistId_plainToken_returnsArtistId() {
        setSimpleAuthContext();
        ArtistDTO artistDTO = new ArtistDTO();
        artistDTO.setArtistId(1L);
        when(artistService.getArtist("test@test.com")).thenReturn(artistDTO);

        Long artistId = tokenExtractor.extractArtistIdFromToken();

        Assertions.assertEquals(1L, artistId);
    }

    @Test
    void extractArtistId_whenNoArtistPresent_throwsResourceNotFound() {
        setSimpleAuthContext();
        ArtistDTO artistDTO = new ArtistDTO();
        artistDTO.setArtistId(1L);
        when(artistService.getArtist("test@test.com")).thenReturn(null);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> tokenExtractor.extractArtistIdFromToken());
    }

    @Test
    void extractEmailFromToken_UserDetailToken_returnsEmail() {
        setUserAuthContext();
        String email = tokenExtractor.extractEmailFromToken();

        Assertions.assertEquals("test1@test1.com", email);
    }

    @Test
    void extractEmailFromToken_simpleToken_returnsEmail() {
        setSimpleAuthContext();
        String email = tokenExtractor.extractEmailFromToken();

        Assertions.assertEquals("test@test.com", email);
    }

    @AfterEach
    public void clear(){
        SecurityContextHolder.clearContext();
    }

    private void setSimpleAuthContext(){
        Authentication auth = new UsernamePasswordAuthenticationToken("test@test.com", "password");
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private void setUserAuthContext(){
        User user = new User();
        user.setEmail("test1@test1.com");
        Authentication auth = new UsernamePasswordAuthenticationToken(user, "password");
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

}