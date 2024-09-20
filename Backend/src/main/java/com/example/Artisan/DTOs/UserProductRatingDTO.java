
package com.example.Artisan.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProductRatingDTO {
    private float rating;
    private String comment;
    private String firstName;
    private String lastName;
}
