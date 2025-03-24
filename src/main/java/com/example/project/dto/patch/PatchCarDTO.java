package com.example.project.dto.patch;


import com.example.project.constant.CarStatus;
import com.example.project.constant.EngineType;
import com.example.project.constant.Transmission;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class PatchCarDTO {
    @Nullable
    private String brand;

    @Nullable
    private String model;

    @Min(1886)
    @Nullable
    private Integer year;

    @Min(0)
    @Nullable
    private Double price;

    @Min(0)
    @Nullable
    private Integer mileage;

    @Size(min = 17, max = 17)
    @Nullable
    private String vin;

    @Nullable
    private CarStatus status;

    @Nullable
    private Transmission transmission;

    @Nullable
    private String color;

    @Nullable
    private EngineType engineType;

    @Nullable
    private Long dealershipId;
}
