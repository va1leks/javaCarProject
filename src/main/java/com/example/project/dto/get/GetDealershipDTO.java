package com.example.project.dto.get;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class GetDealershipDTO {
    Long id;
    String name;
    String address;
    private List<Long> cars;
}
