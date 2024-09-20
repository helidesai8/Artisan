package com.example.Artisan.repositories;

import com.example.Artisan.entities.ArtistProfileImage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistProfileImageRepository extends JpaRepository<ArtistProfileImage, Long> {
    ArtistProfileImage findByArtistArtistId(Long artistId);
}
