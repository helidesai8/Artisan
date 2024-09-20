package com.example.Artisan.services;

import com.example.Artisan.DTOs.ArtistInsightDTO;
import com.example.Artisan.DTOs.products.ProductSales;

import java.util.List;

/**
 * The ArtistInsightService interface provides methods to retrieve insights and sales data for an artist.
 */
public interface ArtistInsightService {
    /**
     * Retrieves the insights for a specific artist.
     *
     * @param artistId the ID of the artist
     * @return the ArtistInsightDTO containing the insights
     */
    ArtistInsightDTO getInsights(long artistId);

    /**
     * Retrieves the latest sales data for a specific artist.
     *
     * @param artistId the ID of the artist
     * @return a list of ProductSales containing the latest sales data
     */
    List<ProductSales> getLatestSaleData(long artistId);
}
