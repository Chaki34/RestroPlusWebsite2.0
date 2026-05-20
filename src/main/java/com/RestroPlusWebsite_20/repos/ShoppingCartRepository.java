package com.RestroPlusWebsite_20.repos;

import com.RestroPlusWebsite_20.Entities.MenuCategoryDetails;
import com.RestroPlusWebsite_20.Entities.ShoppingCart;
import com.RestroPlusWebsite_20.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShoppingCartRepository
        extends JpaRepository<ShoppingCart, Long> {

    List<ShoppingCart> findByUser(UserEntity user);

    Optional<ShoppingCart> findByUserAndMenuCategoryDetails(
            UserEntity user,
            MenuCategoryDetails details
    );
}