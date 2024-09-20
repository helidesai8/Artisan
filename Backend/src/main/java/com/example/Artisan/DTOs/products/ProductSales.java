package com.example.Artisan.DTOs.products;

import java.sql.Date;

public interface ProductSales {
        Date getOrderDate();

        int getQuantity();

        String getName();

        String getPrice();

        String getOrderBy();
}
