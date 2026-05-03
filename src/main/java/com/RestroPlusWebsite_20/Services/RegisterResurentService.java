package com.RestroPlusWebsite_20.Services;

import com.RestroPlusWebsite_20.DTOS.ResturentRegisterRequest;
import com.RestroPlusWebsite_20.Entities.resturentEntity;
import com.RestroPlusWebsite_20.repos.resturnetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service

public class RegisterResurentService {

    private final resturnetRepository repo;
    private final PasswordEncoder passwordEncoder;

    public RegisterResurentService(resturnetRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public resturentEntity registerRestro(ResturentRegisterRequest req) {

        if (repo.existsByResturentRegistrationNo(req.getResturentRegistrationNo())) {
            throw new RuntimeException("Registration number already registered");
        }

        resturentEntity restro = new resturentEntity();
        restro.setResturentname(req.getResturentname());
        restro.setOwnername(req.getOwnername());
        restro.setPassword(passwordEncoder.encode(req.getPassword()));
        restro.setAddress(req.getAddress());
        restro.setResturentRegistrationNo(req.getResturentRegistrationNo());
        restro.setRole("ROLE_RESTAURANT-ADMIN");

        // NEW FIELDS
        restro.setType(req.getType() != null ? req.getType() : "General");
        restro.setImageUrl(req.getImageUrl() != null ? req.getImageUrl() : "/images/default.jpg");
        restro.setRating(0.0);

        return repo.save(restro);
    }

}
