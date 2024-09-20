package com.example.Artisan.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class UserArtistRatingAssociationId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "id")
    private Long artistId;

    public UserArtistRatingAssociationId(Long userId, Long id) {
        this.userId = userId;
        this.artistId = id;
    }
}
