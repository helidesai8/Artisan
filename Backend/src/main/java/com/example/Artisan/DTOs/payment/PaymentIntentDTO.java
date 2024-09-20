package com.example.Artisan.DTOs.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentIntentDTO {
    private int amount;
    private int amount_received;
    private String currency;
    private PaymentMethodDTO payment_method;
    private String status;
}
