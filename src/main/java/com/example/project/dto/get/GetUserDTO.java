package com.example.project.dto.get;

import java.util.List;
import java.util.Set;

import com.example.project.constant.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class GetUserDTO {
    private Long id;
    private String name;
    private String phone;
    private Set<GetCarDTO> interestedCars;
    private List<Roles> roles;
}
