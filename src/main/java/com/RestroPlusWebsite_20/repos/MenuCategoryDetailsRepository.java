package com.RestroPlusWebsite_20.repos;

import com.RestroPlusWebsite_20.Entities.MenuCategoryDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuCategoryDetailsRepository
        extends JpaRepository<MenuCategoryDetails, Long> {

    Optional<MenuCategoryDetails>
    findByMenuCategoryId(Long categoryId);
}
