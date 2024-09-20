package com.example.Artisan.repositories;

import com.example.Artisan.entities.Artist;
import com.example.Artisan.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long>, ArtistInsightRepository {
    Artist findByEmail(String email);
}
