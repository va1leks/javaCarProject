package com.example.project.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    private String name;

    private String phone;

    @JoinTable(
            name = "user_car", // имя связующей таблицы
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "car_id")
    )
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Car> interestedCars ; // переименовано для согласованности



}
