package com.flavio.appointment_booking_api.service;

import com.flavio.appointment_booking_api.dto.auth.RegisterRequest;
import com.flavio.appointment_booking_api.entity.User;
import com.flavio.appointment_booking_api.enums.Role;
import com.flavio.appointment_booking_api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.CLIENT)
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }
}