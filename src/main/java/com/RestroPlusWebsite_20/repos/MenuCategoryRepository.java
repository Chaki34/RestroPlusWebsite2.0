package com.RestroPlusWebsite_20.repos;





import com.RestroPlusWebsite_20.Entities.MenuCategory;
import com.RestroPlusWebsite_20.Entities.resturentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {

    // Optional: fetch all categories for a restaurant
    List<MenuCategory> findByRestaurant_ResturentRegistrationNo(String regNo);

    List<MenuCategory> findByRestaurantId(Long restaurantId);

    void deleteByRestaurant(resturentEntity restaurant);
}
