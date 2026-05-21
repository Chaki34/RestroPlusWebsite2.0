package com.RestroPlusWebsite_20.Controllers;

import com.RestroPlusWebsite_20.DTOS.MenuRequest;
import com.RestroPlusWebsite_20.Entities.MenuCategory;
import com.RestroPlusWebsite_20.Entities.resturentEntity;
import com.RestroPlusWebsite_20.repos.MenuCategoryRepository;
import com.RestroPlusWebsite_20.repos.resturnetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/restaurant")
public class MenuController {

    @Autowired
    private resturnetRepository resturnetRepository;

    @Autowired
    private MenuCategoryRepository menuCategoryRepository;

    @Transactional
    @PostMapping("/save-categories")
    public ResponseEntity<?> saveCategories(
            @RequestBody MenuRequest request) {

        System.out.println("🚀 Received MenuRequest: " + request);

        if (request.getRestaurantRegNo() == null ||
                request.getRestaurantRegNo().isBlank()) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "status", "error",
                            "message", "Restaurant regNo missing"
                    ));
        }

        String regNo = request.getRestaurantRegNo().trim();

        resturentEntity restaurant =
                resturnetRepository
                        .findByResturentRegistrationNo(regNo)
                        .orElse(null);

        if (restaurant == null) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "status", "error",
                            "message", "Restaurant not found"
                    ));
        }

        List<String> incomingCategories =
                request.getCategories();

        if (incomingCategories == null ||
                incomingCategories.isEmpty()) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "status", "error",
                            "message", "No categories selected"
                    ));
        }

        // Existing categories of this restaurant
        List<MenuCategory> existingCategories =
                menuCategoryRepository.findByRestaurant(
                        restaurant
                );

        int inserted = 0;

        for (String categoryName : incomingCategories) {

            if (categoryName == null ||
                    categoryName.trim().isEmpty()) {
                continue;
            }

            String trimmedName = categoryName.trim();

            boolean alreadyExists =
                    existingCategories.stream()
                            .anyMatch(c ->
                                    c.getCategoryName()
                                            .equalsIgnoreCase(trimmedName)
                            );

            if (!alreadyExists) {

                MenuCategory category =
                        new MenuCategory();

                category.setCategoryName(trimmedName);
                category.setRestaurant(restaurant);

                menuCategoryRepository.save(category);

                inserted++;

                System.out.println(
                        "✅ Added new category: "
                                + trimmedName
                );
            }
        }

        System.out.println(
                "🎉 New categories inserted: "
                        + inserted
        );

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "inserted", inserted,
                        "totalCategories",
                        menuCategoryRepository
                                .findByRestaurant(restaurant)
                                .size()
                )
        );
    }
}
