package com.example.project.dto.create;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ClientDTO {
    private String name;
    private String phone;
}
