package com.RestroPlusWebsite_20.Controllers;

import com.RestroPlusWebsite_20.Entities.MenuCategory;
import com.RestroPlusWebsite_20.Entities.UserEntity;
import com.RestroPlusWebsite_20.Entities.resturentEntity;
import com.RestroPlusWebsite_20.repos.MenuCategoryRepository;
import com.RestroPlusWebsite_20.repos.UserRepository;
import com.RestroPlusWebsite_20.repos.resturnetRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private resturnetRepository resturnetRepository;

    @Autowired
    private MenuCategoryRepository menuCategoryRepository;

    /*
     =========================================================
                        USER DASHBOARD
     =========================================================
    */

    @GetMapping("/user")
    public String userDashboard(
            HttpSession session,
            Authentication authentication,
            Model model
    ) {

        // Spring Security Protection
        if (authentication == null ||
                !authentication.isAuthenticated()) {

            return "redirect:/auth/customer";
        }

        // Session timeout
        session.setMaxInactiveInterval(60 * 30);

        // Existing Session Support
        Long phoneNo =
                (Long) session.getAttribute("loggedInPhone");

        if (phoneNo == null) {

            session.invalidate();

            return "redirect:/auth/customer";
        }

        Optional<UserEntity> userOpt =
                userRepository.findByPhoneno(phoneNo);

        if (userOpt.isEmpty()) {

            session.invalidate();

            return "redirect:/auth/customer";
        }

        UserEntity user = userOpt.get();

        // USER INFO
        model.addAttribute(
                "userName",
                user.getUsername()
        );

        model.addAttribute(
                "userPhone",
                user.getPhoneno()
        );

        // RESTAURANTS
        model.addAttribute(
                "restaurants",
                resturnetRepository.findAll()
        );

        // FOODS
        model.addAttribute(
                "foods",
                menuCategoryRepository.findAll()
        );

        // ROLE
        model.addAttribute(
                "role",
                getRole(authentication)
        );

        return "user-dashboard";
    }

    /*
     =========================================================
                        ADMIN DASHBOARD
     =========================================================
    */

    @GetMapping("/admin")
    public String adminDashboard(
            HttpSession session,
            Authentication authentication,
            Model model
    ) {

        // Spring Security Protection
        if (authentication == null ||
                !authentication.isAuthenticated()) {

            return "redirect:/auth/restaurant";
        }

        // Session timeout
        session.setMaxInactiveInterval(60 * 30);

        // Existing Session Support
        String regNo =
                (String) session.getAttribute("loggedInRegNo");

        if (regNo == null || regNo.isBlank()) {

            session.invalidate();

            return "redirect:/auth/restaurant";
        }

        Optional<resturentEntity> restOpt =
                resturnetRepository
                        .findByResturentRegistrationNo(regNo);

        if (restOpt.isEmpty()) {

            session.invalidate();

            return "redirect:/auth/restaurant";
        }

        resturentEntity restaurant = restOpt.get();

        // RESTAURANT INFO
        model.addAttribute(
                "restaurant",
                restaurant
        );

        model.addAttribute(
                "userName",
                restaurant.getResturentname()
        );

        model.addAttribute(
                "imageUrl",
                restaurant.getImageUrl() != null
                        ? restaurant.getImageUrl()
                        : "/images/default.jpg"
        );

        model.addAttribute(
                "registrationNo",
                restaurant.getResturentRegistrationNo()
        );

        // MENU CATEGORIES
        List<MenuCategory> categories =
                menuCategoryRepository
                        .findByRestaurant_ResturentRegistrationNo(regNo);

        model.addAttribute(
                "categories",
                categories
        );

        // ROLE
        model.addAttribute(
                "role",
                getRole(authentication)
        );

        return "restro-dashboard";
    }

    /*
     =========================================================
                        DRIVER DASHBOARD
     =========================================================
    */

    @GetMapping("/drivers")
    public String driverDashboard(
            HttpSession session,
            Authentication authentication,
            Model model
    ) {

        // Spring Security Protection
        if (authentication == null ||
                !authentication.isAuthenticated()) {

            return "redirect:/login";
        }

        // Session timeout
        session.setMaxInactiveInterval(60 * 30);

        model.addAttribute(
                "role",
                getRole(authentication)
        );

        return "driver-dashboard";
    }

    /*
     =========================================================
                        ROLE HELPER
     =========================================================
    */

    private String getRole(Authentication authentication) {

        Collection<? extends GrantedAuthority> authorities =
                authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {

            return authority.getAuthority();
        }

        return "ROLE_GUEST";
    }
}