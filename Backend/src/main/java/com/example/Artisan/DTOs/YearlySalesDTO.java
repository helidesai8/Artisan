package com.example.Artisan.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;


@Getter
@Setter
public class YearlySalesDTO {
    private int year;
    private ArrayList<MonthlyInsightDTO> monthlyInsight;
    private static int monthCount = 12;
    public YearlySalesDTO(int year) {
        this.year = year;
        this.monthlyInsight = new ArrayList<>(monthCount);
    }


}
