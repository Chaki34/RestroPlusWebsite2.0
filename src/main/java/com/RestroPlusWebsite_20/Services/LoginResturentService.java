package com.RestroPlusWebsite_20.Services;

import com.RestroPlusWebsite_20.Entities.UserEntity;
import com.RestroPlusWebsite_20.Entities.resturentEntity;
import com.RestroPlusWebsite_20.repos.UserRepository;
import com.RestroPlusWebsite_20.repos.resturnetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Service

public class LoginResturentService {

    private final resturnetRepository Repository;
    private final PasswordEncoder passwordEncoder;

    public LoginResturentService(resturnetRepository repository, PasswordEncoder passwordEncoder) {
        Repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean validaterestro(String resturentRegistrationNo, String password) {

        // check if phone exists
        Optional<resturentEntity> userOpt = Repository.findByResturentRegistrationNo(resturentRegistrationNo);
        if (userOpt.isEmpty()) {
            return false;
        }

        // check password
        resturentEntity user = userOpt.get();
        return passwordEncoder.matches(password, user.getPassword());
    }

}
