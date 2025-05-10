package com.example.project.service.impl;

import com.example.project.dto.create.JwtRequest;
import com.example.project.dto.create.RegistrationUserDto;
import com.example.project.exception.AppError;
import com.example.project.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServise {

    private final UserServiceImpl userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> Login(JwtRequest jwtRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getPhone(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new AppError(HttpStatus.UNAUTHORIZED, "Неверный логин или пароль"));
        }

        UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getPhone());
        String token = jwtTokenUtils.generateToken(userDetails);

        // Создаем объект для ответа с токеном
        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<AppError> Register(RegistrationUserDto registrationUserDto)
    {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED,"password and confirm password do not match"), HttpStatus.UNAUTHORIZED);
        }
        if (userService.findUserByPhone(registrationUserDto.getPhone()).isPresent()) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED,"has user with this phone number"), HttpStatus.UNAUTHORIZED);
        }

        userService.createNewUser(registrationUserDto);
        return null;
    }
}
