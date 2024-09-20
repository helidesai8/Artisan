package com.example.Artisan.entities.products;

import com.example.Artisan.entities.products.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_stories")
public class ProductStory {

    @Id
    private Long productId;

    @Column(length = 4000) // Adjust length according to your needs
    private String story;

    @OneToOne
    @MapsId
    @JoinColumn(name = "product_id")
    private Product product;

}
