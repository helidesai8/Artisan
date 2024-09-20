package com.example.Artisan.controllers;

import com.example.Artisan.DTOs.ArtistDTO;
import com.example.Artisan.services.artist.ArtistService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArtistControllerTest {
    @InjectMocks
    private ArtistController controller;
    @Mock
    private ArtistService artistService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllArtists_returnsArtistList() {
        List<ArtistDTO> expectedArtists = new ArrayList<>() {{
            add(new ArtistDTO());
        }};
        Mockito.when(artistService.getAllArtists()).thenReturn(expectedArtists);

        ResponseEntity<List<ArtistDTO>> response = controller.getAllArtists();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getAllArtists_serviceException_returnsInternalServerError() {
        Mockito.when(artistService.getAllArtists()).thenThrow(new RuntimeException());

        var response = controller.getAllArtists();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void getArtist_AuthContextArtist_returnsArtisDto() {
        String artistEmail = "artist@example.com";
        Authentication auth = new UsernamePasswordAuthenticationToken(artistEmail, "password");
        SecurityContextHolder.getContext().setAuthentication(auth);
        Mockito.when(artistService.getArtist(artistEmail)).thenReturn(new ArtistDTO());

        var response = controller.getArtist();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @GetMapping("/me")
    public ResponseEntity<ArtistDTO> getArtist() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(artistService.getArtist(SecurityContextHolder.getContext().getAuthentication().getName()));
    }
}
