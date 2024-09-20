package com.example.Artisan.DTOs.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductBaseDTO {
    private Long id;
    private Integer quantity;
}
