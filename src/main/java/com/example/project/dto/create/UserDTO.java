package com.example.project.dto.create;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class UserDTO {
    private String password;
    @NotNull
    private String name;
    @NotNull
    private String phone;
}
