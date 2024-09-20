package com.example.Artisan.exceptions;

public class RatingNotFoundException extends RuntimeException {
    public RatingNotFoundException(Long productId, Long userId) {
        super("Rating not found for product " + productId + " by user " + userId);
    }
}