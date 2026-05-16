package com.RestroPlusWebsite_20.repos;




import org.springframework.data.jpa.repository.JpaRepository;

import com.RestroPlusWebsite_20.Entities.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByPhoneno(Long phoneno);

    boolean existsByPhoneno(Long phoneno);


}
