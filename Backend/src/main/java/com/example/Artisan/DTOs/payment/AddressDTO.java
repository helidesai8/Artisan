package com.example.Artisan.DTOs.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private String city;
    private String country;
    private String line1;
    private String line2;
    private String postal_code;
    private String state;
}
