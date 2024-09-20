package com.example.Artisan.DTOs.products;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO extends ProductBaseDTO {
    private String name;
    private String description;
    private BigDecimal price;
    private String categoryName;
    private List<String> imageUrls;
    private Boolean isActive;
    private long artistId;
}