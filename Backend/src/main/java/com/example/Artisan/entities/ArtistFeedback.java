package com.example.Artisan.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "artist_feedback")
public class ArtistFeedback {
    private static int minRating = 1;
    private static int maxRating = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @Column(length = 1000)
    private String feedback;

    @Column(name = "rating")
    private Integer rating;

    public void setRating(Integer rating) {
        if (rating >= minRating && rating <= maxRating) {
            this.rating = rating;
        } else {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
    }
}
