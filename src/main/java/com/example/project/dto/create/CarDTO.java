package com.example.project.dto.create;

import com.example.project.model.Car;
import lombok.Data;

@Data
public class CarDTO {
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
}
