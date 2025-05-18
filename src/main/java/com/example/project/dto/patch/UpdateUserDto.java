package com.example.project.dto.patch;

import com.example.project.constant.Roles;
import com.example.project.dto.get.GetCarDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class UpdateUserDto {
    private String name;
    private String phone;
    private List<Roles> roles;
}
