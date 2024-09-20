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
public class UserDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String contactNumber;
    private  String streetAddress;
    private  String city;
    private  String state;
    private  String postalCode;
    private  String country;
    private  String profileImageUrl;
    private Role role;

}
