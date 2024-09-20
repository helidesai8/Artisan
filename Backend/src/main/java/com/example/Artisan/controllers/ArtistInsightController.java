package com.example.Artisan.controllers;

import com.example.Artisan.DTOs.ArtistDTO;
import com.example.Artisan.DTOs.ArtistInsightDTO;
import com.example.Artisan.DTOs.products.ProductSales;
import com.example.Artisan.services.ArtistInsightService;
import com.example.Artisan.services.artist.ArtistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This is a Rest Controller for managing Artist Insight related requests.
 * It is mapped to the "/api/v1/artist-insight" URL path.
 */
@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/artist-insight")
public class ArtistInsightController {

    private final ArtistInsightService artistInsightService;
    private final ArtistService artistService;

    /**
     * Constructor for dependency injection of ArtistInsightService and ArtistService.
     *
     * @param insightService The service to handle artist insight related operations.
     * @param artistService The service to handle artist related operations.
     */
    public ArtistInsightController(ArtistInsightService insightService, ArtistService artistService) {
        this.artistInsightService = insightService;
        this.artistService = artistService;
    }

    /**
     * Endpoint to get insights of the current authenticated artist.
     * It is mapped to the "/api/v1/artist-insight" URL path.
     *
     * @return ResponseEntity containing the ArtistInsightDTO of the authenticated artist.
     * If the artist is not found, it returns a not found status.
     */
    @GetMapping()
    public ResponseEntity<ArtistInsightDTO> getInsights(){
        ArtistDTO artist = artistService.getArtist(SecurityContextHolder.getContext().getAuthentication().getName());
        if(artist == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ArtistInsightDTO artistInsight = artistInsightService.getInsights(artist.getArtistId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(artistInsight);
    }

    /**
     * Endpoint to get the last sales of the current authenticated artist.
     * It is mapped to the "/api/v1/artist-insight/sales" URL path.
     *
     * @return ResponseEntity containing a list of ProductSales of the authenticated artist.
     * If the artist is not found, it returns a not found status.
     */
    @GetMapping("/sales")
    public ResponseEntity<List<ProductSales>> getLastSales(){
        ArtistDTO artist = artistService.getArtist(SecurityContextHolder.getContext().getAuthentication().getName());
        if(artist == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        var saleData = artistInsightService.getLatestSaleData(artist.getArtistId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(saleData);
    }
}