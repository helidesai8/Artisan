package com.example.Artisan.responses;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProfileImageApiResponse {

    private String message;
    private boolean success;

    private String profileImageUrl;

    // Overloaded constructor with two parameters
    public ProfileImageApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
    // Overloaded constructor with three parameters
    public ProfileImageApiResponse(String message, boolean success,String profileImageUrl) {
        this.message = message;
        this.success = success;
        this.profileImageUrl = profileImageUrl;
    }


}