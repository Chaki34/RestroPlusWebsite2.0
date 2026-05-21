package com.RestroPlusWebsite_20.repos;





import com.RestroPlusWebsite_20.Entities.MenuCategory;
import com.RestroPlusWebsite_20.Entities.resturentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {

    List<MenuCategory> findByRestaurant_ResturentRegistrationNo(String regNo);

    List<MenuCategory> findByRestaurantId(Long restaurantId);

    List<MenuCategory> findByRestaurant(resturentEntity restaurant);

    List<MenuCategory> findByCategoryName(String categoryName);

    List<MenuCategory> findByCategoryNameContainingIgnoreCase(String name);
}
