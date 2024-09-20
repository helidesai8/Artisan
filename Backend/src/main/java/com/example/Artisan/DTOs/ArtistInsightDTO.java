package com.example.Artisan.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArtistInsightDTO {
    private List<ArtistCategoryInsight> categoryInsights;
    private List<YearlySalesDTO> yearlyInsights;
    private List<CategoryRatingInsight> categoryRating;

    public ArtistInsightDTO(List<ArtistCategoryInsight> categoryInsights,
                            List<YearlySalesDTO> yearlyInsights,
                            List<CategoryRatingInsight> categoryRatings) {
        this.categoryInsights = categoryInsights;
        this.yearlyInsights = yearlyInsights;
        this.categoryRating = categoryRatings;
    }
}
