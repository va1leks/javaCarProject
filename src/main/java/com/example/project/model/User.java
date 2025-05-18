package com.example.project.model;

import com.example.project.constant.Roles;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.*;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String phone;

    private String password;


    @ElementCollection(targetClass = Roles.class)
    @Enumerated(EnumType.STRING)
    private List<Roles> roles;


    @ManyToMany(mappedBy = "interestedUsers",fetch = FetchType.EAGER, cascade = {CascadeType.DETACH,
        CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})


    private Set<Car> interestedCars;

}
