package com.example.Artisan.entities.products;

import com.example.Artisan.entities.Artist;
import com.example.Artisan.DTOs.products.ProductDTO;
import com.example.Artisan.entities.UserProductRating;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Table(name = "products")
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "isActive")
    private Boolean isActive = true;

    @ManyToOne(targetEntity=Artist.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductImage> imageUrls = new HashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<UserProductRating> userProductRatings;

    @OneToOne(mappedBy = "product", fetch = FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval = true)
    private ProductInventory productInventory;
}
