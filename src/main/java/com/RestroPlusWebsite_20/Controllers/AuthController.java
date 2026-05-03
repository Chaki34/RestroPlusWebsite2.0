package com.RestroPlusWebsite_20.Controllers;
import com.RestroPlusWebsite_20.DTOS.RegisterRequest;
import com.RestroPlusWebsite_20.DTOS.ResturentRegisterRequest;
import com.RestroPlusWebsite_20.Entities.UserEntity;
import com.RestroPlusWebsite_20.Entities.resturentEntity;
import com.RestroPlusWebsite_20.Services.LoginResturentService;
import com.RestroPlusWebsite_20.Services.LoginUserService;
import com.RestroPlusWebsite_20.Services.RegisterResurentService;
import com.RestroPlusWebsite_20.Services.RegisterUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final RegisterUserService registerUserService;

    private final LoginResturentService loginservice;

    private final LoginUserService authService;

   private final  RegisterResurentService service;

    public AuthController(RegisterUserService registerUserService, LoginResturentService loginservice, LoginUserService authService, RegisterResurentService service) {
        this.registerUserService = registerUserService;
        this.loginservice = loginservice;
        this.authService = authService;
        this.service = service;
    }


    // User login page
    @GetMapping("/customer")
    public String showRegistrationForm(Model model) {

        // form-binding object
        model.addAttribute("user", new UserEntity());

        // default role for UI badge
        model.addAttribute("role", "User");

        // Thymeleaf template name
        return "user-auth";
    }


    // Restaurant login page
    @GetMapping("/restaurant")
    public String authPageResturent(Model model){
        // form-binding object
        model.addAttribute("restaurant", new resturentEntity());

        // default role for UI badge
        model.addAttribute("role", "RESTAURANT-OWNER");

        // Thymeleaf template name
        return "resturent-auth";


    }

    // Partner/Driver login page
    @GetMapping("/partner")
    public String authPagePartner(
            @RequestParam(required = false, defaultValue = "partner") String role,
            Model model) {
        model.addAttribute("role", role);
        return "partner-auth"; // Thymeleaf template: partner-auth.html
    }

    // register set

    @PostMapping("/user-register")
    public String register(RegisterRequest request,
                           RedirectAttributes redirect) {

        try {
            UserEntity user = registerUserService.registerUser(request);

            redirect.addFlashAttribute("message",
                    "Account created successfully! Please login.");

            return "redirect:/auth/customer";

        } catch (Exception e) {

            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/customer"; // your register page
        }




    }

    @PostMapping("/user-login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            RedirectAttributes redirect,
                            HttpSession session) {  // <-- add session

        try {
            Long phone = Long.parseLong(username); // assuming login via phone only

            boolean valid = authService.validateUser(phone, password);


            if (valid) {
                // Store phone in session
                session.setAttribute("loggedInPhone", phone);

                // Redirect to dashboard
                return "redirect:/dashboard/user";
            } else {
                redirect.addFlashAttribute("error", "Invalid phone number or password");
                return "redirect:/auth/customer"; // login page
            }

        } catch (NumberFormatException e) {
            redirect.addFlashAttribute("error", "Invalid phone number format");
            return "redirect:/auth/customer";
        }
    }


    @PostMapping("/restaurant-register")
    public String Resturentregister(
            ResturentRegisterRequest request,
            @RequestParam("imageFile") MultipartFile imageFile,
            RedirectAttributes redirect) {

        try {
            // Save the uploaded image
            if (!imageFile.isEmpty()) {
                String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
                Path uploadPath = Paths.get("src/main/resources/static/images/");

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                try (InputStream inputStream = imageFile.getInputStream()) {
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                    // Set image URL for the DTO
                    request.setImageUrl("/images/" + fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Could not save image file: " + fileName);
                }
            } else {
                request.setImageUrl("/images/default.jpg");
            }

            // Register restaurant
            resturentEntity user = service.registerRestro(request);

            redirect.addFlashAttribute("message", "Account created successfully! Please login.");
            return "redirect:/auth/restaurant";

        } catch (Exception e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/restaurant";
        }
    }


    @PostMapping("/restaurant-login")
    public String loginrestro(@RequestParam String resturentRegistrationNo,
                            @RequestParam String password,
                            RedirectAttributes redirect,
                            HttpSession session) {  // <-- add session

        try {


            boolean valid = loginservice.validaterestro(resturentRegistrationNo, password);


            if (valid) {
                // Store phone in session
                session.setAttribute("loggedInRegNo", resturentRegistrationNo);

                // Redirect to dashboard
                return "redirect:/dashboard/admin";
            } else {
                redirect.addFlashAttribute("error", "Invalid regNo or password");
                return "redirect:/auth/restaurant"; // login page
            }

        } catch (NumberFormatException e) {
            redirect.addFlashAttribute("error", "Invalid registration no  format");
            return "redirect:/auth/restaurant";
        }
    }

}


