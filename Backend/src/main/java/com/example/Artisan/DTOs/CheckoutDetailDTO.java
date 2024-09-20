package com.example.Artisan.DTOs;

import com.example.Artisan.DTOs.products.CheckoutProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutDetailDTO {
    private List<CheckoutProductDTO> products;
    private BigDecimal totalAmount;
    private String customerEmail;
}
