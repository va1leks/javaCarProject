package com.example.project.dto.get;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Builder
@Data
public class GetDealershipDTO {
    Long id;
    String name;
    String address;
    private List<Long> cars ;
}
