package com.example.Artisan.exceptions;

public class ArtistNotFoundException extends RuntimeException {
    public ArtistNotFoundException(Long id) {
        super("Artist not found with id: " + id);
    }
    public ArtistNotFoundException() {
        super("Artist not found");
    }
}
