package com.example.Artisan.DTOs.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO {
    private String brand;
    private String country;
    private String display_brand;
    private int exp_month;
    private int exp_year;
    private String funding;
    private String last4;
}
