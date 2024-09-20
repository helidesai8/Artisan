package com.example.Artisan.DTOs;

import com.example.Artisan.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDTO {
    private Long artistId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String aboutMe;
    private String city;
    private ArtistStoryDTO story;
    private String profileImageUrl;
    private Role role;
}
