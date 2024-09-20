package com.example.Artisan.services.ArtistInsight;

import com.example.Artisan.DTOs.ArtistYearlyInsight;
import com.example.Artisan.DTOs.YearlySalesDTO;
import com.example.Artisan.exceptions.APIException;
import com.example.Artisan.repositories.ArtistInsightRepository;
import com.example.Artisan.services.ArtistInsightServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ArtistInsightServiceTest {

    @Mock
    private ArtistInsightRepository artistInsightRepository;

    @InjectMocks
    private ArtistInsightServiceImpl artistInsightService;

    @BeforeEach
    public void Setup(){
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(artistInsightService, "insightYears", "2");
        ReflectionTestUtils.setField(artistInsightService, "saleRecordCount", 2);
    }

    @Test
    public void getInsights_artistIdInvalid_throwsIllegalArgumentException(){
        // Act and Assert
        Assert.assertThrows(IllegalArgumentException.class,() -> artistInsightService.getInsights(-1));
    }

    @Test
    public void getInsights_configValueUnavailable_throwsApiException(){
        ReflectionTestUtils.setField(artistInsightService, "insightYears", null);
        // Act and Assert
        Assert.assertThrows(APIException.class,() -> artistInsightService.getInsights(2));
    }

    @Test
    public void getInsights_configValueEmpty_throwsApiException(){
        ReflectionTestUtils.setField(artistInsightService, "insightYears", "  ");
        // Act and Assert
        Assert.assertThrows(APIException.class,() -> artistInsightService.getInsights(2));
    }

    @Test
    public void getLatestSaleData_artistIdInvalid_throwsIllegalArgumentException(){
        // Act and Assert
        Assert.assertThrows(IllegalArgumentException.class,() -> artistInsightService.getLatestSaleData(-1));
    }

    @Test
    public void getLatestSaleData_configValueUnavailable_throwsApiException(){
        ReflectionTestUtils.setField(artistInsightService, "insightYears", null);
        // Act and Assert
        Assert.assertThrows(APIException.class,() -> artistInsightService.getLatestSaleData(2));
    }

    @Test
    public void getLatestSaleData_configValueEmpty_throwsApiException(){
        ReflectionTestUtils.setField(artistInsightService, "insightYears", "  ");
        // Act and Assert
        Assert.assertThrows(APIException.class,() -> artistInsightService.getLatestSaleData(2));
    }

    @Test
    public void getLatestSalesData_returns_ProductSalesData(){
        when(this.artistInsightRepository.getLatestSales(1, 2)).thenReturn(new ArrayList<>());
        // Act
        var result = this.artistInsightService.getLatestSaleData(1);

        Mockito.verify(artistInsightRepository, Mockito.times(1)).getLatestSales(1, 2);
        Assertions.assertNotNull(result);
    }

    @Test
    public void getInsightData_EmptyLast5YearData_returns_emptyArtistInsightData(){
        ArrangeInsightData(1, 1);
        when(this.artistInsightRepository.getArtistYearlySales(1, 2)).thenReturn(new ArrayList<>());

        var result = this.artistInsightService.getInsights(1);

        Assertions.assertTrue(result.getYearlyInsights().isEmpty());
    }

    @Test
    public void getInsightData_OlderYearlySalesData_returns_emptyArtistInsightData(){
        ArrangeInsightData(1,4);
        when(this.artistInsightRepository.getArtistYearlySales(1, 2)).thenReturn(new ArrayList<>());

        var result = this.artistInsightService.getInsights(1);

        Assertions.assertTrue(result.getYearlyInsights().isEmpty());
    }

    @Test
    public void getInsightData_OlderYearlySalesData_returns_emptyArtistInsightData1(){
        ArrangeInsightData(1,4);
        when(this.artistInsightRepository.getArtistYearlySales(1, 2)).thenReturn(new ArrayList<>());

        var result = this.artistInsightService.getInsights(1);

        Assertions.assertTrue(result.getYearlyInsights().isEmpty());
    }

    @Test
    public void getInsightData_salesData_returns_ArtistInsightDatForMonth(){
        ArrangeInsightData(2,1);
        var result = this.artistInsightService.getInsights(2);

        Assertions.assertEquals(2, result.getYearlyInsights().size());
        Assertions.assertEquals(2024, result.getYearlyInsights().get(0).getYear());
        Assertions.assertEquals(12, result.getYearlyInsights().get(0).getMonthlyInsight().size());
        Assertions.assertEquals(344.5f, result.getYearlyInsights().get(0).getMonthlyInsight().get(3).getAmount());
    }


    private void ArrangeInsightData(long artistId, int yearDiff){
        when(this.artistInsightRepository.getArtistSalesByCategory(artistId)).thenReturn(new ArrayList<>());
        when(this.artistInsightRepository.getArtistCategoryRating(artistId)).thenReturn(new ArrayList<>());
        int currentYear = Year.now().getValue();
        var yearlyData = new ArrayList<ArtistYearlyInsight>(){{
            add(new ArtistYearlyInsightData(currentYear, 2, 234.5f));
            add(new ArtistYearlyInsightData(currentYear, 4, 344.5f));
            add(new ArtistYearlyInsightData(currentYear, 8, 1167.90f));
            add(new ArtistYearlyInsightData(currentYear-yearDiff, 1, 88.90f));
            add(new ArtistYearlyInsightData(currentYear-yearDiff, 5, 367.90f));
            add(new ArtistYearlyInsightData(currentYear-yearDiff, 11, 989.90f));
        }};
        when(this.artistInsightRepository.getArtistYearlySales(artistId, 2)).thenReturn(yearlyData);
    }

}
