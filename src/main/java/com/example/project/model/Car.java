package com.example.project.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String brand;
    private String model;
    private int year;
    private double price;
    private int mileage;
    private  String vin;
    private CarStatus status;
    private Transmission transmission;
    private String color;
    private EngineType engineType;

    @ManyToMany(mappedBy = "interestedCars",fetch = FetchType.LAZY)
    private List<Client> interestedClients ;

    @ManyToOne
    private Dealership dealership;

    public enum EngineType{
        PETROL, DIESEL, ELECTRIC, HYBRID
    }
    public enum CarStatus {
        AVAILABLE, RESERVED, SOLD
    }

    public enum Transmission {
        MANUAL, AUTOMATIC
    }
}
