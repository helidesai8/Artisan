package com.example.Artisan.repositories;


import com.example.Artisan.entities.ArtistStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistStoryRepository extends JpaRepository<ArtistStory, Long> {
    ArtistStory findByArtistArtistId(Long artistId);
}