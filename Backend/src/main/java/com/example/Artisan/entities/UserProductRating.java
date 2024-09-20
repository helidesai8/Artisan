package com.example.Artisan.entities;

import com.example.Artisan.entities.products.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserProductRating {
    @EmbeddedId
    private UserProductRatingAssociationId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    private float rating;

    private String comment;
    public UserProductRating() {
        id = new UserProductRatingAssociationId();
    }
}