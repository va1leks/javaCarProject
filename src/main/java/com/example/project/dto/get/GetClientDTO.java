package com.example.project.dto.get;

import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class GetClientDTO {
    private Long id;
    private String name;
    private String phone;
    private Set<Long> interestedCars;
}
