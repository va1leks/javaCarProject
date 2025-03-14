package com.example.project.model;

import jakarta.persistence.*;
import lombok.*;


import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Dealership {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String address;

    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Car> cars ;
}
