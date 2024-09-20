package com.example.Artisan.controllers;

import com.example.Artisan.DTOs.ArtistDTO;
import com.example.Artisan.exceptions.InvalidFileException;
import com.example.Artisan.exceptions.ResourceNotFoundException;
import com.example.Artisan.services.artist.ArtistService;
import com.example.Artisan.services.profile.ArtistProfileImageService;
import com.example.Artisan.services.profile.ArtistProfileService;
import com.example.Artisan.utils.TokenExtractor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class ArtistProfileControllerTest {
    @Mock
    private ArtistProfileService artistProfileService;
    @Mock
    private TokenExtractor tokenExtractor;
    @Mock
    private ArtistProfileImageService profileImageService;

    @InjectMocks
    private ArtistProfileController controller;

    @BeforeEach
    public void Setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getArtistById_invalidId_returnsBadRequest() {
        Long invalidId = 0L;

        ResponseEntity<ArtistDTO> response = controller.getArtistById(invalidId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void getArtistById_artistNotFound_throwsResourceNotFoundException() {
        Long artistId = 1L;
        when(artistProfileService.getArtistDetails(artistId)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> controller.getArtistById(artistId));
    }

    @Test
    public void getArtistById_artistFound_returnsArtistDTO() {
        Long artistId = 1L;
        ArtistDTO expectedArtistDTO = createArtistDTO();
        when(artistProfileService.getArtistDetails(artistId)).thenReturn(expectedArtistDTO);

        ResponseEntity<ArtistDTO> response = controller.getArtistById(artistId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedArtistDTO, response.getBody());
        assertNull(response.getBody().getPassword());
    }

    @Test
     void testGetSingleArtist_returnsSuccess(){
        when(tokenExtractor.extractEmailFromToken()).thenReturn("testemail@test.com");
        when(tokenExtractor.extractArtistIdFromToken()).thenReturn(1L);
        when(artistProfileService.getArtistDetails(1L)).thenReturn(new ArtistDTO());

        var result = controller.getSingleArtist();
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testGetSingleArtist_noArtistPresent_throwsResourceNotFoundException(){
        when(tokenExtractor.extractEmailFromToken()).thenReturn("testemail@test.com");
        when(tokenExtractor.extractArtistIdFromToken()).thenReturn(1L);
        when(artistProfileService.getArtistDetails(1L)).thenReturn(null);

       Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.getSingleArtist());
    }

    @Test
    public void testDeleteProfileImage_Success() {
        Long mockArtistId = 1L;

        when(tokenExtractor.extractArtistIdFromToken()).thenReturn(mockArtistId);
        doNothing().when(profileImageService).deleteProfileImage(mockArtistId);
        // Call the method
        var response = controller.deleteProfileImage();

        // Assert response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Profile image deleted successfully.", response.getBody().getMessage());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    public void testDeleteProfileImage_returnsNotFound() {

        var expectedException = new ResourceNotFoundException("Artist", "id", String.valueOf(1L));
        when(tokenExtractor.extractArtistIdFromToken()).thenThrow(expectedException);

        var response = controller.deleteProfileImage();

        // Assert response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(expectedException.getMessage(), response.getBody().getMessage());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    public void testDeleteProfileImage_returnsBadRequest() {

        var expectedException = new InvalidFileException("test");
        when(tokenExtractor.extractArtistIdFromToken()).thenThrow(expectedException);

        var response = controller.deleteProfileImage();

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expectedException.getMessage(), response.getBody().getMessage());
        assertFalse(response.getBody().isSuccess());
    }

    private ArtistDTO createArtistDTO() {
        var artist = new ArtistDTO();
        artist.setEmail("test@test.com");
        return artist;
    }
}
