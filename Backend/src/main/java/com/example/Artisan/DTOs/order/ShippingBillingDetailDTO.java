package com.example.Artisan.DTOs.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingBillingDetailDTO {
    private String city;
    private String state;
    private String line1;
    private String line2;
    private String postal_code;
    private String country;
    private String name;
    private String billing_postal_code;
    private String billing_country;
    private String billing_name;
}
