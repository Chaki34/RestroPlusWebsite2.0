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

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

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

import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final RegisterUserService registerUserService;

    private final LoginResturentService loginservice;

    private final LoginUserService authService;

    private final RegisterResurentService service;

    public AuthController(
            RegisterUserService registerUserService,
            LoginResturentService loginservice,
            LoginUserService authService,
            RegisterResurentService service
    ) {

        this.registerUserService = registerUserService;
        this.loginservice = loginservice;
        this.authService = authService;
        this.service = service;
    }

    /*
     =========================================================
                        USER AUTH PAGE
     =========================================================
    */

    @GetMapping("/customer")
    public String showRegistrationForm(Model model) {

        model.addAttribute("user", new UserEntity());

        model.addAttribute("role", "User");

        return "user-auth";
    }

    /*
     =========================================================
                    RESTAURANT AUTH PAGE
     =========================================================
    */

    @GetMapping("/restaurant")
    public String authPageResturent(Model model) {

        model.addAttribute(
                "restaurant",
                new resturentEntity()
        );

        model.addAttribute(
                "role",
                "RESTAURANT-OWNER"
        );

        return "resturent-auth";
    }

    /*
     =========================================================
                        DRIVER AUTH PAGE
     =========================================================
    */

    @GetMapping("/partner")
    public String authPagePartner(

            @RequestParam(
                    required = false,
                    defaultValue = "partner"
            ) String role,

            Model model
    ) {

        model.addAttribute("role", role);

        return "partner-auth";
    }

    /*
     =========================================================
                        USER REGISTER
     =========================================================
    */

    @PostMapping("/user-register")
    public String register(
            RegisterRequest request,
            RedirectAttributes redirect
    ) {

        try {

            registerUserService.registerUser(request);

            redirect.addFlashAttribute(
                    "message",
                    "Account created successfully! Please login."
            );

            return "redirect:/auth/customer";

        } catch (Exception e) {

            redirect.addFlashAttribute(
                    "error",
                    e.getMessage()
            );

            return "redirect:/auth/customer";
        }
    }

    /*
     =========================================================
                        USER LOGIN
     =========================================================
    */

    @PostMapping("/user-login")
    public String loginUser(

            @RequestParam String username,

            @RequestParam String password,

            RedirectAttributes redirect,

            HttpSession session
    ) {

        try {

            Long phone = Long.parseLong(username);

            boolean valid =
                    authService.validateUser(phone, password);

            if (valid) {

                // SAVE SESSION
                session.setAttribute(
                        "loggedInPhone",
                        phone
                );

                // SPRING SECURITY AUTH
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(

                                phone.toString(),

                                null,

                                List.of(
                                        new SimpleGrantedAuthority(
                                                "ROLE_USER"
                                        )
                                )
                        );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(auth);

                // SAVE SECURITY CONTEXT
                session.setAttribute(
                        HttpSessionSecurityContextRepository
                                .SPRING_SECURITY_CONTEXT_KEY,

                        SecurityContextHolder.getContext()
                );

                return "redirect:/dashboard/user";
            }

            redirect.addFlashAttribute(
                    "error",
                    "Invalid phone number or password"
            );

            return "redirect:/auth/customer";

        } catch (Exception e) {

            redirect.addFlashAttribute(
                    "error",
                    "Login failed"
            );

            return "redirect:/auth/customer";
        }
    }

    /*
     =========================================================
                    RESTAURANT REGISTER
     =========================================================
    */

    @PostMapping("/restaurant-register")
    public String Resturentregister(

            ResturentRegisterRequest request,

            @RequestParam("imageFile")
            MultipartFile imageFile,

            RedirectAttributes redirect
    ) {

        try {

            // IMAGE SAVE
            if (!imageFile.isEmpty()) {

                String fileName =
                        StringUtils.cleanPath(
                                imageFile.getOriginalFilename()
                        );

                Path uploadPath =
                        Paths.get(
                                "src/main/resources/static/images/"
                        );

                if (!Files.exists(uploadPath)) {

                    Files.createDirectories(uploadPath);
                }

                try (
                        InputStream inputStream =
                                imageFile.getInputStream()
                ) {

                    Path filePath =
                            uploadPath.resolve(fileName);

                    Files.copy(
                            inputStream,
                            filePath,
                            StandardCopyOption.REPLACE_EXISTING
                    );

                    request.setImageUrl(
                            "/images/" + fileName
                    );
                }

            } else {

                request.setImageUrl(
                        "/images/default.jpg"
                );
            }

            service.registerRestro(request);

            redirect.addFlashAttribute(
                    "message",
                    "Account created successfully! Please login."
            );

            return "redirect:/auth/restaurant";

        } catch (Exception e) {

            redirect.addFlashAttribute(
                    "error",
                    e.getMessage()
            );

            return "redirect:/auth/restaurant";
        }
    }

    /*
     =========================================================
                    RESTAURANT LOGIN
     =========================================================
    */

    @PostMapping("/restaurant-login")
    public String loginrestro(

            @RequestParam String resturentRegistrationNo,

            @RequestParam String password,

            RedirectAttributes redirect,

            HttpSession session
    ) {

        try {

            boolean valid =
                    loginservice.validaterestro(
                            resturentRegistrationNo,
                            password
                    );

            if (valid) {

                // SAVE SESSION
                session.setAttribute(
                        "loggedInRegNo",
                        resturentRegistrationNo
                );

                // SPRING SECURITY AUTH
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(

                                resturentRegistrationNo,

                                null,

                                List.of(
                                        new SimpleGrantedAuthority(
                                                "ROLE_ADMIN"
                                        )
                                )
                        );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(auth);

                // SAVE SECURITY CONTEXT
                session.setAttribute(
                        HttpSessionSecurityContextRepository
                                .SPRING_SECURITY_CONTEXT_KEY,

                        SecurityContextHolder.getContext()
                );

                return "redirect:/dashboard/admin";
            }

            redirect.addFlashAttribute(
                    "error",
                    "Invalid registration no or password"
            );

            return "redirect:/auth/restaurant";

        } catch (Exception e) {

            redirect.addFlashAttribute(
                    "error",
                    "Login failed"
            );

            return "redirect:/auth/restaurant";
        }
    }

    /*
     =========================================================
                            LOGOUT
     =========================================================
    */

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        SecurityContextHolder.clearContext();

        session.invalidate();

        return "redirect:/";
    }
}