package com.example.Artisan.repositories;
import com.example.Artisan.entities.ArtistFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArtistFeedbackRepository extends JpaRepository<ArtistFeedback, Long> {
    List<ArtistFeedback> findByArtist_ArtistId(Long artistId);
}
