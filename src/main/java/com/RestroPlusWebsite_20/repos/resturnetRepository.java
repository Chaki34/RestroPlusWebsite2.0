package com.RestroPlusWebsite_20.repos;

import com.RestroPlusWebsite_20.Entities.resturentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface resturnetRepository extends JpaRepository<resturentEntity, Long> {
    Optional<resturentEntity> findByResturentRegistrationNo(String resturentRegistrationNo);
    boolean existsByResturentRegistrationNo(String resturentRegistrationNo);
}
