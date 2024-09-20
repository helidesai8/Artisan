package com.example.Artisan.services.profile;

import com.example.Artisan.DTOs.ArtistDTO;

import java.util.List;



public interface ArtistProfileService {


    ArtistDTO update(ArtistDTO artist, Long artistId);
    ArtistDTO getArtistDetails(Long artistId);



}