package com.example.project.dto.create;

import com.example.project.model.Car;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
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
    private Car.CarStatus status;

    @NotNull
    private Car.Transmission transmission;

    @NotNull
    private String color;

    @NotNull
    private Car.EngineType engineType;

    @Nullable
    private Long dealershipId;
}
