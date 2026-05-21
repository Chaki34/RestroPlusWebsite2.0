package com.RestroPlusWebsite_20.Controllers;

import com.RestroPlusWebsite_20.Entities.MenuCategory;
import com.RestroPlusWebsite_20.Entities.MenuCategoryDetails;
import com.RestroPlusWebsite_20.Entities.resturentEntity;
import com.RestroPlusWebsite_20.repos.MenuCategoryDetailsRepository;
import com.RestroPlusWebsite_20.repos.MenuCategoryRepository;
import com.RestroPlusWebsite_20.repos.ShoppingCartRepository;
import com.RestroPlusWebsite_20.repos.resturnetRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@Controller
public class AdminActivitiesController {

    @Autowired
    private resturnetRepository resturnetRepository;

    @Autowired
    private MenuCategoryRepository menuCategoryRepository;

    @Autowired
    private MenuCategoryDetailsRepository menuCategoryDetailsRepository;

    @Autowired
    private resturnetRepository repository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;


    @GetMapping("/set-manu")
    public String showSetMenuPage(HttpSession session, Model model) {

        String regNo = (String) session.getAttribute("loggedInRegNo");
        // Extend session timeout to 30 mins
        session.setMaxInactiveInterval(60 * 30);


        if (regNo == null || regNo.isBlank()) {
            return "redirect:/auth/restaurant"; // not logged in
        }

        Optional<resturentEntity> restOpt = resturnetRepository.findByResturentRegistrationNo(regNo);

        if (restOpt.isEmpty()) {
            session.invalidate();
            return "redirect:/restro-login";
        }

        resturentEntity rest = restOpt.get();

        // Pass owner info to Thymeleaf
        model.addAttribute("loggedInRegNo", regNo);
        model.addAttribute("ownerName", rest.getResturentname());
        model.addAttribute("ownerImage", rest.getImageUrl() != null ? rest.getImageUrl() : "/images/default.jpg");



        return "set-menu";
    }


    @PostMapping("/upload-category-image/{id}")
    public String uploadCategoryImage(
            @PathVariable Long id,
            @RequestParam("imageFile") MultipartFile file) {

        try {

            // FIND CATEGORY
            MenuCategory category =
                    menuCategoryRepository.findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException("Category not found"));

            // CHECK EMPTY FILE
            if (file.isEmpty()) {
                return "redirect:/dashbaord/admin";
            }

            // CREATE UPLOAD DIRECTORY
            String uploadDir =
                    "src/main/resources/static/images/categories/";

            File uploadPath = new File(uploadDir);

            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            // GENERATE UNIQUE FILE NAME
            String fileName =
                    System.currentTimeMillis() + "_"
                            + file.getOriginalFilename();

            // SAVE FILE
            Path path = Paths.get(uploadDir + fileName);

            Files.copy(
                    file.getInputStream(),
                    path,
                    StandardCopyOption.REPLACE_EXISTING
            );

            // SAVE IMAGE URL INTO DATABASE
            category.setImageUrl("/images/categories/" + fileName);

            menuCategoryRepository.save(category);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/dashboard/admin";
    }

    @GetMapping("/category/delete/{id}")
    @Transactional
    public String deleteCategory(
            @PathVariable Long id) {

        try {

            MenuCategory category =
                    menuCategoryRepository.findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Category not found"));

            // delete image
            if (category.getImageUrl() != null &&
                    !category.getImageUrl().isBlank()) {

                File imageFile =
                        new File(
                                "src/main/resources/static"
                                        + category.getImageUrl()
                        );

                if (imageFile.exists()) {
                    imageFile.delete();
                }
            }

            // find menu details
            Optional<MenuCategoryDetails> detailsOpt =
                    menuCategoryDetailsRepository
                            .findByMenuCategoryId(id);

            if (detailsOpt.isPresent()) {

                MenuCategoryDetails details =
                        detailsOpt.get();

                // delete cart references first
                shoppingCartRepository
                        .deleteByMenuCategoryDetails(
                                details
                        );

                // delete menu item
                menuCategoryDetailsRepository
                        .delete(details);
            }

            // finally delete category
            menuCategoryRepository
                    .delete(category);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return "redirect:/dashboard/admin";
    }

    @GetMapping("/category/details/{id}")
    public String showCategoryDetails(
            @PathVariable Long id,
            Model model) {

        MenuCategory category =
                menuCategoryRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Category not found"));

        MenuCategoryDetails details =menuCategoryDetailsRepository
                        .findByMenuCategoryId(id)
                        .orElse(new MenuCategoryDetails());

        if (details.getMenuCategory() == null) {
            details.setMenuCategory(category);
        }

        model.addAttribute("category", category);
        model.addAttribute("details", details);

        return "category-details";
    }

    @PostMapping("/category/details/save")
    public String saveCategoryDetails(
            @ModelAttribute MenuCategoryDetails details,
            @RequestParam Long categoryId
    ) {

        MenuCategory category =
                menuCategoryRepository.findById(categoryId)
                        .orElseThrow(() -> new RuntimeException("Category not found"));




        MenuCategoryDetails existing =
                menuCategoryDetailsRepository
                        .findByMenuCategoryId(categoryId)
                        .orElse(new MenuCategoryDetails());

        // IMPORTANT FIX: SET BOTH RELATIONS
        existing.setMenuCategory(category);
        if (category.getRestaurant() == null) {
            throw new RuntimeException("Category has no restaurant linked");
        }

        existing.setRestaurant(category.getRestaurant()); // 🔥 THIS IS KEY

        // DATA COPY
        existing.setDescription(details.getDescription());
        existing.setHowItIsMade(details.getHowItIsMade());
        existing.setIngredients(details.getIngredients());
        existing.setPrice(details.getPrice());
        existing.setRating(details.getRating());

        existing.setSizeSmallPrice(details.getSizeSmallPrice());
        existing.setSizeMediumPrice(details.getSizeMediumPrice());
        existing.setSizeLargePrice(details.getSizeLargePrice());

        existing.setExtraSpicy(details.getExtraSpicy());
        existing.setExtraCheese(details.getExtraCheese());

        existing.setVeg(details.getVeg());
        existing.setNonVeg(details.getNonVeg());

        existing.setPreparationTime(details.getPreparationTime());
        existing.setAvailable(details.getAvailable());

        menuCategoryDetailsRepository.save(existing);

        return "redirect:/category/details/" + categoryId;
    }

    @GetMapping("/upload-menu-card")
    public String uploadPage() {
        return "upload-menu-card";
    }

    @PostMapping("/save-menu-card")
    public String saveMenuCard(
            @RequestParam("restaurantId") Long restaurantId,
            @RequestParam("image") MultipartFile file
    ) throws IOException {

        Optional<resturentEntity> optional =
                resturnetRepository.findById(restaurantId);

        if (optional.isPresent()) {

            resturentEntity restaurant = optional.get();

            // Upload folder
            String uploadDir =
                    "C:/Users/debma/OneDrive/Desktop/Spring Projects/RestroPlusWebsite2.0/src/main/resources/static/images/menu-cards/";


            File dir = new File(uploadDir);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filename = file.getOriginalFilename();

            file.transferTo(
                    new File(uploadDir + filename)
            );

            // SAVE MENU PATH INSIDE SAME TABLE
            restaurant.setMenuCard("/images/menu-cards/" + filename);

            resturnetRepository.save(restaurant);
        }

        return "redirect:/dashboard/admin";
    }

    @PostMapping("/status")
    @ResponseBody
    public String updateStatus(

            @RequestBody Map<String, Boolean> body,

            HttpSession session
    ) {

        // GET LOGGED IN RESTAURANT

        String regNo =
                (String) session.getAttribute(
                        "loggedInRegNo"
                );

        if (regNo == null || regNo.isBlank()) {

            return "Session expired";
        }

        // FIND RESTAURANT

        Optional<resturentEntity> optional =
                repository.findByResturentRegistrationNo(
                        regNo
                );

        if (optional.isEmpty()) {

            return "Restaurant not found";
        }

        // GET RESTAURANT

        resturentEntity restaurant =
                optional.get();

        // GET ACTIVE STATUS FROM REQUEST

        Boolean active =
                body.get("active");



        // SAVE AS 1 OR 0

        Boolean activestatus =
                body.get("active");

        restaurant.setActive(
                Boolean.TRUE.equals(activestatus)
        );
        // SAVE TO DATABASE

        repository.save(restaurant);

        return "Restaurant status updated successfully";
    }




}

