package com.example.Artisan.DTOs.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private String provider;
    private String card_number;
    private String card_Type;
}
