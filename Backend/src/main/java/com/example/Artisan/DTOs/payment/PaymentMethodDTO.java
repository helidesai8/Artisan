package com.example.Artisan.DTOs.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDTO {
    private BillingDetailDTO billing_details;
    private CardDTO card;
    private String type;
}
