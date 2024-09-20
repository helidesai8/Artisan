package com.example.Artisan.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@Data
public class ShippingBillingDetail {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long Id;

    @Column
    private String city;
    @Column
    private String state;
    @Column
    private String line1;
    @Column
    private String line2;
    @Column
    private String postal_code;
    @Column
    private String country;
    @Column
    private String name;
    @Column
    private String billing_postal_code;
    @Column
    private String billing_country;
    @Column
    private String billing_name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, referencedColumnName = "order_id")
    private OrderDetail orderDetail;
}
