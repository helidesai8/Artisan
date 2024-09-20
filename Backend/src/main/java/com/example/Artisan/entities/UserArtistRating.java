package com.example.Artisan.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserArtistRating {
    @EmbeddedId
    private UserArtistRatingAssociationId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("artistId")
    @JoinColumn(name = "artist_id")
    private Artist artist;

    private float rating;

    private String comment;
    public UserArtistRating() {
        id = new UserArtistRatingAssociationId();
    }
}