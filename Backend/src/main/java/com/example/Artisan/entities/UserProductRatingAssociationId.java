package com.example.Artisan.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class UserProductRatingAssociationId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "id")
    private Long productId;

    public UserProductRatingAssociationId(Long userId, Long id) {
        this.userId = userId;
        this.productId = id;
    }
}
