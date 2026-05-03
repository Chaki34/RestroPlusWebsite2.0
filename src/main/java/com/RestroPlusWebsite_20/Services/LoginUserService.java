package com.RestroPlusWebsite_20.Services;

import com.RestroPlusWebsite_20.Entities.UserEntity;
import com.RestroPlusWebsite_20.repos.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean validateUser(Long phoneno, String password) {

        // check if phone exists
        Optional<UserEntity> userOpt = userRepository.findByPhoneno(phoneno);
        if (userOpt.isEmpty()) {
            return false;
        }

        // check password
        UserEntity user = userOpt.get();
        return passwordEncoder.matches(password, user.getPassword());
    }
}


