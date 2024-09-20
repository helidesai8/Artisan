package com.example.Artisan.entities;

import com.example.Artisan.entities.products.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long orderItemId;

    @ManyToOne()
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne(targetEntity= OrderDetail.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderDetail orderDetail;

    private int quantity;

    private BigDecimal amount;
}
