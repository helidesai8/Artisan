package com.example.Artisan.repositories;



import com.example.Artisan.entities.ArtistProfileImage;
import com.example.Artisan.entities.UserProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileImageRepository extends JpaRepository<UserProfileImage, Long> {
    UserProfileImage findByUserUserId(Long userId);

}

