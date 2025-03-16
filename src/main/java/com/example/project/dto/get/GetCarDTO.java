package com.example.project.dto.get;

import com.example.project.model.Car;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class GetCarDTO {
    Long id;
    private String brand;
    private String model;
    private int year;
    private double price;
    private int mileage;
    private  String vin;
    private Car.CarStatus status;
    private Car.Transmission transmission;
    private String color;
    private Car.EngineType engineType;
    private List<Long> interestedClients;
    private Long dealershipId;
}
