package com.example.Artisan.payloads;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginCredentials {
    @Email
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
}
