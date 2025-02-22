package com.example.project.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Car {
    private int id;
    private String brand;
    private String model;
    private int year;
    private double price;
    private String description;
}
