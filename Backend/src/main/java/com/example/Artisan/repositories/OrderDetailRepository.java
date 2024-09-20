package com.example.Artisan.repositories;

import com.example.Artisan.entities.OrderDetail;
import com.example.Artisan.entities.UserProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByUserEmail(String email);
}
