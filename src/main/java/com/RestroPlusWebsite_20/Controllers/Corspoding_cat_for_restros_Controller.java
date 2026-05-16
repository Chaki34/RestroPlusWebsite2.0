package com.RestroPlusWebsite_20.Controllers;

import com.RestroPlusWebsite_20.Entities.MenuCategory;
import com.RestroPlusWebsite_20.Entities.MenuCategoryDetails;
import com.RestroPlusWebsite_20.Entities.resturentEntity;
import com.RestroPlusWebsite_20.repos.MenuCategoryDetailsRepository;
import com.RestroPlusWebsite_20.repos.MenuCategoryRepository;
import com.RestroPlusWebsite_20.repos.resturnetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class Corspoding_cat_for_restros_Controller {

    @Autowired
    private MenuCategoryRepository menuCategoryRepository;

    @Autowired
    private MenuCategoryDetailsRepository menuCategoryDetailsRepository;

    @Autowired
    private resturnetRepository restaurantRepository;

    @GetMapping("/restaurants")
    public String showRestaurantsByCategory(@RequestParam("category") String category,
                                            Model model) {

        List<MenuCategory> categories =
                menuCategoryRepository.findByCategoryName(category);

        List<resturentEntity> restaurants = categories.stream()
                .map(MenuCategory::getRestaurant)
                .distinct()
                .toList();

        List<MenuCategoryDetails> categoryDetailsList = categories.stream()
                .map(cat -> menuCategoryDetailsRepository
                        .findByMenuCategoryId(cat.getId())
                        .orElse(null))
                .filter(java.util.Objects::nonNull)
                .toList();

        System.out.println("Category Details Size = " + categoryDetailsList.size());



        model.addAttribute("restaurants", restaurants);
        model.addAttribute("category", category);
        model.addAttribute("categoryDetailsList", categoryDetailsList);

        return "cat-by-restaurants";
    }

    @GetMapping("/search")
    public String searchFoods(@RequestParam("query") String query,
                              Model model) {

        // 1. Find categories matching search
        List<MenuCategory> categories =
                menuCategoryRepository.findByCategoryNameContainingIgnoreCase(query);

        // 2. Restaurants
        List<resturentEntity> restaurants = categories.stream()
                .map(MenuCategory::getRestaurant)
                .distinct()
                .toList();

        // 3. Food details
        List<MenuCategoryDetails> categoryDetailsList = categories.stream()
                .map(cat -> menuCategoryDetailsRepository
                        .findByMenuCategoryId(cat.getId())
                        .orElse(null))
                .filter(java.util.Objects::nonNull)
                .toList();

        // 4. Send to SAME PAGE
        model.addAttribute("searchQuery", query);
        model.addAttribute("category", query);
        model.addAttribute("restaurants", restaurants);
        model.addAttribute("categoryDetailsList", categoryDetailsList);

        return "cat-by-restaurants";   // 👈 your big HTML page
    }
}
