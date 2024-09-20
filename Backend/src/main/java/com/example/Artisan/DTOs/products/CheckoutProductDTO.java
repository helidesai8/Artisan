package com.example.Artisan.DTOs.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutProductDTO extends ProductBaseDTO {
    private String name;
    private BigDecimal price;
    private String img_url;

    public CheckoutProductDTO(Long id, Integer quantity, String name, BigDecimal price,String imgUrl ) {
        super(id, quantity);
        this.name = name;
        this.price = price;
        this.img_url = imgUrl;
    }
}
