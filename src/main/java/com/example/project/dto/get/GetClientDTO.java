package com.example.project.dto.get;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class GetClientDTO {
    private Long id;
    private String name;
    private String phone;
    private Set<Long> interestedCars;
}
