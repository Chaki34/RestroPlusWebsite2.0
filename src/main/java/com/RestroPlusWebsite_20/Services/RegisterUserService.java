package com.RestroPlusWebsite_20.Services;



import com.RestroPlusWebsite_20.DTOS.RegisterRequest;
import com.RestroPlusWebsite_20.Entities.UserEntity;
import com.RestroPlusWebsite_20.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service

public class RegisterUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity registerUser(RegisterRequest req) {

        if (userRepository.existsByPhoneno(req.getPhoneno())) {
            throw new RuntimeException("Phone already registered");
        }

        UserEntity user = new UserEntity();

        user.setUsername(req.getUsername());
        user.setPhoneno(req.getPhoneno());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole("ROLE_USER");

        return userRepository.save(user);
    }


}
