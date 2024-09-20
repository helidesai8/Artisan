package com.example.Artisan.services;

import com.example.Artisan.DTOs.*;
import com.example.Artisan.DTOs.products.ProductSales;
import com.example.Artisan.exceptions.APIException;
import com.example.Artisan.repositories.ArtistInsightRepository;
import com.example.Artisan.repositories.ArtistRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Service implementation for artist insights.
 */
@Service
@Transactional
public class ArtistInsightServiceImpl implements ArtistInsightService {

    private static final int monthCount = 12;
    private ArtistInsightRepository artistInsightRepo;

    /**
     * Constructor for ArtistInsightServiceImpl.
     * @param artistInsightRepository The artist insight repository.
     */
    @Autowired
    public ArtistInsightServiceImpl(ArtistInsightRepository artistInsightRepository) {
        this.artistInsightRepo = artistInsightRepository;
    }

    @Value("${saleInsightYears}")
    private String insightYears;

    @Value("${saleRecordCount}")
    private int saleRecordCount;

    /**
     * Get insights for an artist.
     * @param artistId The ID of the artist.
     * @return The artist insight DTO.
     * @throws IllegalArgumentException If the artist ID is not valid.
     * @throws APIException If the configuration value for insight years cannot be retrieved.
     */
    @Override
    public ArtistInsightDTO getInsights(long artistId){
        if(artistId <= 0){
            throw new IllegalArgumentException("Artist id not valid");
        }
        if(insightYears == null || insightYears.isBlank()){
            throw new APIException("Not able to retrieve config value for insight years");
        }

        int numberOfYears = Integer.parseInt(insightYears);

        var categoryData = this.GetInsightForArtCategory(artistId);
        var yearlyData = this.GetLast5YearInsight(artistId, numberOfYears);
        var categoryRatings = this.GetAverageRatingForArtCategory(artistId);
        var reportData = this.GenerateYearlyInsights(yearlyData);
        return new ArtistInsightDTO(categoryData, reportData, categoryRatings);
    }

    /**
     * Get the latest sale data for an artist.
     * @param artistId The ID of the artist.
     * @return The list of product sales.
     * @throws IllegalArgumentException If the artist ID is not valid.
     * @throws APIException If the configuration value for insight years cannot be retrieved.
     */
    @Override
    public List<ProductSales> getLatestSaleData(long artistId){
        if(artistId <= 0){
            throw new IllegalArgumentException("Artist id not valid");
        }
        if(insightYears == null || insightYears.isBlank()){
            throw new APIException("Not able to retrieve config value for insight years");
        }

        return this.artistInsightRepo.getLatestSales(artistId, saleRecordCount);
    }

    /**
     * Get the last 5 years of sales insight for an artist.
     * @param id The ID of the artist.
     * @param numberOfYears The number of years to retrieve.
     * @return The list of artist yearly insights.
     */
    private List<ArtistYearlyInsight> GetLast5YearInsight(long id, int numberOfYears){
        return this.artistInsightRepo.getArtistYearlySales(id, numberOfYears);
    }

    /**
     * Get the sales insight for each art category of an artist.
     * @param id The ID of the artist.
     * @return The list of artist category insights.
     */
    private List<ArtistCategoryInsight> GetInsightForArtCategory(long id){
        return this.artistInsightRepo.getArtistSalesByCategory(id);
    }

    /**
     * Get the average rating for each art category of an artist.
     * @param id The ID of the artist.
     * @return The list of category rating insights.
     */
    private List<CategoryRatingInsight> GetAverageRatingForArtCategory(long id){
        return this.artistInsightRepo.getArtistCategoryRating(id);
    }

    /**
     * Generate yearly sales insights based on the artist yearly sales data.
     * @param saleData The list of artist yearly insights.
     * @return The list of yearly sales DTOs.
     */
    private List<YearlySalesDTO> GenerateYearlyInsights(List<ArtistYearlyInsight> saleData){
        if(saleData == null || saleData.isEmpty()){
            return List.of();
        }

        int saleInsightYears = Integer.parseInt(insightYears);
        // initialize the yearly report data list with a capacity as the number of previous year data required
        List<YearlySalesDTO> saleReportData = new ArrayList<>(saleInsightYears);
        int currentYear = Year.now().getValue();
        int endYear = currentYear-saleInsightYears;
        int yearCounter = 0;
        // run loop for number of years data required
        while(currentYear > endYear)
        {
            YearlySalesDTO yearlySales = new YearlySalesDTO(currentYear);
            int year =yearlySales.getYear();
            Supplier<Stream<ArtistYearlyInsight>> streamSupplier = () -> saleData.stream().filter(sale -> sale.getYear() == year);
            // map data returned from the database for each month in a year individually
            for(int i = 0; i < monthCount; i++){
                var currentYearMonthlyInsight = yearlySales.getMonthlyInsight();
                int month = i+1;
                float amount = 0;
                if(streamSupplier.get().count() > 0) {
                    var monthAmount = streamSupplier.get().filter(t -> t.getMonth() == month).findFirst().orElse(null);
                    if (monthAmount != null) {
                        amount = monthAmount.getAmount();
                    }
                }
                currentYearMonthlyInsight.add(i, new MonthlyInsightDTO(month,  amount));
            }
            currentYear --;

            saleReportData.add(yearCounter, yearlySales);
            yearCounter ++;
        }

        return  saleReportData;
    }
}
