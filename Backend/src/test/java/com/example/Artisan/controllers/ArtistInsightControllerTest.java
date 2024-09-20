package com.example.Artisan.controllers;

import com.example.Artisan.DTOs.ArtistDTO;
import com.example.Artisan.DTOs.ArtistInsightDTO;
import com.example.Artisan.services.ArtistInsightService;
import com.example.Artisan.services.artist.ArtistService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ArtistInsightControllerTest {

    @Mock
    private ArtistInsightService artistInsightService;

    @Mock
    private ArtistService artistService;

    @InjectMocks
    private ArtistInsightController artistInsightController;

    @BeforeEach
    public void Setup(){
        MockitoAnnotations.openMocks(this);
        Authentication auth = new UsernamePasswordAuthenticationToken("test_user", "password");

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void getInsights_ReturnsArtistInsightDTO() {
        ArtistDTO artistDTO = new ArtistDTO();
        artistDTO.setArtistId(1L);
        when(artistService.getArtist(anyString())).thenReturn(artistDTO);

        ArtistInsightDTO insightDTO = new ArtistInsightDTO(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        when(artistInsightService.getInsights(anyLong())).thenReturn(insightDTO);

        var result = artistInsightController.getInsights();

        verify(artistService, times(1)).getArtist(anyString());
        verify(artistInsightService, times(1)).getInsights(1L);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(insightDTO, result.getBody());
    }

    @Test
    void getInsights_NoArtistReturned_ReturnsNotFound() {
        when(artistService.getArtist(anyString())).thenReturn(null);
        var result = artistInsightController.getInsights();

        verify(artistService, times(1)).getArtist(anyString());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void getLastSales_ReturnsOkWithSalesData() {
        ArtistDTO artistDTO = new ArtistDTO();
        artistDTO.setArtistId(1L);
        when(artistService.getArtist(anyString())).thenReturn(artistDTO);
        when(artistInsightService.getLatestSaleData(anyLong())).thenReturn(Collections.emptyList());

        var result = artistInsightController.getLastSales();

        verify(artistService, times(1)).getArtist(anyString());
        verify(artistInsightService, times(1)).getLatestSaleData(1L);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testGetInsights_NoArtistReturned_ReturnsNotFound() {
        when(artistService.getArtist(anyString())).thenReturn(null);
        var result = artistInsightController.getLastSales();

        verify(artistService, times(1)).getArtist(anyString());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
}