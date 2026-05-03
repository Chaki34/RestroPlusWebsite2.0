package com.RestroPlusWebsite_20.Controllers;





import com.RestroPlusWebsite_20.Entities.UserEntity;
import com.RestroPlusWebsite_20.Entities.resturentEntity;
import com.RestroPlusWebsite_20.repos.MenuCategoryRepository;
import com.RestroPlusWebsite_20.repos.UserRepository;
import com.RestroPlusWebsite_20.repos.resturnetRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/user")
    public String userDashboard(HttpSession session, Model model) {

        Long phoneNo = (Long) session.getAttribute("loggedInPhone");
        String userName = "Guest";

        // If user is logged in, fetch username
        if (phoneNo != null) {
            Optional<UserEntity> userOpt = userRepository.findByPhoneno(phoneNo);
            if (userOpt.isPresent()) {
                userName = userOpt.get().getUsername();
            }
        }

        // Set session timeout (30 minutes)
        session.setMaxInactiveInterval(60 * 30);

        // Send username to dashboard
        model.addAttribute("userName", userName);

        // Load restaurants
        model.addAttribute("restaurants", resturnetRepository.findAll());

        // ✅ Load menu categories (foods)
        model.addAttribute("foods", menuCategoryRepository.findAll());

        return "user-dashboard";
    }



    @GetMapping("/admin")
    public String adminDashboard(HttpSession session, Model model) {

        // Get registration number from session
        String regNo = (String) session.getAttribute("loggedInRegNo");

        // Extend session timeout to 30 mins
        session.setMaxInactiveInterval(60 * 30);

        // Default values
        String userName = "Owner";
        String imageUrl = "/images/default.jpg";

        if (regNo != null) {
            // Fetch restaurant by registration number
            Optional<resturentEntity> restOpt = resturnetRepository.findByResturentRegistrationNo(regNo);

            if (restOpt.isPresent()) {
                resturentEntity rest = restOpt.get();
                userName = rest.getResturentname();
                imageUrl = rest.getImageUrl() != null ? rest.getImageUrl() : "/images/default.jpg";
            }
        }

        model.addAttribute("userName", userName); // ✅ this is important
        return "restro-dashboard";
    }


    @GetMapping("/drivers")
    public String DriverDashbord(){
        return "driver-dashboard";
    }
}
