package com.RestroPlusWebsite_20.Controllers;

import com.RestroPlusWebsite_20.DTOS.LocationDTO;
import com.RestroPlusWebsite_20.Entities.UserEntity;
import com.RestroPlusWebsite_20.repos.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/update")
    public String updateLocation(
            @RequestBody LocationDTO dto,
            HttpSession session
    ) {

        try {

            System.out.println("========== LOCATION API ==========");

            // CHECK SESSION ID
            System.out.println("SESSION ID : " + session.getId());

            // GET PHONE FROM SESSION
            Object phoneObj =
                    session.getAttribute("loggedInPhone");

            System.out.println("SESSION PHONE : " + phoneObj);

            // CHECK LOGIN
            if(phoneObj == null){

                System.out.println("USER NOT LOGGED IN");

                return "User not logged in";
            }

            Long phone = (Long) phoneObj;

            // DTO DEBUG
            System.out.println("CITY : " + dto.getCity());

            System.out.println("LATITUDE : " + dto.getLatitude());

            System.out.println("LONGITUDE : " + dto.getLongitude());

            // FIND USER
            Optional<UserEntity> optionalUser =
                    userRepository.findByPhoneno(phone);

            System.out.println("USER FOUND : " + optionalUser.isPresent());

            if(optionalUser.isEmpty()){

                return "User not found";
            }

            UserEntity user = optionalUser.get();

            // BEFORE UPDATE
            System.out.println("OLD CITY : " + user.getCity());

            // UPDATE
            user.setCity(dto.getCity());

            user.setLatitude(dto.getLatitude());

            user.setLongitude(dto.getLongitude());

            // SAVE
            UserEntity savedUser =
                    userRepository.save(user);

            // AFTER SAVE
            System.out.println("NEW CITY : " + savedUser.getCity());

            System.out.println("LOCATION SAVED SUCCESSFULLY");

            return "Location Updated Successfully";
        }
        catch (Exception e){

            System.out.println("ERROR OCCURRED");

            e.printStackTrace();

            return "Error Updating Location : " + e.getMessage();
        }
    }
}