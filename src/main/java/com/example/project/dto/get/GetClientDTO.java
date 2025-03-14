package com.example.project.dto.get;

import lombok.Builder;
import lombok.Data;

import java.util.Set;
@Builder
@Data
public class GetClientDTO {
    private Long id;
    private String name;
    private String phone;
    private Set<Long> interestedCars ;
}
