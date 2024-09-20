package com.example.Artisan.exceptions;

public class PaymentGatewayException extends RuntimeException{
    public PaymentGatewayException(String message) {
            super("payment gateway error");
    }
}
