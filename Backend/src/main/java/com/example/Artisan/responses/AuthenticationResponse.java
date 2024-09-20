package com.example.Artisan.responses;

import com.example.Artisan.DTOs.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;
    private String  message;
}
