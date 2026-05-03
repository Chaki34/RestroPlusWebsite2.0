package com.RestroPlusWebsite_20.Controllers;

import com.RestroPlusWebsite_20.Entities.MenuCategory;
import com.RestroPlusWebsite_20.Entities.resturentEntity;
import com.RestroPlusWebsite_20.repos.MenuCategoryRepository;
import com.RestroPlusWebsite_20.repos.resturnetRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller

public class UserAcititvetiesController {

    private final resturnetRepository repo;
    private  final MenuCategoryRepository repository;
    public UserAcititvetiesController(resturnetRepository repo, MenuCategoryRepository repository) {
        this.repo = repo;
        this.repository = repository;
    }

    @GetMapping("/restaurant/{id}")
    public String showRestaurantDetail(@PathVariable Long id, Model model) {

        resturentEntity r = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant Not Found"));

        List<MenuCategory> categories =
                repository.findByRestaurantId(id);

        model.addAttribute("restaurant", r);
        model.addAttribute("categories", categories);


        return "restro-details";
    }

}
