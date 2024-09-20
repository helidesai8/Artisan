package com.example.Artisan.exceptions;

public class ProductCategoryNotFoundException extends RuntimeException {
    public ProductCategoryNotFoundException(Long id) {
        super("Product Category not found with id: " + id);
    }
}
