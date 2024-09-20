package com.example.Artisan.DTOs.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInventoryDTO {

    private Long productId;
    private Integer quantity;

}
