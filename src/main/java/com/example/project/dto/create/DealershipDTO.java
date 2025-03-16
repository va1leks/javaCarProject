package com.example.project.dto.create;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class DealershipDTO {
    @NotNull
    String name;
    @NotNull
    String address;
}
