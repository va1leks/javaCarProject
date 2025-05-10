package com.example.project.dto.create;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtRequest {

    private String phone;
    private String password;
}
