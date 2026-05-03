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
    public ResponseEntity<?> saveCategories(@RequestBody MenuRequest request) {

        System.out.println("🚀 Received MenuRequest: " + request);

        // 🔴 Validate regNo
        if (request.getRestaurantRegNo() == null || request.getRestaurantRegNo().isBlank()) {
            System.out.println("❌ Restaurant regNo is missing");
            return ResponseEntity.badRequest()
                    .body(Map.of("status", "error", "message", "Restaurant regNo missing"));
        }

        String regNo = request.getRestaurantRegNo().trim();
        System.out.println("🔹 Looking for restaurant with regNo: " + regNo);

        // 🔴 Fetch restaurant
        Optional<resturentEntity> restOpt = resturnetRepository.findByResturentRegistrationNo(regNo);

        if (restOpt.isEmpty()) {
            System.out.println("❌ Restaurant not found for regNo: " + regNo);
            return ResponseEntity.badRequest()
                    .body(Map.of("status", "error", "message", "Restaurant not found"));
        }

        resturentEntity restaurant = restOpt.get();
        System.out.println("✅ Found restaurant: " + restaurant.getResturentname());

        // 🔴 Validate categories
        List<String> categories = request.getCategories();
        if (categories == null || categories.isEmpty()) {
            System.out.println("❌ No categories selected");
            return ResponseEntity.badRequest()
                    .body(Map.of("status", "error", "message", "No categories selected"));
        }

        System.out.println("🔹 Categories to save: " + categories);

        // Remove old categories before saving new ones
        menuCategoryRepository.deleteByRestaurant(restaurant);
        System.out.println("🗑️ Deleted old categories for restaurant: " + restaurant.getResturentname());

        int savedCount = 0;

        for (String cat : categories) {
            if (cat == null || cat.trim().isEmpty()) {
                System.out.println("⚠️ Skipping empty category");
                continue;
            }

            MenuCategory mc = new MenuCategory();
            mc.setCategoryName(cat.trim());
            mc.setRestaurant(restaurant);

            menuCategoryRepository.save(mc);
            savedCount++;
            System.out.println("✅ Saved category: " + cat.trim());
        }

        System.out.println("🎉 Total categories saved: " + savedCount);

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "savedCategories", savedCount
                )
        );
    }
}
