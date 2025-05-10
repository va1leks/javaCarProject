package com.example.project.dto.create;

import com.example.project.constant.CarStatus;
import com.example.project.constant.EngineType;
import com.example.project.constant.Transmission;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class CarDTO {

    @NotNull
    private String brand;

    @NotNull
    private String model;

    @NotNull
    @Min(1886)
    private int year;

    @NotNull
    @Min(0)
    private double price;

    @NotNull
    @Min(0)
    private int mileage;

    @NotNull
    @Size(min = 17, max = 17)
    private  String vin;

    @NotNull
    private CarStatus status;

    @NotNull
    private Transmission transmission;

    @NotNull
    private String color;

    @NotNull
    private EngineType engineType;

    @Nullable
    private String dealership;
}
