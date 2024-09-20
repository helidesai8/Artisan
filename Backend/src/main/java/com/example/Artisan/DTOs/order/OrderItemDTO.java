package com.example.Artisan.DTOs.order;

import com.example.Artisan.DTOs.products.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private ProductDTO product;
    private int quantity;
    private BigDecimal amount;
}
