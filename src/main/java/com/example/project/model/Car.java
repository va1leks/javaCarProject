package com.example.project.model;

import com.example.project.constant.CarStatus;
import com.example.project.constant.EngineType;
import com.example.project.constant.Transmission;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String brand;
    private String model;
    private int year;
    private double price;
    private int mileage;
    @Column(unique = true, nullable = false)
    private  String vin;
    @Setter
    private CarStatus status;
    private Transmission transmission;
    private String color;
    private EngineType engineType;

    @JoinTable(
            name = "user_car",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> interestedUsers;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dealership_id")
    @ToString.Exclude
    private Dealership dealership = null;

}
