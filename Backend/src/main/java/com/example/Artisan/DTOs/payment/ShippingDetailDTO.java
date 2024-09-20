package com.example.Artisan.DTOs.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingDetailDTO {
    private AddressDTO address;
    private String name;
    private String phone;
    private String tracking_number;
}
