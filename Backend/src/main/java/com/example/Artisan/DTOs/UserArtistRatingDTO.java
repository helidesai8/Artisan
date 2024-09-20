
package com.example.Artisan.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserArtistRatingDTO {
    private float rating;
    private String comment;
    private String firstName;
    private String lastName;
}
