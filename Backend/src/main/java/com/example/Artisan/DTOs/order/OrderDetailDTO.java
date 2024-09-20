package com.example.Artisan.DTOs.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    private long orderId;
    private BigDecimal total;
    private LocalDateTime orderDate;
    private List<OrderItemDTO> items;
    private PaymentDTO paymentDetail;
    private ShippingBillingDetailDTO shippingBillingDetail;
}
