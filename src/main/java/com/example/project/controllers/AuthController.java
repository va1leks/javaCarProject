package com.example.project.controllers;

import com.example.project.dto.create.JwtRequest;
import com.example.project.dto.create.RegistrationUserDto;
import com.example.project.service.impl.AuthServise;
import com.example.project.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class AuthController {
    private final UserServiceImpl userService;
    private final AuthServise authServise;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken (@RequestBody JwtRequest jwtRequest) {
        log.info("=-=-=-=-=-=-=-=-===============-=-=-= {}", jwtRequest.toString());
        return  authServise.Login(jwtRequest);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registerUser (@RequestBody RegistrationUserDto registrationUserDto) {
        return ResponseEntity.ok(authServise.Register(registrationUserDto));
    }
}
