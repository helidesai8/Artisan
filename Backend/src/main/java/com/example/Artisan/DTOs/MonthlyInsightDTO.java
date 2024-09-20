package com.example.Artisan.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

@Getter
@Setter
public class MonthlyInsightDTO {
    private String month;
    private float amount;

    public MonthlyInsightDTO(int month, float amount) {
        this.month = Month.of(month).getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        this.amount = amount;
    }
}
