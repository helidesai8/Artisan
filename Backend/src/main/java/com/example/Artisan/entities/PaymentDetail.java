package com.example.Artisan.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class PaymentDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String payment_type;

    @Column
    private String provider;

    @Column
    private String card_number;

    @Column
    private String card_Type;

    @Column
    private String expiry;

    @Column(nullable = false)
    private String Status;

    @Column(nullable = false)
    private BigDecimal Amount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, referencedColumnName = "order_id")
    private OrderDetail orderDetail;
}
