package com.example.Artisan.repositories;

import com.example.Artisan.entities.ArtistStory;
import com.example.Artisan.entities.ArtistStoryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistStoryImageRepository extends JpaRepository<ArtistStoryImage, Long> {
    List<ArtistStoryImage> findByArtistStoryId(Long artistStoryId);
}
