package com.flavio.appointment_booking_api.controller;

import com.flavio.appointment_booking_api.dto.auth.RegisterRequest;
import com.flavio.appointment_booking_api.entity.User;
import com.flavio.appointment_booking_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody @Valid RegisterRequest request) {
        return userService.register(request);
    }
}