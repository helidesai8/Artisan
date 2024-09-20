package com.example.Artisan.DTOs.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    private String customer_email;
    private PaymentIntentDTO payment_intent;
    private String payment_status;
    private ShippingDetailDTO shipping_details;
    private String status;
}
