package com.example.Artisan.services.ArtistInsight;

import com.example.Artisan.DTOs.ArtistYearlyInsight;

class ArtistYearlyInsightData implements ArtistYearlyInsight {
    int _year;
    int _month;
    float _amount;

    ArtistYearlyInsightData(int year, int month, float amount) {
        this._year = year;
        this._month = month;
        this._amount = amount;
    }

    @Override
    public int getYear() {
        return _year;
    }

    @Override
    public int getMonth() {
        return _month;
    }

    @Override
    public float getAmount() {
        return _amount;
    }
}