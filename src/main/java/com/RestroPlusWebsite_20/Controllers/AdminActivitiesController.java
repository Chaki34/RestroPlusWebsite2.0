package com.RestroPlusWebsite_20.Controllers;

import com.RestroPlusWebsite_20.Entities.resturentEntity;
import com.RestroPlusWebsite_20.repos.resturnetRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@Controller
public class AdminActivitiesController {

    @Autowired
    private resturnetRepository resturnetRepository;

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

}

