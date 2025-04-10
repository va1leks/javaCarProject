package com.example.project.dto.get;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class GetDealershipDTO {
    Long id;
    String name;
    String address;
    private List<Long> cars;
}
