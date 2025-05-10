package com.example.project.dto.create;

import lombok.Data;

@Data

public class RegistrationUserDto {
        private String phone;
        private String password;
        private String confirmPassword;
        private String name;
}
